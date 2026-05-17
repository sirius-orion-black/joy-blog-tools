package com.joy.enums.http;

import com.joy.common.HttpCodeMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CommonCodeMessage implements HttpCodeMessage {
    SUCCESS(HttpStatus.OK, "success"),                             // 成功
    CREATED(HttpStatus.CREATED, "created"),                        // 已创建
    ACCEPTED(HttpStatus.ACCEPTED, "accepted"),                     // 已接受
    NO_CONTENT(HttpStatus.NO_CONTENT, "no_content"),               // 无内容
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "bad_request"),            // 错误请求
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "unauthorized"),         // 未授权
    FORBIDDEN(HttpStatus.FORBIDDEN, "forbidden"),                  // 禁止访问
    NOT_FOUND(HttpStatus.NOT_FOUND, "not_found"),                  // 资源未找到
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "method_not_allowed"), // 方法不允许
    REQUEST_TIMEOUT(HttpStatus.REQUEST_TIMEOUT, "request_timeout"), // 请求超时
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "internal_server_error"), // 内部服务器错误
    BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "bad_gateway"),            // 网关错误
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "service_unavailable"), // 服务不可用
    GATEWAY_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "gateway_timeout"); // 网关超时

    private final HttpStatus httpStatus;
    private final String message;

    CommonCodeMessage(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}