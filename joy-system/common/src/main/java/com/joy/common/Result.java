package com.joy.common;

import com.joy.utils.EnhancedEmptyObjectUtil;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.http.HttpStatus;

/**
 * 统一响应结果类
 *
 * @param <T>
 */
@Data
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
     * 参数顺序：状态 -> 消息 -> 数据
     */
    public Result(HttpStatus httpStatus, String message, T data) {
        this.code = httpStatus.value();
        this.state = httpStatus.getReasonPhrase();
        // 如果提供了自定义消息则使用自定义消息，否则使用默认消息
        this.message = (message != null && !message.isEmpty()) ? message : httpStatus.getReasonPhrase().toLowerCase(java.util.Locale.ENGLISH);
        this.data = data == null ? (T) EnhancedEmptyObjectUtil.getInstance() : data;
    }

    /**
     * 创建响应结果 - 仅状态码（无数据、无自定义消息）
     *
     * @param httpStatus HTTP状态码
     * @param <T>        响应数据类型
     * @return Result实例
     */
    public static <T> Result<T> of(HttpStatus httpStatus) {
        return new Result<>(httpStatus, null, null);
    }

    /**
     * 创建响应结果 - 状态码+自定义消息（无数据）
     *
     * @param httpStatus HTTP状态码
     * @param message    自定义业务消息码
     * @param <T>        响应数据类型
     * @return Result实例
     */
    public static <T> Result<T> of(HttpStatus httpStatus, String message) {
        return new Result<>(httpStatus, message, null);
    }

    /**
     * 创建响应结果 - 状态码+数据（无自定义消息）
     *
     * @param httpStatus HTTP状态码
     * @param data       响应数据
     * @param <T>        响应数据类型
     * @return Result实例
     */
    public static <T> Result<T> of(HttpStatus httpStatus, T data) {
        return new Result<>(httpStatus, null, data);
    }

    /**
     * 创建响应结果 - 状态码+数据+自定义消息
     *
     * @param httpStatus HTTP状态码
     * @param data       响应数据
     * @param message    自定义业务消息码
     * @param <T>        响应数据类型
     * @return Result实例
     */
    public static <T> Result<T> of(HttpStatus httpStatus, String message, T data) {
        return new Result<>(httpStatus, message, data);
    }

    /**
     * ================== 成功响应 ==================
     */
    public static <T> Result<T> success() {
        return of(HttpStatus.OK);
    }

    public static <T> Result<T> success(String message) {
        return of(HttpStatus.OK, message);
    }

    public static <T> Result<T> success(T data) {
        return of(HttpStatus.OK, data);
    }

    public static <T> Result<T> success(T data, String message) {
        return of(HttpStatus.OK, message, data);
    }

    /**
     * ================== 失败响应 ==================
     */
    public static <T> Result<T> fail(HttpStatus httpStatus) {
        return of(httpStatus);
    }

    public static <T> Result<T> fail(HttpStatus httpStatus, String message) {
        return of(httpStatus, message);
    }

    public static <T> Result<T> fail(HttpStatus httpStatus, T data, String message) {
        return of(httpStatus, message, data);
    }

    public static <D> Result<D> error(HttpStatus httpStatus, String message, D data) {
//        return new Result<>(httpStatus, message, data);
        return of(httpStatus, message, data);
    }
}