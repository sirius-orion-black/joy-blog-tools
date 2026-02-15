package com.joy.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joy.common.Result;
import com.joy.entity.files.FilesRecord;
import com.joy.entity.sysConfig.SysConfig;
import com.joy.mapper.files.FilesRecordMapper;
import com.joy.mapper.sysConfig.SysConfigMapper;
import com.joy.service.FileService;
import com.joy.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@Slf4j
public class FileServiceImpl extends ServiceImpl<FilesRecordMapper, FilesRecord> implements FileService {

    @Autowired
    private SysConfigMapper sysConfigMapper;

    /**
     * 获取数据库配置
     *
     * @return
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
     * @param file
     * @param fileType
     * @param map
     * @return
     */
    private Map<String, String> handleFileUpload(MultipartFile file, String fileType, String platform, Map<String, Map<String, SysConfig>> map) {
        Map<String, String> result = new HashMap<>();
        Map<String, SysConfig> typeMap = map.get("file_type");
        Map<String, SysConfig> storageMap = map.get("file_storage");
        // 校验文件
        String error = FileUtil.validateFile(file, fileType, typeMap);
        if (error != null) {
            result.put("error", error);
            return result;
        }
        // 获取文件信息
        String originalName = file.getOriginalFilename();
        String fileExt = FileUtil.extractExtension(originalName);
        long fileSize = file.getSize();  // 获取文件大小（字节）

        // 生成存储路径
        String fileTypeDir = map.get("file_type").get(fileType).getConfigValue();
        Path targetDir = Paths.get(storageMap.get("file_storage_path").getConfigValue() + fileTypeDir);
        String storedName = FileUtil.generateUniqueName(Objects.requireNonNull(originalName));
        Path targetPath = targetDir.resolve(storedName);
        try {
            // 保存文件
            Files.createDirectories(targetDir);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            //数据保存
            FilesRecord record = new FilesRecord();
            record.setOriginalName(file.getOriginalFilename());
            record.setStoredName(storedName);
            record.setFileType(fileType);
            record.setPath(targetPath.toString());
            record.setUrl(storageMap.get("file_access_address").getConfigValue()+fileTypeDir+storedName);
            record.setFileExt(fileExt);
            record.setFileSize(fileSize);
            record.setUserPlatform(Integer.parseInt(platform));
            record.setUserId(StpUtil.getLoginIdAsLong());
            this.save(record);

            result.put("url", record.getUrl());
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
     * @param file
     * @param fileType
     * @param platform
     * @return
     */
    @Override
    public Result<Map<String, String>> uploadFile(MultipartFile file, String fileType, String platform) {
        if (file.isEmpty()) return Result.badRequest("file_cannot_empty");
        Map<String, Map<String, SysConfig>> map = getFileConfig();
        Map<String, String> result = handleFileUpload(file, fileType, platform, map);
        return result.containsKey("url") ?
                Result.success(result) :
                Result.badRequest(result.get("error"));
    }

    /**
     * 多文件上传
     *
     * @param files
     * @param fileType
     * @param platform
     * @return
     */
    @Override
    public Result<Map<String, Object>> uploadMultiple(MultipartFile[] files, String fileType, String platform) {
        if (files == null || files.length == 0) return Result.badRequest("please_select_file");
        Map<String, Map<String, SysConfig>> map = getFileConfig();
        List<Map<String, String>> results = Arrays.stream(files)
                .map(file -> handleFileUpload(file, fileType, platform, map))
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("results", results); // 包含每个文件的上传结果
        return Result.success(response);
    }
}
