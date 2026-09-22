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

    @PostMapping("/preview")
    public Result<Map<String, Object>> preview(@RequestParam("file") MultipartFile file) throws Exception {
        return photoService.preview(file);
    }

    @PostMapping("/generate")
    public DayPhotoRecord generate(@RequestBody IdPhotoGenerateDto req) throws Exception {
        return photoService.generate(req);
    }


}
