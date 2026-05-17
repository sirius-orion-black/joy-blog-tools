package com.joy.enums.http;

import lombok.Getter;

/**
 * HTTP状态码枚举
 * - code: HTTP状态码（如200, 400, 500等）
 * - state: HTTP状态描述（如"OK", "Bad Request", "Internal Server Error"等）
 * - defaultMessage: 对应的默认业务消息码
 * - 只包含标准HTTP状态码和对应的状态信息，不含具体的业务消息
 */
@Getter
public enum HttpStatusCode {
    // 2xx 成功状态
    SUCCESS(200, "OK", "success"),                     // 成功
    CREATED(201, "Created", "created"),                // 已创建
    ACCEPTED(202, "Accepted", "accepted"),             // 已接受
    NO_CONTENT(204, "No Content", "no_content"),       // 无内容

    // 4xx 客户端错误
    BAD_REQUEST(400, "Bad Request", "bad_request"),    // 错误请求
    UNAUTHORIZED(401, "Unauthorized", "unauthorized"), // 未授权
    FORBIDDEN(403, "Forbidden", "forbidden"),          // 禁止访问
    NOT_FOUND(404, "Not Found", "not_found"),          // 资源未找到
    METHOD_NOT_ALLOWED(405, "Method Not Allowed", "method_not_allowed"), // 方法不允许
    REQUEST_TIMEOUT(408, "Request Timeout", "request_timeout"),         // 请求超时

    // 5xx 服务器错误
    INTERNAL_SERVER_ERROR(500, "Internal Server Error", "internal_server_error"), // 内部服务器错误
    BAD_GATEWAY(502, "Bad Gateway", "bad_gateway"),                               // 网关错误
    SERVICE_UNAVAILABLE(503, "Service Unavailable", "service_unavailable"),       // 服务不可用
    GATEWAY_TIMEOUT(504, "Gateway Timeout", "gateway_timeout");                  // 网关超时

    /**
     * HTTP状态码（如200, 400, 500等）
     */
    private final int code;

    /**
     * HTTP状态描述（如"OK", "Bad Request", "Internal Server Error"等）
     */
    private final String state;

    /**
     * 对应的默认业务消息码
     */
    private final String defaultMessage;

    /**
     * 构造函数
     * @param code HTTP状态码
     * @param state HTTP状态描述
     * @param defaultMessage 默认业务消息码
     */
    HttpStatusCode(int code, String state, String defaultMessage) {
        this.code = code;
        this.state = state;
        this.defaultMessage = defaultMessage;
    }
}