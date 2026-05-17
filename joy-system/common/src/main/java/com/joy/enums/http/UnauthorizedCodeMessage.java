package com.joy.enums.http;

import com.joy.common.HttpCodeMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UnauthorizedCodeMessage implements HttpCodeMessage {
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "unauthorized_access"),           // 未授权访问
    UNAUTHORIZED_ROLE_ACCESS(HttpStatus.FORBIDDEN, "unauthorized_role_access"),    // 角色未授权访问

    // 未登录细分场景
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "token已过期，请重新登录"),
    TOKEN_KICKED(HttpStatus.UNAUTHORIZED, "账号已在其他设备登录，您已被顶下线"),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "token无效"),
    NOT_LOGIN(HttpStatus.UNAUTHORIZED, "当前会话未登录");

    private final HttpStatus httpStatus;
    private final String message;

    UnauthorizedCodeMessage(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}