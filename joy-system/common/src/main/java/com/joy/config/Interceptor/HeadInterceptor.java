package com.joy.config.Interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class HeadInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头时间戳
        String timestampStr = request.getHeader("X-Timestamp");
        log.info("======X-Timestamp>>>>{}",timestampStr);

        if (StringUtils.isEmpty(timestampStr)) {
            throw new RuntimeException("缺少时间戳");
        }
        long timestamp = Long.parseLong(timestampStr);

        // 获取当前时间戳（秒级）
        long now = System.currentTimeMillis() / 1000;

        // 验证时间戳有效性（允许5分钟误差）
//        if (Math.abs(now - timestamp) > 300) {
//            throw new RuntimeException("时间戳无效");
//        }

        return true; // 放行请求
    }

}
