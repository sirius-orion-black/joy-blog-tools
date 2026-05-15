package com.joy.config.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {

    /** 限流key前缀 */
    String key() default "rate_limit";

    /** 时间窗口内允许的最大请求数 */
    long maxCount() default 10;

    /** 时间窗口 */
    long time() default 60;

    /** 时间单位 */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /** 限流提示信息 */
    String message() default "操作过于频繁，请稍后再试";
}
