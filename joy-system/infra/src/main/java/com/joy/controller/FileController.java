package com.joy.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.joy.common.Result;
import com.joy.config.apiPrefix.ApiPrefixInfraRestController;
import com.joy.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@ApiPrefixInfraRestController
@RequestMapping("/manage")
@Slf4j
public class FileController {

    @Autowired
    private FileService fileService;

    // 单文件上传
    @SaCheckLogin
    @PostMapping("/upload")
    public Result<Map<String,String>> uploadFile(@RequestParam("file") MultipartFile file,
                                                 @RequestParam("type") String fileType,
                                                 @RequestParam("platform") String platform){
        return fileService.uploadFile(file,fileType,platform);
    }

    // 多文件上传（同类型）
    @SaCheckLogin
    @PostMapping("/multiple")
    public Result<Map<String,Object>> uploadMultiple(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("type") String fileType,
            @RequestParam("platform") String platform) {
        return fileService.uploadMultiple(files,fileType,platform);
    }
}
