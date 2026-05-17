package com.joy.common;

import com.joy.enums.http.HttpStatusCode;
import com.joy.utils.EnhancedEmptyObjectUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 统一响应结果类
 * 设计目标：
 * 1. HttpStatusCode只包含code和state
 * 2. message部分按模块拆分为多个文件
 * @param <T> 响应数据的类型
 */
@Data
@Slf4j
public class Result<T> {

    @ApiModelProperty(value = "HTTP状态码", example = "200")
    private Integer code;

    @ApiModelProperty(value = "HTTP状态描述", example = "OK")
    private String state;

    @ApiModelProperty(value = "业务消息码", example = "success")
    private String message;

    @ApiModelProperty(value = "响应数据")
    private T data;

    /**
     * 无参构造函数
     */
    public Result() {
    }

    /**
     * 主构造函数
     * @param status HTTP状态码枚举
     * @param message 业务消息码，如果为null或空则使用状态码的默认消息
     * @param data 响应数据
     */
    public Result(HttpStatusCode status, String message, T data) {
        this.code = status.getCode();
        this.state = status.getState();
        // 如果提供了自定义消息则使用自定义消息，否则使用默认消息
        this.message = message != null && !message.isEmpty() ? message : status.getDefaultMessage();
        this.data = data == null ? (T) EnhancedEmptyObjectUtil.getInstance() : data;
    }

    /**
     * 构造函数（无数据版本）
     *
     * @param status HTTP状态码枚举
     * @param message 业务消息码
     */
    public Result(HttpStatusCode status, String message) {
        this(status, message, null);
    }

    /**
     * 创建响应结果 - 仅状态码（无数据、无自定义消息）
     * @param status HTTP状态码
     * @param <T> 响应数据类型
     * @return Result实例
     */
    public static <T> Result<T> of(HttpStatusCode status) {
        return new Result<>(status, null);
    }

    /**
     * 创建响应结果 - 状态码+数据（无自定义消息）
     * @param status HTTP状态码
     * @param data 响应数据
     * @param <T> 响应数据类型
     * @return Result实例
     */
    public static <T> Result<T> of(HttpStatusCode status, T data) {
        return new Result<>(status, null, data);
    }

    /**
     * 创建响应结果 - 状态码+自定义消息（无数据）
     * @param status HTTP状态码
     * @param message 自定义业务消息码
     * @param <T> 响应数据类型
     * @return Result实例
     */
    public static <T> Result<T> of(HttpStatusCode status, String message) {
        return new Result<>(status, message);
    }

    /**
     * 创建响应结果 - 状态码+数据+自定义消息
     * @param status HTTP状态码
     * @param data 响应数据
     * @param message 自定义业务消息码
     * @param <T> 响应数据类型
     * @return Result实例
     */
    public static <T> Result<T> of(HttpStatusCode status, T data, String message) {
        return new Result<>(status, message, data);
    }


    /**
     * 成功响应 - 无数据、无自定义消息
     * 默认使用HttpStatusCode.SUCCESS和默认消息
     * @param <T> 响应数据类型
     * @return Result实例
     */
    public static <T> Result<T> success() {
        return of(HttpStatusCode.SUCCESS);
    }

    /**
     * 成功响应 - 有数据、无自定义消息
     * @param data 响应数据
     * @param <T> 响应数据类型
     * @return Result实例
     */
    public static <T> Result<T> success(T data) {
        return of(HttpStatusCode.SUCCESS, data);
    }

    /**
     * 成功响应 - 无数据、有自定义消息
     * @param message 自定义业务消息码
     * @param <T> 响应数据类型
     * @return Result实例
     */
    public static <T> Result<T> success(String message) {
        return of(HttpStatusCode.SUCCESS, message);
    }

    /**
     * 成功响应 - 有数据、有自定义消息
     * @param data 响应数据
     * @param message 自定义业务消息码
     * @param <T> 响应数据类型
     * @return Result实例
     */
    public static <T> Result<T> success(T data, String message) {
        return of(HttpStatusCode.SUCCESS, data, message);
    }


    /**
     * 失败响应 - 仅状态码（无数据、无自定义消息）
     * @param status HTTP状态码
     * @param <T> 响应数据类型
     * @return Result实例
     */
    public static <T> Result<T> fail(HttpStatusCode status) {
        return of(status);
    }

    /**
     * 失败响应 - 状态码+自定义消息（无数据）
     * @param status HTTP状态码
     * @param message 自定义业务消息码
     * @param <T> 响应数据类型
     * @return Result实例
     */
    public static <T> Result<T> fail(HttpStatusCode status, String message) {
        return of(status, message);
    }

    /**
     * 失败响应 - 状态码+数据（无自定义消息）
     * @param status HTTP状态码
     * @param data 响应数据
     * @param <T> 响应数据类型
     * @return Result实例
     */
    public static <T> Result<T> fail(HttpStatusCode status, T data) {
        return of(status, data);
    }

    /**
     * 失败响应 - 状态码+数据+自定义消息
     * @param status HTTP状态码
     * @param data 响应数据
     * @param message 自定义业务消息码
     * @param <T> 响应数据类型
     * @return Result实例
     */
    public static <T> Result<T> fail(HttpStatusCode status, T data, String message) {
        return of(status, data, message);
    }




    //==========下面的代码后续删除，仅过度使用==========
    /**
     * 请求成功并创建了新资源
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> created() {
        return of(HttpStatusCode.CREATED);
    }

    /**
     * 服务器无法理解请求格式
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> badRequest() {
        return of(HttpStatusCode.BAD_REQUEST);
    }

    /**
     * 服务器无法理解请求格式 只传消息
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> badRequest(String message) {
        return of(HttpStatusCode.BAD_REQUEST, message);
    }

    /**
     * 服务器无法理解请求格式 传数据和消息
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> badRequest(T data, String message) {
        return of(HttpStatusCode.BAD_REQUEST, data, message);
    }

    /**
     * 请求需要用户验证
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> unauthorized() {
        return of(HttpStatusCode.UNAUTHORIZED);
    }

    /**
     * 请求需要用户验证 只传消息
     *
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Result<T> unauthorized(String message) {
        return of(HttpStatusCode.UNAUTHORIZED, message);
    }

    /**
     * 服务器拒绝请求
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> forbidden() {
        return of(HttpStatusCode.FORBIDDEN);
    }

    /**
     * 服务器拒绝请求 只传消息
     *
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Result<T> forbidden(String message) {
        return of(HttpStatusCode.FORBIDDEN, message);
    }

    /**
     * 请求的资源不存在
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> notFound() {
        return of(HttpStatusCode.NOT_FOUND);
    }

    /**
     * 服务器异常
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> internalServerError() {
        return of(HttpStatusCode.INTERNAL_SERVER_ERROR);
    }

    /**
     * 服务器异常 只传消息
     *
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Result<T> internalServerError(String message) {
        return of(HttpStatusCode.INTERNAL_SERVER_ERROR, message);
    }
}