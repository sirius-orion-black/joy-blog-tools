package com.joy.untils;

import com.joy.entity.sysConfig.SysConfigMail;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import java.util.Properties;
import java.util.concurrent.TimeUnit;


public class VerifyCodeUtil {

    /**
     * 发送验证码邮件
     *
     * @param toAccount     目标邮箱地址
     * @param redisTemplate
     * @return 是否发送成功
     */
    public static boolean sendVerificationCode(String account, String toAccount, Long validTime, SysConfigMail mail, RedisTemplate<String, String> redisTemplate) {

        try {
            // 生成验证码
            String verificationCode = RandomStringUtils.randomAlphanumeric(6);
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
                helper.setTo(toAccount);
                helper.setFrom(account); // 发件人邮箱
                helper.setSubject("邮箱验证码");
                helper.setText(buildEmailContent(verificationCode,validTime), true);
            };
            // 发送邮件
            mailSender.send(message);

            // 将验证码存储到Redis，设置5分钟过期时间
            String redisKey = "verification_code:" + toAccount;
            redisTemplate.execute((RedisCallback<Void>) connection -> {
                connection.setEx(redisKey.getBytes(), validTime, verificationCode.getBytes());
                return null;
            });
            return true;
        } catch (RuntimeException e) {
            return false;
//            throw new RuntimeException(e);
        }
    }

    /**
     * 构建邮件内容HTML模板
     * @param code 验证码
     * @return HTML格式的邮件内容
     */
    public static String buildEmailContent(String code,Long validTime) {
        return "<div style=\"width: 520px\">" +
                "<div style=\"margin: 0;background-color: rgb(81, 76, 74);color: rgb(255, 255, 255);border-top-left-radius: 5px;border-top-right-radius: 5px;text-align: center;font-size: 18px;font-weight: bold;line-height: 52px;\">" +
                "徐徐乐之验证码" +
                "</div>" +
                "<div style=\"margin: 0; border: 1px solid #aba29b; padding: 20px; font-size: 14px; color: #393939\">" +
                "<div style=\"padding: 10px 0\">" +
                "尊敬的徐徐乐之用户：您好！您正在进行的操作需要验证邮箱，请输入以下验证码：" +
                "</div>" +
                "<div style=\"text-align: center;font-size: 32px;font-weight: bold;line-height: 62px;color: #000000;\">" +
                code +
                "</div>" +
                "<div style=\"padding: 10px 0; color: #000000\">验证码有效期为<span style=\"font-size: 18px;font-weight: bold;padding: 0 5px;\">" + (validTime / 60 / 60) + "</span>小时</div>" +
                "<div style=\"padding: 10px 0\">" +
                "如果您并未请求此验证码，则可能是他人正在尝试访问您的账户。请勿将此验证码转发给或提供给任何人。" +
                "</div>" +
                "<div style=\"padding: 10px 0\">徐徐乐之 账号团队敬上</div>" +
                "</div>" +
                "</div>";
    }
    /**
     * 验证邮箱验证码
     * @param email 邮箱地址
     * @param inputCode 用户输入的验证码
     * @return 验证结果
     */
    public static boolean verifyCode(String email, String inputCode,RedisTemplate<String, String> redisTemplate) {
        String redisKey = "verification_code:" + email;
        String cachedCode = redisTemplate.opsForValue().get(redisKey);

        // 检查验证码是否存在
        if (cachedCode == null) {
            return false;
        }

        // 比较验证码
        boolean isValid = cachedCode.equals(inputCode);

        // 无论验证成功与否，都删除Redis中的验证码
        redisTemplate.delete(redisKey);

        return isValid;
    }
    /**
     * 检查邮箱是否已发送过验证码
     * @param email 邮箱地址
     * @return 是否已发送
     */
    public static boolean hasSentVerificationCode(String email,RedisTemplate<String, String> redisTemplate) {
        String redisKey = "verification_code:" + email;
        return redisTemplate.hasKey(redisKey);
    }
    /**
     * 获取验证码剩余有效时间
     * @param email 邮箱地址
     * @return 剩余时间（秒）
     */
    public static Long getVerificationCodeTtl(String email,RedisTemplate<String, String> redisTemplate) {
        String redisKey = "verification_code:" + email;
        return redisTemplate.getExpire(redisKey, TimeUnit.SECONDS);
    }

}
