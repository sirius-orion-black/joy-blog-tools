package com.joy.enums.common;

import lombok.Getter;

@Getter
public enum RedisConstant {

    SliderVerificationCode("slider_verification_code"),
    OneMinute(100),
    FiveMinutes(300);

    private final Object value;

    RedisConstant(Object value) {
        this.value = value;
    }
}
