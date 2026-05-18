package com.joy.utils;

import com.joy.entity.sysConfig.SysConfigMail;
import com.joy.enums.http.AdminCodeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
public class VerifyCodeUtil {

    private static RedisUtil redis() {
        return BeanUtil.getBean(RedisUtil.class);
    }

    // ==================== 限制参数 ====================
    private static final long EMAIL_INTERVAL = 60;   // 同一邮箱发送间隔（秒）
    private static final long EMAIL_HOUR_MAX = 3;    // 同一邮箱每小时上限
    private static final long EMAIL_DAY_MAX = 10;   // 同一邮箱每天上限
    private static final long IP_MINUTE_MAX = 3;    // 同一IP每分钟上限
    private static final long IP_HOUR_MAX = 5;    // 同一IP每小时上限
    private static final long IP_DAY_MAX = 20;   // 同一IP每天上限
    private static final long MAX_ERROR_COUNT = 3;    // 验证码最大错误次数

    // ==================== Redis Key 前缀 ====================
    private static final String PREFIX_EMAIL_INTERVAL = "vc:vf:email:interval:";
    private static final String PREFIX_EMAIL_HOUR = "vc:vf:email:hour:";
    private static final String PREFIX_EMAIL_DAY = "vc:vf:email:day:";
    private static final String PREFIX_IP_MINUTE = "vc:vf:ip:minute:";
    private static final String PREFIX_IP_HOUR = "vc:vf:ip:hour:";
    private static final String PREFIX_IP_DAY = "vc:vf:ip:day:";
    private static final String PREFIX_ERROR = "vc:vf:error:";

    /**
     * 模板占位符正则
     */
    private static final String PLACEHOLDER_REGEX = "\\{(\\w+)\\}";

    /**
     * 检查是否允许发送验证码
     *
     * @param email 目标邮箱
     * @param ip    客户端IP
     *  表示允许发送，否则返回错误提示
     */
    public static void checkSendAllowed(String email, String ip) {
        RedisUtil redis = redis();

        // ---- 邮箱维度 ----
        // 1. 60秒发送间隔
        String intervalKey = PREFIX_EMAIL_INTERVAL + email;
        if (redis.hasKey(intervalKey)) {
            long ttl = redis.getExpire(intervalKey);
            AdminCodeMessage.VERIFICATION_CODE_SECONDS.throwIt(ttl);
        }

        // 2. 邮箱每小时上限
        String emailHourKey = PREFIX_EMAIL_HOUR + email;
        long emailHourCount = redis.increment(emailHourKey, 1);
        if (emailHourCount == 1) {
            redis.expire(emailHourKey, 3600);
        }
        if (emailHourCount > EMAIL_HOUR_MAX) {//该邮箱获取验证码过于频繁，请1小时后再试
            AdminCodeMessage.VERIFICATION_CODES_FREQUENTLY.throwIt();
        }

        // 3. 邮箱每天上限
        String emailDayKey = PREFIX_EMAIL_DAY + email;
        long emailDayCount = redis.increment(emailDayKey, 1);
        if (emailDayCount == 1) {
            redis.expire(emailDayKey, 86400);
        }
        if (emailDayCount > EMAIL_DAY_MAX) {//该邮箱今日获取验证码次数已达上限，请明天再试
            AdminCodeMessage.MAXIMUM_VERIFICATION_CODES_TODAY.throwIt();
        }

        // ---- IP 维度 ----
        // 4. IP每分钟上限
        String ipMinuteKey = PREFIX_IP_MINUTE + ip;
        long ipMinuteCount = redis.increment(ipMinuteKey, 1);
        if (ipMinuteCount == 1) {
            redis.expire(ipMinuteKey, 60);
        }
        if (ipMinuteCount > IP_MINUTE_MAX) {//当前网络请求过于频繁，请稍后再试
            AdminCodeMessage.NETWORK_REQUESTS_FREQUENT.throwIt();
        }

        // 5. IP每小时上限
        String ipHourKey = PREFIX_IP_HOUR + ip;
        long ipHourCount = redis.increment(ipHourKey, 1);
        if (ipHourCount == 1) {
            redis.expire(ipHourKey, 3600);
        }
        if (ipHourCount > IP_HOUR_MAX) {//当前网络请求过于频繁，请1小时后再试
            AdminCodeMessage.NETWORK_REQUESTS_FREQUENT_HOUR.throwIt();
        }

        // 6. IP每天上限
        String ipDayKey = PREFIX_IP_DAY + ip;
        long ipDayCount = redis.increment(ipDayKey, 1);
        if (ipDayCount == 1) {
            redis.expire(ipDayKey, 86400);
        }
        if (ipDayCount > IP_DAY_MAX) {// 当前网络今日请求次数已达上限，请明天再试
            AdminCodeMessage.NETWORK_REQUESTS_FREQUENT_TODAY.throwIt();
        }

    }

