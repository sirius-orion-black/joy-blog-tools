package com.joy.enums.http;

import com.joy.common.HttpCodeMessage;
import lombok.Getter;

/**
 * 通用业务消息码
 * 包含系统通用的业务消息定义
 */
@Getter
public enum CommonCodeMessage implements HttpCodeMessage {
    SUCCESS("success"),                             // 成功
    CREATED("created"),                             // 已创建
    ACCEPTED("accepted"),                           // 已接受
    NO_CONTENT("no_content"),                       // 无内容
    BAD_REQUEST("bad_request"),                     // 错误请求
    UNAUTHORIZED("unauthorized"),                   // 未授权
    FORBIDDEN("forbidden"),                         // 禁止访问
    NOT_FOUND("not_found"),                         // 资源未找到
    METHOD_NOT_ALLOWED("method_not_allowed"),       // 方法不允许
    REQUEST_TIMEOUT("request_timeout"),             // 请求超时
    INTERNAL_SERVER_ERROR("internal_server_error"), // 内部服务器错误
    BAD_GATEWAY("bad_gateway"),                     // 网关错误
    SERVICE_UNAVAILABLE("service_unavailable"),     // 服务不可用
    GATEWAY_TIMEOUT("gateway_timeout");             // 网关超时

    private final String message;

    CommonCodeMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
