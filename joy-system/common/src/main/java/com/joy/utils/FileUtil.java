package com.joy.utils;

import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.*;


public class FileUtil {

    // 文件类型校验规则
    private static final Map<String, List<String>> TYPE_RULES = new HashMap<>();
    static {
        TYPE_RULES.put("avatar", Arrays.asList("image/jpeg", "image/png", "image/gif"));
        TYPE_RULES.put("cover", Arrays.asList("image/jpeg", "image/png", "image/webp"));
        TYPE_RULES.put("document", Arrays.asList("application/pdf", "text/plain"));
    }

    /**
     * 文件类型校验
     * @param file 文件对象
     * @param type 文件类型标识
     * @return 错误码或null（校验通过）
     */
    public static String validateFile(MultipartFile file, String type) {
        if (!TYPE_RULES.containsKey(type))
            return "invalid_file_type";

        String contentType = file.getContentType();
        if (contentType == null || !TYPE_RULES.get(type).contains(contentType))
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
