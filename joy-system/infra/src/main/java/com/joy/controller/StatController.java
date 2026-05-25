package com.joy.controller;

import com.joy.config.apiPrefix.ApiPrefixInfraRestController;
import com.joy.dto.stat.StatTrackDTO;
import com.joy.service.StatService;
import com.joy.utils.DeviceInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

import static com.joy.utils.IpRegionUtil.getClientIpAddress;

@ApiPrefixInfraRestController
@RequestMapping("/stat")
@Slf4j
public class StatController {

    @Autowired
    private StatService statService;

    // 使用 @Qualifier 指定注入 statExecutor 线程池
    @Autowired
    @Qualifier("statExecutor")
    private ThreadPoolTaskExecutor statExecutor;

    /**
     * @param stat    dto
     * @param request http request
     * @return 给前端返回的数据
     */
    @PostMapping("/track")
    public ResponseEntity<Void> track(@RequestBody StatTrackDTO stat, HttpServletRequest request) {
        // 1. 获取真实 IP
        String ip = getClientIpAddress(request);

        Map<String, String> extra = new HashMap<>();

        // 2. 设备基础信息
        if (StringUtils.isBlank(stat.getUserAgent())) {
            String userAgent = request.getHeader("User-Agent");
            String osName = DeviceInfoUtil.getOsName(userAgent);
            String browserName = DeviceInfoUtil.getBrowserName(userAgent);
            boolean isMobile = DeviceInfoUtil.isMobile(userAgent);
            String deviceType = isMobile ? "Mobile" : "PC";
            stat.setUserAgent(osName + ";" + browserName + ";" + deviceType);
        }

        // 3. 异步处理，立即返回，不阻塞用户
        statExecutor.execute(() -> {
            try {
                stat.setDeviceId(request.getHeader("X-Device-Id"));
                statService.recordAccess(ip, stat, extra);
            } catch (Exception e) {
                log.error("统计上报异步处理异常", e);
            }
        });

        return ResponseEntity.ok().build();
    }
}
