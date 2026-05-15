package com.joy.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.joy.dto.stat.StatTrackDTO;
import com.joy.entity.stat.StatAccessLog;
import com.joy.entity.stat.StatDailyTraffic;
import com.joy.mapper.stat.StatAccessLogMapper;
import com.joy.mapper.stat.StatDailyTrafficMapper;
import com.joy.service.StatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class StatServiceImpl implements StatService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private StatDailyTrafficMapper trafficMapper;

    @Autowired
    private StatAccessLogMapper accessLogMapper;

    // 常量
    private static final String DIM_GLOBAL = "global";
    private static final String DIM_PAGE_PREFIX = "page:";
    //private static final String DIM_ARTICLE_PREFIX = "article:";

    // Redis Key 前缀
    private static final String KEY_PREFIX_PV = "stat:pv:";
    private static final String KEY_PREFIX_UV = "stat:uv:";
    private static final String KEY_PREFIX_IP = "stat:ips:";
    private static final String LOG_QUEUE_KEY = "stat:log:queue";

    /**
     * 统一入口：记录访问并触发所有相关维度的统计
     * @param ip 用户IP
     * @param stat 设备唯一标识
     * @param extra 额外维度参数 (如 articleId)
     */
    public void recordAccess(String ip, StatTrackDTO stat, Map<String, String> extra) {
        String deviceId =stat.getDeviceId();
        String path=stat.getPath();
        String terminalType=stat.getTerminalType();
        String userAgent = stat.getUserAgent();
        if (path == null || path.isEmpty()) path = "/";
        String today = LocalDate.now().toString();

        // 1. 构建需要统计的列表
        List<String> dimensions = new ArrayList<>();

        // 全站统计
        dimensions.add(DIM_GLOBAL);

        // 页面统计
        dimensions.add(DIM_PAGE_PREFIX + path);

        // 动态业务统计 (根据 extra 参数添加,可根据需求继续扩展)
//        if (extra != null) {
//            if (StringUtils.hasText(extra.get("articleId"))) {
//                dimensions.add(DIM_ARTICLE_PREFIX + extra.get("articleId"));
//            }
//        }

        // 2. 更新所有 Redis 统计,其实现在没必要循环的，只是先写好来，以后或许会多呢
        for (String dimension : dimensions) {
            updateStats(today, dimension, ip, deviceId);
        }

        // 3. 缓冲明细日志到 Redis List
        bufferAccessLogToRedis(ip, deviceId, path, terminalType, userAgent);
    }

    /**
     * 通用统计更新方法
     * @param date 日期 yyyy-MM-dd
     * @param dimension 标识 (如: global, page:/home)
     * @param ip 用户IP
     * @param deviceId 设备ID
     */
    private void updateStats(String date, String dimension, String ip, String deviceId) {
        String pvKey = KEY_PREFIX_PV + date + ":" + dimension;
        String uvKey = KEY_PREFIX_UV + date + ":" + dimension;
        String ipKey = KEY_PREFIX_IP + date + ":" + dimension;

        try {
            // PV: 简单计数
            redisTemplate.opsForValue().increment(pvKey);

            // UV: HyperLogLog 去重计数
            redisTemplate.opsForHyperLogLog().add(uvKey, deviceId);

            // IP: Set 去重计数
            redisTemplate.opsForSet().add(ipKey, ip);

            // 设置过期时间 (3天)，防止 Redis 无限膨胀
            long expireSeconds = 3 * 24 * 3600;
            redisTemplate.expire(pvKey, expireSeconds, TimeUnit.SECONDS);
            redisTemplate.expire(uvKey, expireSeconds, TimeUnit.SECONDS);
            redisTemplate.expire(ipKey, expireSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Redis统计写入失败, dimension: {}", dimension, e);
        }
    }

    /**
     * 将访问日志推入 Redis List
     */
    private void bufferAccessLogToRedis(String ip, String deviceId, String path, String terminalType,String userAgent) {
        try {
            StatAccessLog logInfo = new StatAccessLog();
            logInfo.setRequestIp(ip);
            logInfo.setDeviceId(deviceId);
            logInfo.setPagePath(path);
            logInfo.setTerminalType(terminalType);
            logInfo.setAccessTime(LocalDateTime.now());
            logInfo.setUserAgent(userAgent);

            String jsonLog = JSON.toJSONString(logInfo);
            redisTemplate.opsForList().leftPush(LOG_QUEUE_KEY, jsonLog);
        } catch (Exception e) {
            log.warn("访问日志缓冲到Redis失败: {}", e.getMessage());
        }
    }

    /**
     * 定时任务：每6小时执行一次
     * 同步 PV/UV/IP 汇总数据
     * 消费 Redis List 中的日志明细，批量入库
     */
    @Scheduled(cron = "0 0 0/6 * * ?")
    @Transactional
    public void syncStats() {
        // 同步汇总统计数据 (SCAN方式)
        syncSummaryStats();

        // 消费日志队列，批量入库
        syncAccessLogs();
    }

    /**
     * SCAN 遍历 Redis Key，汇总数据到 MySQL
     */
    private void syncSummaryStats() {
        String today = LocalDate.now().toString();
        // 匹配所有今日统计 Key: stat:pv:2026-05-09:*
        String pattern = KEY_PREFIX_PV + today + ":*";

        ScanOptions options = ScanOptions.scanOptions()
                .match(pattern)
                .count(100)
                .build();

        try (Cursor<String> cursor = redisTemplate.scan(options)) {
            while (cursor.hasNext()) {
                String pvKey = cursor.next();
                try {
                    // 提取维度标识: 从 key 中去除前缀 stat:pv:yyyy-MM-dd:
                    String prefix = KEY_PREFIX_PV + today + ":";
                    String dimension = pvKey.substring(prefix.length());

                    // 获取各项指标
                    Long pv = redisTemplate.opsForValue().increment(pvKey, 0); // 读取不增加
                    String uvKey = KEY_PREFIX_UV + today + ":" + dimension;
                    Long uv = redisTemplate.opsForHyperLogLog().size(uvKey);
                    String ipKey = KEY_PREFIX_IP + today + ":" + dimension;
                    Long ipCount = redisTemplate.opsForSet().size(ipKey);

                    StatDailyTraffic daily = new StatDailyTraffic();
                    daily.setStatDate(LocalDate.now());
                    daily.setPagePath(dimension); // 存储维度标识
                    daily.setPv(pv);
                    daily.setUv(uv);
                    // assert ipCount != null;
                    // 记录日志或给个默认值
                    if (ipCount == null) {
                        ipCount = 0L;
                    }
                    daily.setIpCount(ipCount.intValue());

                    statSave(daily);
                } catch (Exception e) {
                    log.error("同步汇总数据失败, key: {}", pvKey, e);
                }
            }
        } catch (Exception e) {
            log.error("Redis SCAN 操作异常", e);
        }
    }

    /**
     * 从 Redis List 批量读取日志并入库
     * RPOP 弹出即释放空间
     */
    private void syncAccessLogs() {
        List<StatAccessLog> batchLogs = new ArrayList<>();
        int batchSize = 100;

        try {
            // 循环从右侧弹出数据 (RPOP)，取够 batchSize 或队列为空
            for (int i = 0; i < batchSize; i++) {
                String jsonLog = redisTemplate.opsForList().rightPop(LOG_QUEUE_KEY);
                if (jsonLog == null) {
                    break; // 队列已空
                }

                try {
                    StatAccessLog logEntity = JSON.parseObject(jsonLog, StatAccessLog.class);
                    batchLogs.add(logEntity);
                } catch (Exception e) {
                    log.error("解析日志JSON失败: {}", jsonLog, e);
                }
            }

            // 批量插入数据库
            if (!batchLogs.isEmpty()) {
                for (StatAccessLog log : batchLogs) {
                    accessLogMapper.insert(log);
                }
                log.info("成功同步 {} 条访问日志到数据库", batchLogs.size());
            }
        } catch (Exception e) {
            log.error("同步访问日志队列失败", e);
        }
    }

    /**
     * 保存或更新每日流量统计
     */
    private void statSave(StatDailyTraffic entity) {
        LambdaQueryWrapper<StatDailyTraffic> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StatDailyTraffic::getStatDate, entity.getStatDate())
                .eq(StatDailyTraffic::getPagePath, entity.getPagePath());

        StatDailyTraffic existing = trafficMapper.selectOne(wrapper);
        if (existing != null) {
            existing.setPv(entity.getPv());
            existing.setUv(entity.getUv());
            existing.setIpCount(entity.getIpCount());
            trafficMapper.updateById(existing);
        } else {
            trafficMapper.insert(entity);
        }
    }
}
