package com.joy.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.joy.common.Result;
import com.joy.entity.infra.FilesRecord;
import com.joy.entity.admin.sysConfig.SysConfig;
import com.joy.enums.http.InfraCodeMessage;
import com.joy.mapper.infra.FilesRecordMapper;
import com.joy.mapper.admin.sysConfig.SysConfigMapper;
import com.joy.service.FileService;
import com.joy.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@Slf4j
public class FileServiceImpl extends ServiceImpl<FilesRecordMapper, FilesRecord> implements FileService {

    @Autowired
    private SysConfigMapper sysConfigMapper;

    // 单例复用，线程安全，无状态
    private static final HashFunction HASH_FUNCTION = Hashing.murmur3_128();

    // 8KB 缓冲区，兼顾磁盘 IO 效率与内存占用
    private static final int BUFFER_SIZE = 8192;

    /**
     * 计算文件的 128 位 Murmur3 哈希值
     */
    public byte[] calculateFileHash(Path filePath) throws IOException {
        try (InputStream is = new BufferedInputStream(Files.newInputStream(filePath), BUFFER_SIZE)) {
            return calculateStreamHash(is);
        }
    }

    /**
     * 计算输入流的 128 位 Murmur3 哈希值
     * 适用于 MultipartFile.getInputStream() 等场景
     */
    @SuppressWarnings("UnstableApiUsage")
    public byte[] calculateStreamHash(InputStream inputStream) throws IOException {
        var hasher = HASH_FUNCTION.newHasher();
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            hasher.putBytes(buffer, 0, bytesRead);
        }

        HashCode hashCode = hasher.hash();
        // asBytes() 返回固定 16 字节的 byte[]，可直接存入 BINARY(16)
        return hashCode.asBytes();
    }

    /**
     * 仅用于日志/调试，严禁存入数据库
     */
    public static String toHexString(byte[] hashBytes) {
        StringBuilder sb = new StringBuilder(hashBytes.length * 2);
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * 获取数据库配置
     *
     * @return 返回config 中file信息
     */
    private Map<String, Map<String, SysConfig>> getFileConfig() {
        QueryWrapper<SysConfig> query = new QueryWrapper<>();
        query.in("config_type", Arrays.asList("file_storage", "file_type"));
        List<SysConfig> config = sysConfigMapper.selectList(query);
        return config.stream()
                .collect(Collectors.groupingBy(
                        row -> ((String) row.getConfigType()).toLowerCase(),
                        Collectors.toMap(
                                row -> (String) row.getConfigKey(),
                                Function.identity()
                                // (oldVal, newVal) -> newVal  // 处理重复key
                        )
                ));
    }

    /**
     * 单文件类型
     *
     * @param file 文件
     * @param fileType 文件类型
     * @param map config file 配置
     * @return 给前端返回结果
     */
    private Map<String, String> handleFileUpload(MultipartFile file, String fileType, String platform, Map<String, Map<String, SysConfig>> map) throws IOException {
        Map<String, String> result = new HashMap<>();
        Map<String, SysConfig> typeMap = map.get("file_type");
        Map<String, SysConfig> storageMap = map.get("file_storage");
        // 校验文件
        String error = FileUtil.validateFile(file, fileType, typeMap);
        if (error != null) {
            result.put("error", error);
            return result;
        }
        //流式计算哈希 (不先将整个文件读入内存)
        byte[] fileHash = this.calculateStreamHash(file.getInputStream());
        FilesRecord existing = this.getOne(new LambdaQueryWrapper<FilesRecord>().eq(FilesRecord::getFileHash, fileHash));
        if (existing != null) {
            result.put("url", existing.getUrl());
            result.put("id", existing.getId().toString());
            return result;
        }
        // 获取文件信息
        String originalName = file.getOriginalFilename();
        String fileExt = FileUtil.extractExtension(originalName);
        long fileSize = file.getSize();  // 获取文件大小（字节）

        // 获取年月
        YearMonth currentYearMonth = YearMonth.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        String dateStr = currentYearMonth.format(formatter);
        // 生成存储路径
        String fileTypeDir = map.get("file_type").get(fileType).getConfigValue()+dateStr+"/";
        Path targetDir = Paths.get(storageMap.get("file_storage_path").getConfigValue() + fileTypeDir);
        String storedName = FileUtil.generateUniqueName(Objects.requireNonNull(originalName));
        Path targetPath = targetDir.resolve(storedName);
        try {
            // 保存文件
            Files.createDirectories(targetDir);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            //数据保存
            FilesRecord record = new FilesRecord();
            record.setOriginalName(originalName);
            record.setStoredName(storedName);
            record.setFileType(fileType);
            record.setPath(targetPath.toString());
            record.setUrl(storageMap.get("file_access_address").getConfigValue()+fileTypeDir+storedName);
            record.setFileExt(fileExt);
            record.setFileSize(fileSize);
            record.setUserPlatform(Integer.parseInt(platform));
            record.setUserId(StpUtil.getLoginIdAsLong());
            record.setFileHash(fileHash);
            this.save(record);

            result.put("url", record.getUrl());
            result.put("id", record.getId().toString());
        } catch (IOException e) {
            result.put("error", "file_saving_failed");
        } catch (Exception e) {
            // 数据库异常时删除已保存的文件
            try {
                Files.deleteIfExists(targetPath);
            } catch (IOException ex) { /* 记录日志 */ }
            result.put("error", "database_operation_failed");
        }
        return result;
    }

    /**
     * 单文件上传
     *
     * @param file 文件
     * @param fileType 文件类型
     * @param platform 平台
     * @return 给前端返回结果
     */
    @Override
    public Result<Map<String, String>> uploadFile(MultipartFile file, String fileType, String platform) throws IOException {
        if (file.isEmpty()) {
            InfraCodeMessage.FILE_EMPTY.throwIt();
            return null;//永远也用不上，流氓点，去除黄标而已
        }
        Map<String, Map<String, SysConfig>> map = getFileConfig();
        Map<String, String> result = handleFileUpload(file, fileType, platform, map);
        if(!result.containsKey("url"))
            InfraCodeMessage.FILE_SAVING_FAILED.throwIt();
        return Result.success(result);

    }

    /**
     * 多文件上传
     *
     * @param files 多个文件
     * @param fileType 文件类型
     * @param platform config file 配置
     * @return 给前端返回结果
     */
    @Override
    public Result<Map<String, Object>> uploadMultiple(MultipartFile[] files, String fileType, String platform) {
        if (files == null || files.length == 0){
            InfraCodeMessage.NO_FILE.throwIt();
            return null;//永远也用不上，流氓点，去除黄标而已
        }
        Map<String, Map<String, SysConfig>> map = getFileConfig();
        List<Map<String, String>> results = Arrays.stream(files)
                .map(file -> {
                    try {
                        return handleFileUpload(file, fileType, platform, map);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("results", results); // 包含每个文件的上传结果
        return Result.success(response);
    }
}