    /**
     * 发送成功后调用：设置发送间隔标记
     */
    public static void markSent(String email) {
        redis().setex(PREFIX_EMAIL_INTERVAL + email, "1", EMAIL_INTERVAL);
    }

    /**
     * 保存验证码到Redis（5分钟有效期）
     */
    public static void saveCode(String email, String prefixCode, String code, Integer validTime) {
        RedisUtil redis = redis();
        redis.setex(prefixCode + email, code, validTime);
        redis.del(PREFIX_ERROR + email); // 重置错误次数
    }

    /**
     * 异步保存验证码（不阻塞主线程）
     */
    public static void saveCodeAsync(String email, String prefixCode, String code, Integer validTime) {
        ThreadPoolTaskExecutor executor;
        try {
            executor = BeanUtil.getBean("verifyCodeExecutor", ThreadPoolTaskExecutor.class); // 拿到 verifyCodeExecutor
        } catch (Exception e) {
            // 万一拿不到线程池，降级为同步保存
            log.warn("获取线程池失败，降级为同步保存验证码", e);
            saveCode(email, prefixCode, code, validTime);
            return;
        }
        executor.execute(() -> {
            try {
                RedisUtil redis = redis();
                redis.setex(prefixCode + email, code, validTime);
                redis.del(PREFIX_ERROR + email);  // 重置错误次数
                log.debug("验证码异步保存成功，邮箱：{}", email);
            } catch (Exception e) {
                log.error("异步保存验证码失败，邮箱：{}", email, e);
            }
        });
    }

    /**
     * 校验验证码
     *
     * @param email 邮箱
     * @param code  用户输入的验证码
     * @return null 表示验证通过，否则返回错误提示
     */
    public static void verifyCode(String email, String prefixCode, String code, Integer validTime) {
        RedisUtil redis = redis();

        // 1. 检查错误次数
        String errorKey = PREFIX_ERROR + email;
        long errorCount = redis.increment(errorKey, 1);
        if (errorCount == 1) {
            redis.expire(errorKey, validTime);
        }
        if (errorCount > MAX_ERROR_COUNT) {
            AdminCodeMessage.MANY_VERIFICATION_ERRORS.throwIt();
        }

        // 2. 检查验证码是否存在
        Object savedObj = redis.get(prefixCode + email);
        if (savedObj == null) {
            AdminCodeMessage.VERIFICATION_CODE_EXPIRED.throwIt();
            return;
        }
        String savedCode = savedObj.toString();

        // 3. 比对验证码
        if (!savedCode.equals(code)) {
            long remaining = MAX_ERROR_COUNT - errorCount;
            AdminCodeMessage.VERIFICATION_CODE_ATTEMPTS.throwIt(remaining);
        }

        // 4. 验证成功，清除验证码和错误计数
        redis.del(prefixCode + email);
        redis.del(PREFIX_ERROR + email);
    }

    /**
     * 检查邮箱是否已发送过验证码
     *
     * @param email 邮箱地址
     * @return 是否已发送
     */
    public static boolean hasSentVerificationCode(String email) {
        RedisUtil redis = redis();
        String redisKey = "verification_code:" + email;
        return redis.hasKey(redisKey);
    }

