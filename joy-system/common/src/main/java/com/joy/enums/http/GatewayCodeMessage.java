package com.joy.enums.http;

import com.joy.common.HttpCodeMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum GatewayCodeMessage implements HttpCodeMessage {
    LACK_TIMESTAMP(HttpStatus.BAD_REQUEST, "lack_of_timestamp"),      // 缺少时间戳
    INVALID_TIMESTAMP(HttpStatus.BAD_REQUEST, "invalid_timestamp"),   // 无效时间戳
    MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "too_many_requests"), // 请求过多
    DEVICE_ID_EMPTY(HttpStatus.BAD_REQUEST, "device_id_empty"); // 设备id为空


    private final HttpStatus httpStatus;
    private final String message;

    GatewayCodeMessage(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}