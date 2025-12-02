package com.joy.config.Interceptor;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
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
    public Map<String, Object> handleNotLoginException(NotLoginException e) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 401);
        result.put("message", "未登录或登录已过期");
        return result;
    }

    /**
     * 处理无权限异常
     * @param e 异常对象
     * @return 错误响应
     */
    @ExceptionHandler(NotPermissionException.class)
    public Map<String, Object> handleNotPermissionException(NotPermissionException e) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 403);
        result.put("message", "无权限访问：" + e.getPermission());
        return result;
    }

    /**
     * 处理无角色异常
     * @param e 异常对象
     * @return 错误响应
     */
    @ExceptionHandler(NotRoleException.class)
    public Map<String, Object> handleNotRoleException(NotRoleException e) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 403);
        result.put("message", "无角色权限：" + e.getRole());
        return result;
    }

}
