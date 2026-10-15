package com.joy.controller;


import com.joy.common.Result;
import com.joy.config.apiPrefix.ApiPrefixDayByDayRestController;
import com.joy.dto.dayByDay.IdPhotoGenerateDto;
import com.joy.entity.dayByDay.DayPhotoRecord;
import com.joy.entity.dayByDay.DayPhotoSize;
import com.joy.entity.dayByDay.DayPhotoSuit;
import com.joy.service.IdPhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@ApiPrefixDayByDayRestController
@RequiredArgsConstructor
@RequestMapping("/id/photo")
public class IdPhotoController {

    private final IdPhotoService photoService;


    /**
     * 获取证件照尺寸配置
     * @return 返回尺寸列表
     */
    @GetMapping("/sizes")
    public Result<List<DayPhotoSize>> photoSizes() {
        return photoService.photoSizes();
    }

    /**
     * @param gender 性别
     * @return 返回服装列表
     */
    @GetMapping("/suits")
    public Result<List<DayPhotoSuit>> photoSuits(@RequestParam(required = false) Integer gender) {
        return photoService.photoSuits(gender);
    }

    /**
     *
     * @param file 证件照原始文件
     * @param fileType 类型
     * @param platform 平台
     * @return 返回预览图
     */
    @PostMapping("/preview")
    public Result<Map<String, Object>> photoPreview(@RequestParam("file") MultipartFile file,
                                                    @RequestParam("type") String fileType,
                                                    @RequestParam("platform") String platform) throws Exception {
        return photoService.photoPreview(file,fileType,platform);
    }

    @PostMapping("/changeSuit")
    public Result<Map<String, Object>> photoChangeSuit(@RequestBody IdPhotoGenerateDto photo){
        return photoService.photoChangeSuit(photo);
    }

    @PostMapping("/process")
    public Result<DayPhotoRecord> photoProcess(@RequestBody IdPhotoGenerateDto photo) throws Exception {
        return photoService.photoProcess(photo);
    }


}
