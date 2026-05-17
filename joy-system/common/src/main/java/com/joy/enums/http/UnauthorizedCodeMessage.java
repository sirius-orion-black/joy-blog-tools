package com.joy.enums.http;

import com.joy.common.HttpCodeMessage;
import lombok.Getter;

/**
 * 权限相关业务消息码
 * 包含认证、授权相关的业务消息定义
 */
@Getter
public enum UnauthorizedCodeMessage implements HttpCodeMessage {
    UNAUTHORIZED_ACCESS("unauthorized_access"),           // 未授权访问
    UNAUTHORIZED_ROLE_ACCESS("unauthorized_role_access"); // 角色未授权访问

    private final String message;

    UnauthorizedCodeMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}