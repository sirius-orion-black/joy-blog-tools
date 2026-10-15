package com.joy.utils;

import com.joy.entity.admin.sysConfig.SysConfig;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
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

    /**
     *
     * @param mat 待转换的图像矩阵
     * @param fileName 上传时使用的文件名（
     * @return 可直接用于调用上传接口的 MultipartFile
     */
    public static MultipartFile convertMatToMultipartFile(Mat mat, String fileName) {
        // 将 Mat 编码为内存 PNG 字节流
        try (MatVector buf = new MatVector()) {
            opencv_imgcodecs.imencode(".png", mat, buf.asByteBuffer());

            // JavaCV 1.5.14 安全提取堆外内存字节
            try (BytePointer bp = buf.get(0).data()) {
                byte[] imageBytes = new byte[(int) bp.capacity()];
                bp.get(imageBytes);

                // 使用自定义实现，无需 spring-test 依赖
                return new ByteArrayMultipartFile(
                        "file",          // 必须与上传接口 @RequestParam 名称一致
                        fileName,
                        "image/png",
                        imageBytes
                );
            }
        }
    }
}
