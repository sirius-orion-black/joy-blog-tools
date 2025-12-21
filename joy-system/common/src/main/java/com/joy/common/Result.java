package com.joy.common;

import com.joy.enums.common.HttpStatusCode;
import com.joy.untils.EnhancedEmptyObjectUtil;
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
        this.data = data == null ? (T) EnhancedEmptyObjectUtil.getInstance() : data;
    }


    public void setMessage(String message) {
        if(message != null && !message.isEmpty())
            this.message = message;
    }

    public static <T> Result<T> of(HttpStatusCode status) {
        return new Result<>(status, null);
    }

    public static <T> Result<T> of(HttpStatusCode status, T data) {
        return new Result<>(status, data);
    }

    public static <T> Result<T> of(HttpStatusCode status, String message) {
        Result<T> result = new Result<>(status, null);
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> of(HttpStatusCode status, T data, String message) {
        Result<T> result = new Result<>(status, data);
        result.setMessage(message);
        return result;
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
     * 请求成功 只传消息
     * @param message
     * @return
     * @param <T>
     */
    public static <T> Result<T> success(String message) {
        return of(HttpStatusCode.Success, message);
    }

    /**
     * 请求成功 传数据和消息
     * @param data
     * @param message
     * @return
     * @param <T>
     */
    public static <T> Result<T> success(T data, String message) {
        return of(HttpStatusCode.Success, data, message);
    }

    /**
     * 请求成功并创建了新资源
     * @return
     * @param <T>
     */
    public static <T> Result<T> created() {
        return of(HttpStatusCode.Created);
    }

    /**
     * 服务器无法理解请求格式
     * @return
     * @param <T>
     */
    public static <T> Result<T> badRequest() {
        return of(HttpStatusCode.BadRequest);
    }

    /**
     * 服务器无法理解请求格式 只传消息
     * @return
     * @param <T>
     */
    public static <T> Result<T> badRequest(String message) {
        return of(HttpStatusCode.BadRequest,message);
    }
    /**
     * 服务器无法理解请求格式 传数据和消息
     * @return
     * @param <T>
     */
    public static <T> Result<T> badRequest(T data, String message) {
        return of(HttpStatusCode.BadRequest,data, message);
    }

    /**
     * 请求需要用户验证
     * @return
     * @param <T>
     */
    public static <T> Result<T> unauthorized() {
        return of(HttpStatusCode.Unauthorized);
    }

    /**
     * 请求需要用户验证 只传消息
     * @param message
     * @return
     * @param <T>
     */
    public static <T> Result<T> unauthorized(String message) {
        return of(HttpStatusCode.Unauthorized, message);
    }

    /**
     * 服务器拒绝请求
     * @return
     * @param <T>
     */
    public static <T> Result<T> forbidden() {
        return of(HttpStatusCode.Forbidden);
    }

    /**
     * 服务器拒绝请求 只传消息
     * @param message
     * @return
     * @param <T>
     */
    public static <T> Result<T> forbidden(String message) {
        return of(HttpStatusCode.Forbidden, message);
    }

    /**
     * 请求的资源不存在
     * @return
     * @param <T>
     */
    public static <T> Result<T> notFound() {
        return of(HttpStatusCode.NotFound);
    }

    /**
     * 服务器异常
     * @return
     * @param <T>
     */

    public static <T> Result<T> internalServerError() {
        return of(HttpStatusCode.InternalServerError);
    }

    /**
     * 服务器异常 只传消息
     * @param message
     * @return
     * @param <T>
     */
    public static <T> Result<T> internalServerError(String message) {
        return of(HttpStatusCode.InternalServerError, message);
    }

}
