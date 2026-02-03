package com.joy.service;

import com.joy.common.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface FileService {
    Result<Map<String,String>> uploadFile(MultipartFile file, String fileType, String platform);

    Result<Map<String,Object>> uploadMultiple(MultipartFile[] files, String fileType, String platform);
}
