package com.joy.common;

import com.joy.enums.result.HttpStatusCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class Result<T> {

    @ApiModelProperty(value="状态码")
    private Integer code;

    @ApiModelProperty(value = "消息")
    private String message;

    @ApiModelProperty(value = "数据")
    private T data;

    /**
     * 请求成功
     * @param data
     * @return
     * @param <T>
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        HttpStatusCode success = HttpStatusCode.Success;
        result.setCode(success.getCode());
        result.setMessage(success.getMessage());
        result.setData(data);
        return result;
    }

    /**
     * 请求成功
     * @return
     * @param <T>
     */
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        HttpStatusCode success = HttpStatusCode.Success;
        result.setCode(success.getCode());
        result.setMessage(success.getMessage());
        result.setData(null);
        return result;
    }

    /**
     * 请求成功并创建了新资源
     * @return
     * @param <T>
     */
    public static <T> Result<T> created() {
        Result<T> result = new Result<>();
        HttpStatusCode created = HttpStatusCode.Created;
        result.setCode(created.getCode());
        result.setMessage(created.getMessage());
        result.setData(null);
        return result;
    }

    /**
     * 服务器无法理解请求格式
     * @return
     * @param <T>
     */
    public static <T> Result<T> badRequest() {
        Result<T> result = new Result<>();
        HttpStatusCode badRequest = HttpStatusCode.BadRequest;
        result.setCode(badRequest.getCode());
        result.setMessage(badRequest.getMessage());
        result.setData(null);
        return result;
    }


    /**
     * 请求需要用户验证
     * @return
     * @param <T>
     */
    public static <T> Result<T> unauthorized() {
        Result<T> result = new Result<>();
        HttpStatusCode unauthorized = HttpStatusCode.Unauthorized;
        result.setCode(unauthorized.getCode());
        result.setMessage(unauthorized.getMessage());
        result.setData(null);
        return result;
    }

    /**
     * 服务器拒绝请求
     * @return
     * @param <T>
     */
    public static <T> Result<T> forbidden() {
        Result<T> result = new Result<>();
        HttpStatusCode forbidden = HttpStatusCode.Forbidden;
        result.setCode(forbidden.getCode());
        result.setMessage(forbidden.getMessage());
        result.setData(null);
        return result;
    }

    /**
     * 请求的资源不存在
     * @return
     * @param <T>
     */
    public static <T> Result<T> notFound() {
        Result<T> result = new Result<>();
        HttpStatusCode notFound = HttpStatusCode.NotFound;
        result.setCode(notFound.getCode());
        result.setMessage(notFound.getMessage());
        result.setData(null);
        return result;
    }

    /**
     * 服务器异常
     * @return
     * @param <T>
     */
    public static <T> Result<T> internalServerError() {
        Result<T> result = new Result<>();
        HttpStatusCode internalServerError = HttpStatusCode.InternalServerError;
        result.setCode(internalServerError.getCode());
        result.setMessage(internalServerError.getMessage());
        result.setData(null);
        return result;
    }




}