    /**
     * 获取验证码剩余有效时间
     *
     * @param email 邮箱地址
     * @return 剩余时间（秒）
     */
    public static Long getVerificationCodeTtl(String email) {
        RedisUtil redis = redis();
        String redisKey = "verification_code:" + email;
        return redis.getExpire(redisKey);
    }

    /**
     * 替换模板中的占位符 {key}
     *
     * @param template 模板
     * @param params   需要替换的值
     * @return HTML格式的邮件内容
     */
    public static String replacePlaceholders(String template, Map<String, String> params) {
        String result = template;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }

    /**
     * 将秒数格式化为“X小时Y分钟”的字符串
     *
     * @param totalSeconds 总秒数
     * @return 格式化后的时间字符串，如“1小时30分钟”或“1小时”
     */
    public static String formatSeconds(int totalSeconds) {
        if (totalSeconds < 0) {
            return "秒数不能为负数";
        }
        int hours = totalSeconds / 3600;          // 计算小时数
        int minutes = (totalSeconds % 3600) / 60; // 计算剩余分钟数

        if (hours == 0 && minutes == 0) {
            return "0分钟";
        }
        StringBuilder sb = new StringBuilder();
        if (hours > 0) {
            sb.append(hours).append("小时");
        }
        if (minutes > 0) {
            sb.append(minutes).append("分钟");
        }
        return sb.toString();
    }

    /**
     * 发送验证码邮件
     *
     * @param verificationCode 验证码
     * @param content          发送邮箱
     * @param toAccount        目标邮箱地址
     * @param mail             邮箱配置
     * @return 是否发送成功
     */
    public static boolean sendEmail(String verificationCode, String content, String toAccount, SysConfigMail mail) {

        try {

            // 配置邮箱SMTP服务器
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost(mail.getHost());//设置SMTP服务器地址
            mailSender.setPort(mail.getPort());// 设置SMTP端口
            mailSender.setUsername(mail.getUsername()); // 设置邮箱账号
            mailSender.setPassword(mail.getPassword()); // 设置邮箱密码或授权码

            // 设置邮件属性
            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", mail.getProtocol());// 设置协议类型
            props.put("mail.smtp.auth", mail.getSmtpAuth());// 启用身份验证
            props.put("mail.smtp.ssl.enable", mail.getSmtpSslEnable());// 启用SSL加密
            props.put("mail.smtp.starttls.enable", mail.getSmtpStarttlsEnable());// 禁用TLS加密
            props.put("mail.debug", mail.getDebug()); // 调试模式，生产环境可设为false
            props.put("mail.smtp.connectiontimeout", mail.getConnectionTimeout());  // 设置连接超时时间（毫秒）
            props.put("mail.smtp.timeout", mail.getTimeout());  // 设置读取超时时间（毫秒）
            props.put("mail.smtp.writetimeout", mail.getWriteTimeout());  // 设置写入超时时间（毫秒）
            // 构建邮件消息
            MimeMessagePreparator message = mimeMessage -> {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                helper.setTo(toAccount); //收件人
                helper.setFrom(mail.getUsername()); // 发件人邮箱
                helper.setSubject(mail.getSubject());
                helper.setText(content, true);
            };

            log.info("模板内容======》〉》〉{}", content);
            log.info("邮件验证码======》〉》〉{}", verificationCode);
            // 发送邮件
//            mailSender.send(message);

            return true;
        } catch (RuntimeException e) {
            return false;
//            throw new RuntimeException(e);
        }
    }

    /**
     * @param mail 发送邮箱
     * @param code 验证码
     * @return 替换后的模板
     */
    public static String buildEmailVerifyContent(SysConfigMail mail, String code) {
        // 替换模板占位符
        Map<String, String> params = new ConcurrentHashMap<>();
        params.put("code", code);
        params.put("time", formatSeconds(mail.getValidTime()));
        return replacePlaceholders(mail.getTemplate(), params);
    }


}
