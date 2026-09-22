package com.joy.dto.dayByDay;

import lombok.Data;

@Data
public class IdPhotoGenerateDto {
    private Long userId;
    private String sessionId;
    private String sizeCode = "295,413";
    private String bgColor = "438EDB";
    private Integer beautyEnable = 0;
    private Integer skinSmooth = 0;
    private Integer suitEnable = 0;
    private Long suitId;
    private String format = "JPG";
    private Integer dpi = 300;
}
