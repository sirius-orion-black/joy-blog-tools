package com.joy.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joy.common.Result;
import com.joy.entity.files.FeFilesRecord;
import com.joy.mapper.files.FeFilesRecordMapper;
import com.joy.service.FileService;
import com.joy.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class FileServiceImpl extends ServiceImpl<FeFilesRecordMapper, FeFilesRecord> implements FileService {

    // 文件存储路径配置
    private static final Map<String, String> TYPE_PATH = new HashMap<>();

    static {
        TYPE_PATH.put("avatar", "static/avatar-images/");
        TYPE_PATH.put("cover", "static/cover-images/");
        TYPE_PATH.put("document", "static/document-files/");
    }

    /**
     * 单文件类型
     *
     * @param file
     * @param fileType
     * @return
     */
    private Map<String, String> handleFileUpload(MultipartFile file, String fileType, String platform) {
        Map<String, String> result = new HashMap<>();
        // 校验文件
        String error = FileUtil.validateFile(file, fileType);
        if (error != null) {
            result.put("error", error);
            return result;
        }

        // 获取文件信息
        String originalName = file.getOriginalFilename();
        String fileExt = FileUtil.extractExtension(originalName);
        long fileSize = file.getSize();  // 获取文件大小（字节）

        // 生成存储路径
        Path targetDir = Paths.get(TYPE_PATH.get(fileType));
        String storedName = FileUtil.generateUniqueName(Objects.requireNonNull(originalName));
        Path targetPath = targetDir.resolve(storedName);
        try {
            // 保存文件
            Files.createDirectories(targetDir);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            FeFilesRecord record = new FeFilesRecord();
            record.setOriginalName(file.getOriginalFilename());
            record.setStoredName(storedName);
            record.setFileType(fileType);
            record.setPath(targetPath.toString());
            record.setUrl(storedName);
            record.setFileExt(fileExt);
            record.setFileSize(fileSize);
            record.setUserPlatform(Integer.parseInt(platform));
            record.setUserId(StpUtil.getLoginIdAsLong());

            this.save(record);


            result.put("url", storedName);
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

    @Override
    public Result<Map<String, String>> uploadFile(MultipartFile file, String fileType, String platform) {
        if (file.isEmpty()) return Result.badRequest("file_cannot_empty");
        Map<String, String> result = handleFileUpload(file, fileType, platform);
        return result.containsKey("url") ?
                Result.success(result) :
                Result.badRequest(result.get("error"));
    }

    @Override
    public Result<Map<String, Object>> uploadMultiple(MultipartFile[] files, String fileType, String platform) {
        if (files == null || files.length == 0) return Result.badRequest("please_select_file");

        List<Map<String, String>> results = Arrays.stream(files)
                .map(file -> handleFileUpload(file, fileType, platform))
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("results", results); // 包含每个文件的上传结果
        return Result.success(response);
    }
}
