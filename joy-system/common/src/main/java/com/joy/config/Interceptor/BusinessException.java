package com.joy.config.Interceptor;

import com.joy.common.HttpCodeMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final Object data;

    // 无数据的构造方法
    public BusinessException(HttpCodeMessage codeMessage) {
        this(codeMessage, null);
    }

    // 带数据的构造方法
    public BusinessException(HttpCodeMessage codeMessage, Object data) {
        super(codeMessage.getMessage());
        this.httpStatus = codeMessage.getHttpStatus();
        this.data = data;
    }
}