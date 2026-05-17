package com.joy.common;

import com.joy.config.Interceptor.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * 业务消息码接口
 * 所有模块的业务消息码都需要实现此接口
 * 按模块拆分
 */
public interface HttpCodeMessage {
    HttpStatus getHttpStatus();
    String getMessage();

    // 无参抛出
    default void throwIt() {
        throw new BusinessException(this);
    }

    // 带数据抛出
    default void throwIt(Object data) {
        throw new BusinessException(this, data);
    }
}