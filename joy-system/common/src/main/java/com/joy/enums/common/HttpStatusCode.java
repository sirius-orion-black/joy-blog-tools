package com.joy.enums.common;

import lombok.Getter;

@Getter
public enum HttpStatusCode {

    Success(200,"SUCCESS","success"),//请求成功
    Created(201,"CREATED","created"),//请求成功并创建了新资源
    BadRequest(400,"BAD REQUEST","bad_request"),//服务器无法理解请求格式
    Unauthorized(401,"UNAUTHORIZED","unauthorized"),//请求需要用户验证
    Forbidden(403,"FORBIDDEN","forbidden"),//服务器拒绝请求
    NotFound(404,"NOT FOUND","not_found"),//请求的资源不存在
    InternalServerError(500,"INTERNAL SERVER ERROR","internal_server_error");//服务器异常

    private final String state;
    private final int code;
    private final String message;

    HttpStatusCode(int code,String state, String message) {
        this.code = code;
        this.state = state;
        this.message = message;
    }

    public static Boolean fromCode(int code) {
        for (HttpStatusCode status : values()) {
            if (status.code == code) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

}
