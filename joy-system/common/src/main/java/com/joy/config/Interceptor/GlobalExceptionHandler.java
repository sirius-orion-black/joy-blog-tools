package com.joy.config.Interceptor;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.joy.common.Result;
import com.joy.enums.http.UnauthorizedCodeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 全局异常处理器
 * 捕获Sa-Token相关异常并统一返回格式
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理未登录异常
     */
    @ExceptionHandler(NotLoginException.class)
    public void handleNotLoginException(NotLoginException e) {
        // 根据 Sa-Token 提供的异常类型，精准抛出对应的枚举异常
        if (NotLoginException.TOKEN_TIMEOUT.equals(e.getType())) {
            UnauthorizedCodeMessage.TOKEN_EXPIRED.throwIt();
        } else if (NotLoginException.BE_REPLACED.equals(e.getType()) ||
                NotLoginException.KICK_OUT.equals(e.getType())) {
            UnauthorizedCodeMessage.TOKEN_KICKED.throwIt();
        } else if (NotLoginException.INVALID_TOKEN.equals(e.getType())) {
            UnauthorizedCodeMessage.TOKEN_INVALID.throwIt();
        } else {
            // 兜底其他未登录情况
            UnauthorizedCodeMessage.NOT_LOGIN.throwIt();
        }
    }

    /**
     * 处理无权限异常
     */
    @ExceptionHandler(NotPermissionException.class)
    public void handleNotPermissionException(NotPermissionException e) {
        // 缺权限：直接抛出带具体权限码的异常
        UnauthorizedCodeMessage.UNAUTHORIZED_ACCESS.throwIt(e.getPermission());
    }

    /**
     * 处理无角色异常
     */
    @ExceptionHandler(NotRoleException.class)
    public void handleNotRoleException(NotRoleException e) {
        // 缺角色：直接抛出带具体角色标识的异常
        UnauthorizedCodeMessage.UNAUTHORIZED_ROLE_ACCESS.throwIt(e.getRole());
    }

    /**
     * 处理自定义业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<Object>> handleBusinessException(BusinessException e) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(Result.error(e.getHttpStatus(), e.getMessage(), e.getData()));
    }

    /**
     * 兜底处理系统未知异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> handleException(Exception e) {
        log.error("系统发生未知异常：", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.fail(HttpStatus.INTERNAL_SERVER_ERROR, "internal_server_error"));
    }
}
