package com.joy.config.Interceptor;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.joy.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 * 捕获Sa-Token相关异常并统一返回格式
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理未登录异常
     * @param e 异常对象
     * @return 错误响应
     */
    @ExceptionHandler(NotLoginException.class)
    public <T> Result<T> handleNotLoginException(NotLoginException e) {
        return Result.unauthorized();
    }

    /**
     * 处理无权限异常
     * @param e 异常对象
     * @return 错误响应
     */
    @ExceptionHandler(NotPermissionException.class)
    public <T> Result<T> handleNotPermissionException(NotPermissionException e) {
        return Result.unauthorized("unauthorized_access");//无权限访问
    }

    /**
     * 处理无角色异常
     * @param e 异常对象
     * @return 错误响应
     */
    @ExceptionHandler(NotRoleException.class)
    public <T> Result<T> handleNotRoleException(NotRoleException e) {
        return Result.unauthorized("no_role_permissions");//无角色权限
    }

}
