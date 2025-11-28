package com.joy.common;

import com.joy.enums.result.HttpStatusCode;
import com.joy.untils.EnhancedEmptyObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Data
@Slf4j
public class Result<T> {

    @ApiModelProperty(value="状态码")
    private Integer code;

    @ApiModelProperty(value = "状态")
    private String state;

    @ApiModelProperty(value = "消息")
    private String message;

    @ApiModelProperty(value = "数据")
    private T data;

    public Result() {}

    /**
     * 抽取公共方法，统一处理
     * @param status
     * @param data
     */
    public Result(HttpStatusCode status, T data) {
        if (!HttpStatusCode.fromCode(status.getCode())) {
            log.info("状态码不存在");
            throw new IllegalArgumentException("status_code_does_not_exist");
        }
        this.code = status.getCode();
        this.state = status.getState();
        this.message = status.getMessage();
        if (data == null) {
            // 使用类型转换确保类型安全
            this.data = (T) EnhancedEmptyObject.getInstance();
        } else {
            this.data = data;
        }
    }

    public static <T> Result<T> of(HttpStatusCode status, T data) {
        return new Result<>(status, data);
    }


    /**
     * 请求成功
     * @param data
     * @return
     * @param <T>
     */
    public static <T> Result<T> success(T data) {
        return of(HttpStatusCode.Success, data);
    }

    /**
     * 请求成功
     * @return
     * @param <T>
     */
    public static <T> Result<T> success() {
        return of(HttpStatusCode.Success,null);
    }

    /**
     * 请求成功 只传消息
     * @param message
     * @return
     * @param <T>
     */
    public static <T> Result<T> success(String message) {
        Result<T> result = of(HttpStatusCode.Success, null);
        result.setMessage(message);
        return result;
    }

    /**
     * 请求成功 传数据和消息
     * @param data
     * @param message
     * @return
     * @param <T>
     */
    public static <T> Result<T> success(T data, String message) {
        Result<T> result = of(HttpStatusCode.Success, data);
        result.setMessage(message);
        return result;
    }

    /**
     * 请求成功并创建了新资源
     * @return
     * @param <T>
     */
    public static <T> Result<T> created() {
        return of(HttpStatusCode.Created,null);
    }

    /**
     * 服务器无法理解请求格式
     * @return
     * @param <T>
     */
    public static <T> Result<T> badRequest() {
        return of(HttpStatusCode.BadRequest,null);
    }

    /**
     * 请求需要用户验证
     * @return
     * @param <T>
     */
    public static <T> Result<T> unauthorized() {
        return of(HttpStatusCode.Unauthorized,null);
    }

    /**
     * 请求需要用户验证 只传消息
     * @param message
     * @return
     * @param <T>
     */
    public static <T> Result<T> unauthorized(String message) {
        Result<T> result = of(HttpStatusCode.Unauthorized, null);
        result.setMessage(message);
        return result;
    }

    /**
     * 服务器拒绝请求
     * @return
     * @param <T>
     */
    public static <T> Result<T> forbidden() {
        return of(HttpStatusCode.Forbidden,null);
    }

    /**
     * 服务器拒绝请求 只传消息
     * @param message
     * @return
     * @param <T>
     */
    public static <T> Result<T> forbidden(String message) {
        Result<T> result = of(HttpStatusCode.Forbidden, null);
        result.setMessage(message);
        return result;
    }

    /**
     * 请求的资源不存在
     * @return
     * @param <T>
     */
    public static <T> Result<T> notFound() {
        return of(HttpStatusCode.NotFound,null);
    }

    /**
     * 服务器异常
     * @return
     * @param <T>
     */

    public static <T> Result<T> internalServerError() {
        return of(HttpStatusCode.InternalServerError,null);
    }

    /**
     * 服务器异常 只传消息
     * @param message
     * @return
     * @param <T>
     */
    public static <T> Result<T> internalServerError(String message) {
        Result<T> result = of(HttpStatusCode.InternalServerError, null);
        result.setMessage(message);
        return result;
    }

}
