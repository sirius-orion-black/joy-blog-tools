package com.joy.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joy.common.Result;
import com.joy.dao.sysUser.SysLoginDao;
import com.joy.entity.sysConfig.SysConfig;
import com.joy.entity.sysConfig.SysConfigMail;
import com.joy.entity.sysUser.SysUser;
import com.joy.mapper.sysConfig.SysConfigMailMapper;
import com.joy.mapper.sysConfig.SysConfigMapper;
import com.joy.mapper.sysUser.SysUserMapper;
import com.joy.service.SysLoginService;
import com.joy.untils.UserVerify;
import com.joy.untils.VerifyCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;

import java.util.List;

@Slf4j
@Service
public class SysLoginServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysLoginService {

    @Autowired
    private SysConfigMailMapper sysConfigMailMapper;

    @Autowired
    private SysConfigMapper sysConfigMapper;


    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    /**
     * 后台管理人员邮箱验证
     *
     * @param loginInfo
     * @return
     */
    @Override
    public Result<String> emailVerify(SysLoginDao loginInfo) {
        if (StringUtils.isEmpty(loginInfo.getUsername())) {//判断用户名
            return Result.success("username_cannot_empty");
        }
        if (StringUtils.isEmpty(loginInfo.getEmail()) || !UserVerify.emailFormat(loginInfo.getEmail())) {//判断邮箱
            return Result.success("email_format_incorrect");
        }
        //校验邮箱或者账户是否存在
        QueryWrapper<SysUser> query = new QueryWrapper<>();
        query.eq("email", loginInfo.getEmail()).eq("username", loginInfo.getUsername());
        List<SysUser> userList = list(query);
        if (userList.isEmpty()) {
            return Result.success("account_email_incorrect");
        }
        SysUser user = userList.get(0);
        log.info("user=>>>>>>>>>" + JSON.toJSONString(user));
        //获取配置邮箱
        QueryWrapper<SysConfigMail> mailQuery = new QueryWrapper<>();
        mailQuery.eq("state", 1);
        List<SysConfigMail> mailList = sysConfigMailMapper.selectList(mailQuery);
        if (mailList.isEmpty()) {
            log.info("邮箱未配");
            return Result.internalServerError();
        }
        SysConfigMail mail = mailList.get(0);
        log.info("mail=>>>>" + JSON.toJSONString(mail));
        //获取验证码有效期
        QueryWrapper<SysConfig> config = new QueryWrapper<>();
        config.eq("config_key", "email_auth_valid_time");
        SysConfig validConfig = sysConfigMapper.selectOne(config);
        if (validConfig == null) {
            log.info("邮箱有效期未配置");
            return Result.internalServerError();
        }
        Long validTime = NumberUtils.parseNumber(validConfig.getConfigValue(), Long.class);
        boolean bl = VerifyCode.sendVerificationCode(mail.getUsername(), loginInfo.getEmail(), validTime, mail, redisTemplate);
        String message = bl ? "verification_code_sent" : "verification_code_sent_again";
        return Result.success(message);
    }

    /**
     * 后台管理人员登录
     *
     * 。0.0.。
     * ++
     * 
     * @param loginInfo
     * @return
     */
    @Override
    public Result<SysLoginDao> login(SysLoginDao loginInfo) {
        QueryWrapper<SysUser> query = new QueryWrapper<>();
        query.eq("username",loginInfo.getUsername());
        if (loginInfo.getLoginType() == 1) {
            SysUser user = getOne(query);
            if(user == null){
                return Result.success();
            }
        }
        log.info("登录来了=======>>>>>>>>>>" + loginInfo.toString());
        return Result.success("0");
    }
}
