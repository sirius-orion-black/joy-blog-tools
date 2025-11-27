package com.joy.enums.result;

import lombok.Getter;

@Getter
public enum HttpStatusCode {

    Success("SUCCESS",200),//请求成功
    Created("CREATED",201),//请求成功并创建了新资源
    BadRequest("BAD REQUEST",400),//服务器无法理解请求格式
    Unauthorized("UNAUTHORIZED",401),//请求需要用户验证
    Forbidden("FORBIDDEN",403),//服务器拒绝请求
    NotFound("NOT FOUND",404),//请求的资源不存在
    InternalServerError("INTERNAL SERVER ERROR",500);//服务器异常

    private final String message;
    private final int code;

    HttpStatusCode(String message,int code) {
        this.code = code;
        this.message = message;
    }

//    public static HttpStatusCode fromCode(int code) {
//        for (HttpStatusCode status : values()) {
//            if (status.code == code) {
//                return status;
//            }
//        }
//        throw new IllegalArgumentException("无效的HTTP状态码: " + code);
//    }

}
