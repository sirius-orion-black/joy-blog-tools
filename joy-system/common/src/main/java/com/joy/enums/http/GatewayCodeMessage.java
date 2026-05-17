package com.joy.enums.http;

import com.joy.common.HttpCodeMessage;
import lombok.Getter;

/**
 * 网关相关业务消息码
 * 包含网关层面的业务消息定义
 */
@Getter
public enum GatewayCodeMessage implements HttpCodeMessage {
    LACK_TIMESTAMP("lack_of_timestamp"),      // 缺少时间戳
    INVALID_TIMESTAMP("invalid_timestamp"),   // 无效时间戳
    MANY_REQUESTS("too_many_requests");       // 请求过多

    private final String message;

    GatewayCodeMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}