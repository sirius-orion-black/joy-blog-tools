package com.joy.dto.dayByDay;

import com.fasterxml.jackson.annotation.JsonProperty;

public record IdPhotoHumanMattingDto(
        boolean status,

        @JsonProperty("image_base64")
        String imageBase64
) {}
