package com.joy.config.Interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joy.common.Result;
import com.joy.enums.http.GatewayCodeMessage;
import com.joy.utils.IpRegionUtil;
import com.joy.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class HeadInterceptor implements HandlerInterceptor {

    private final RedisUtil redisUtil;
    public HeadInterceptor(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    private static final String PREFIX = "global:rate_limit:";
    private static final long WINDOW_SECONDS = 10;   // 时间 10秒
    private static final long MAX_REQUESTS = 3;      // 最大请求次数

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        // 获取请求头时间戳
        String timestampStr = request.getHeader("X-Timestamp");
        if (StringUtils.isEmpty(timestampStr)) {
            log.info("缺少时间戳");
            GatewayCodeMessage.LACK_TIMESTAMP.throwIt();
            return false;// 拦截请求
        }
        long timestamp = Long.parseLong(timestampStr);
        // 获取当前时间戳，其实用不上，有点不干人事
        long now = System.currentTimeMillis();
        // 验证时间戳有效性（允许1分钟误差）
        if (Math.abs(now - timestamp) > 60000) {
            log.info("时间戳无效");
            GatewayCodeMessage.INVALID_TIMESTAMP.throwIt();
            return false;
        }
        // 全局 API 限流，同一 IP + 同一接口 10秒内最多请求 3 次
        String ip = IpRegionUtil.getClientIpAddress(request);
        String uri = request.getRequestURI();  // 拿到请求路径
        String key = PREFIX + ip + ":" + uri;  // 按 IP + 接口 独立限流
        long count = redisUtil.increment(key, 1);
        if (count == 1) {
            redisUtil.expire(key, WINDOW_SECONDS);
        }
        if (count > MAX_REQUESTS) {
            log.warn("触发全局限流，key:{}, IP: {}, URI: {}, 当前次数: {}", key, ip, uri, count);
            GatewayCodeMessage.MANY_REQUESTS.throwIt();
            return false;
        }


        return true; // 放行请求
    }

}
