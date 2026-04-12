package com.joy.utils;

import com.joy.entity.sysConfig.SysConfig;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


public class FileUtil {

    /**
     * 文件类型校验
     *
     * @param file       文件对象
     * @param type       文件类型标识
     * @param configList
     * @return 错误码或null（校验通过）
     */
    public static String validateFile(MultipartFile file, String type, Map<String, SysConfig> configList) {
        if (!configList.containsKey(type))
            return "invalid_file_type";
        String contentType = file.getContentType();
        if (contentType == null || !configList.get(type).getConfigRestrict().contains(contentType))
            return "unsupported_file_type";

        return null; // 校验通过
    }

    /**
     * 提取文件扩展名
     * @param fileName 文件名
     * @return 小写扩展名（如.jpg）
     */
    public static String extractExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
    }

    /**
     * 生成唯一文件名
     * @param originalName 原始文件名
     * @return
     */
    public static String generateUniqueName(String originalName) {
        String extension = originalName.substring(originalName.lastIndexOf("."));
        return UUID.randomUUID() + extension;
    }
}
