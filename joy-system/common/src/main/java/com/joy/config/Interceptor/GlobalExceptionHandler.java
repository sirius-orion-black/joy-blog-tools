package com.joy.config.Interceptor;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.joy.common.Result;
import com.joy.enums.http.UnauthorizedCodeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;


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
                .body(Result.error(HttpStatus.NOT_ACCEPTABLE, e.getMessage(), e.getData()));
    }
    /**
     * 兜底处理系统未知异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> handleException(Exception e) {
        log.error("系统发生未知异常：{}", e.getClass().getName(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.fail(HttpStatus.INTERNAL_SERVER_ERROR, "internal_server_error"));
    }

    /**
     * 处理表单绑定/无@RequestBody时的校验失败
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Result<Void>> handleBindException(BindException e) {
        String message = e.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("参数校验失败");
        log.warn("参数绑定校验失败: {}", message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.fail(HttpStatus.INTERNAL_SERVER_ERROR, "information_incomplete"));
    }

    /**
     *
     * @param e RequestBody 校验失败
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("参数校验失败");
        log.warn("参数校验失败: {}", message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.fail(HttpStatus.INTERNAL_SERVER_ERROR, "information_incomplete"));
    }

    /**
     *
     * @param e @RequestParam / @PathVariable 校验失败
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Result<Void>> handleConstraintViolation(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse("参数校验失败");
        log.warn("约束校验失败: {}", message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.fail(HttpStatus.INTERNAL_SERVER_ERROR, "information_incomplete"));
    }
}
