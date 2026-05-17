package com.joy.config.aop;

import com.joy.common.Result;
import com.joy.config.annotation.RateLimiter;
import com.joy.enums.http.GatewayCodeMessage;
import com.joy.utils.IpRegionUtil;
import com.joy.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class RateLimiterAspect {

    private final RedisUtil redisUtil;

    public RateLimiterAspect(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Around("@annotation(com.joy.config.annotation.RateLimiter)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RateLimiter rateLimiter = method.getAnnotation(RateLimiter.class);

        String key = buildKey(rateLimiter);
        long currentCount = redisUtil.increment(key, 1);

        // 第一次访问，设置过期时间
        if (currentCount == 1) {
            redisUtil.expire(key, rateLimiter.timeUnit().toSeconds(rateLimiter.time()));
        }

        if (currentCount > rateLimiter.maxCount()) {
            log.warn("触发接口限流，key: {}, 当前次数: {}, {}", key, currentCount,rateLimiter.message());
            GatewayCodeMessage.MANY_REQUESTS.throwIt();
//            return Result.forbidden(rateLimiter.message());
        }

        return joinPoint.proceed();
    }

    /**
     * 构建限流key：前缀 + IP地址
     */
    private String buildKey(RateLimiter rateLimiter) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String ip = "unknown";
        String uri = "";
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            ip = IpRegionUtil.getClientIpAddress(request);
            uri = request.getRequestURI();
        }
        return rateLimiter.key() + ":" + uri + ":" + ip;
    }
}

