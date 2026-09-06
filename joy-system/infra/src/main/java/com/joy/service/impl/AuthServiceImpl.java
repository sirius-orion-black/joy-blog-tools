package com.joy.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joy.common.Result;
import com.joy.dto.auth.EmailVerifyDto;
import com.joy.entity.sysConfig.SysCloudMail;
import com.joy.entity.sysConfig.SysConfig;
import com.joy.entity.sysConfig.SysConfigMail;
import com.joy.entity.user.User;
import com.joy.enums.http.RequestCodeMessage;
import com.joy.mapper.sysConfig.SysCloudMailMapper;
import com.joy.mapper.sysConfig.SysConfigMailMapper;
import com.joy.mapper.sysConfig.SysConfigMapper;
import com.joy.mapper.user.UserMapper;
import com.joy.service.AuthService;
import com.joy.utils.IpRegionUtil;
import com.joy.utils.UserVerifyUtil;
import com.joy.utils.VerifyCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
public class AuthServiceImpl extends ServiceImpl<UserMapper, User> implements AuthService {

    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Autowired
    private SysConfigMailMapper configMailMapper;

    @Autowired
    private SysCloudMailMapper cloudMailMapper;

    //邮箱配置
    private volatile SysConfig cachedConfig;
    //阿里云邮箱
    private volatile SysCloudMail cachedCloudMail;
    private volatile Long cachedCloudMailId = null;
    //139邮箱
    private volatile SysConfigMail cachedConfigMail;
    private volatile Long cachedConfigMailId = null;

    private volatile long cacheExpireTime = 0;
    private static final long CACHE_MS = TimeUnit.MINUTES.toMillis(30);
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * 获取是否启用云邮箱配置
     */
    public SysConfig getMailConfig() {
        if (cachedConfig != null && System.currentTimeMillis() < cacheExpireTime) {
            return cachedConfig;
        }
        lock.lock();
        try {
            if (cachedConfig != null && System.currentTimeMillis() < cacheExpireTime) {
                return cachedConfig;
            }
            SysConfig config = sysConfigMapper.selectOne(
                    new LambdaQueryWrapper<SysConfig>()
                            .eq(SysConfig::getConfigKey, "is_cloud_email")
                            .last("LIMIT 1"));
            if (config == null) {
                RequestCodeMessage.ENABLE_CLOUD_EMAIL_CONFIG.throwIt();
                return  null;
            }

            cachedConfig = config;
            cacheExpireTime = System.currentTimeMillis() + CACHE_MS;
            return cachedConfig;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取阿里云邮箱的配置
     */
    public SysCloudMail getCloudMail() {
        if (cachedCloudMail != null && System.currentTimeMillis() < cacheExpireTime) {
            return cachedCloudMail;
        }
        lock.lock();
        try {
            if (cachedCloudMail != null && System.currentTimeMillis() < cacheExpireTime) {
                return cachedCloudMail;
            }
            SysCloudMail mail = cloudMailMapper.selectOne(
                    new LambdaQueryWrapper<SysCloudMail>()
                            .eq(SysCloudMail::getState, 1)
                            .last("LIMIT 1"));
            if (mail == null) {
                log.info("============>>>>>>>>>阿里云邮箱未配");
                RequestCodeMessage.EMAIL_NOT_CONFIG.throwIt();
                return null;
            }
            cachedCloudMail = mail;
            cachedCloudMailId = mail.getId();
            cacheExpireTime = System.currentTimeMillis() + CACHE_MS;
            return cachedCloudMail;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取139邮箱的配置
     */
    public SysConfigMail getConfigMail() {
        if (cachedConfigMail != null && System.currentTimeMillis() < cacheExpireTime) {
            return cachedConfigMail;
        }
        lock.lock();
        try {
            if (cachedConfigMail != null && System.currentTimeMillis() < cacheExpireTime) {
                return cachedConfigMail;
            }
            SysConfigMail mail = configMailMapper.selectOne(
                    new LambdaQueryWrapper<SysConfigMail>()
                            .eq(SysConfigMail::getState, 1)
                            .last("LIMIT 1"));
            if (mail == null) {
                log.info("============>>>>>>>>>139邮箱未配");
                RequestCodeMessage.EMAIL_NOT_CONFIG.throwIt();
                return null;
            }
            cachedConfigMail = mail;
            cachedConfigMailId = mail.getId();
            cacheExpireTime = System.currentTimeMillis() + CACHE_MS;
            return cachedConfigMail;
        } finally {
            lock.unlock();
        }
    }

    /**
     *
     * @param loginInfo 登录信息
     * @param key redis key
     */
    @Override
    public Result<Map<String,Integer>> emailInfo(EmailVerifyDto loginInfo, String key) throws Exception {
        //获取是否启用云邮箱配置
        SysConfig cloudEmail = this.getMailConfig();
        // 生成验证码
        String verificationCode = RandomStringUtils.randomAlphanumeric(6);
        boolean bl;
        Integer validTime;
        if (cloudEmail.getConfigValue().equals("yes")) {
            //云邮箱我这里用了阿里云邮箱推送
            SysCloudMail mail = this.getCloudMail();
            //发送邮件
            bl = VerifyCodeUtil.sendCloudEmail(verificationCode,loginInfo.getEmail(),mail);
            validTime = mail.getValidTime();
        } else {
            //获取配置邮箱，这里我用的是139邮箱
            SysConfigMail mail = this.getConfigMail();
            //发送邮件
            bl = VerifyCodeUtil.sendEmail(
                    verificationCode,
                    VerifyCodeUtil.buildEmailVerifyContent(mail, verificationCode),
                    loginInfo.getEmail(),
                    mail);
            validTime = mail.getValidTime();
        }
        String message = RequestCodeMessage.VERIFICATION_CODE_SENT_AGAIN.getMessage();
        if (bl) {
            message = RequestCodeMessage.VERIFICATION_CODE_SENT.getMessage();
            VerifyCodeUtil.markSent(loginInfo.getEmail());
            VerifyCodeUtil.saveCodeAsync(loginInfo.getEmail(), key, verificationCode, validTime);
        }
        Map<String,Integer> result = new HashMap<>();
        result.put("validTime",validTime);
        return Result.success(result,message);
    }
    /**
     * 后台管理人员邮箱验证
     *
     * @param loginInfo
     * @param request
     * @return
     */
    @Override
    public Result<Map<String,Integer>> emailCode(EmailVerifyDto loginInfo, HttpServletRequest request) throws Exception {
        if (StringUtils.isBlank(loginInfo.getUsername())) {//判断用户名
            RequestCodeMessage.USERNAME_CANNOT_EMPTY.throwIt();
        }
        if (StringUtils.isBlank(loginInfo.getEmail()) || !UserVerifyUtil.emailFormat(loginInfo.getEmail())) {//判断邮箱
            RequestCodeMessage.EMAIL_FORMAT_INCORRECT.throwIt();
        }
        String ip = IpRegionUtil.getClientIpAddress(request);

        //查验邮箱发送速率，并标记
        VerifyCodeUtil.checkSendAllowed(loginInfo.getEmail(), ip);
        //校验邮箱或者账户是否存在
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("email", loginInfo.getEmail()).eq("username", loginInfo.getUsername());
        User user = this.getOne(query);
        if (user == null) {
            RequestCodeMessage.ACCOUNT_EMAIL_INCORRECT.throwIt();
        }
        log.info("user=>>>>>>>>>{}", JSON.toJSONString(user));
        return this.emailInfo(loginInfo, "vc:vf:mini:login:code:");
    }

}
