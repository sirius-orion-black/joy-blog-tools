package com.joy.service;

import com.joy.common.Result;
import com.joy.dto.dayByDay.IdPhotoGenerateDto;
import com.joy.entity.dayByDay.DayPhotoRecord;
import com.joy.entity.dayByDay.DayPhotoSize;
import com.joy.entity.dayByDay.DayPhotoSuit;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IdPhotoService {
    Result<List<DayPhotoSize>> photoSizes();

    Result<List<DayPhotoSuit>> photoSuits(Integer gender);

    void syncSuits();

    Result<Map<String, Object>> preview(MultipartFile file) throws IOException;

    DayPhotoRecord generate(IdPhotoGenerateDto req);
}
