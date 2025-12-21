package com.joy.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joy.common.Result;
import com.joy.dto.auth.CaptchaDto;
import com.joy.dto.sysUser.SysLoginDto;
import com.joy.dto.sysUser.SysUserInfoDto;
import com.joy.entity.sysConfig.SysConfig;
import com.joy.entity.sysConfig.SysConfigMail;
import com.joy.entity.sysUser.SysUser;
import com.joy.mapper.sysConfig.SysConfigMailMapper;
import com.joy.mapper.sysConfig.SysConfigMapper;
import com.joy.mapper.sysUser.SysUserMapper;
import com.joy.service.SysLoginService;
import com.joy.utils.CaptchaCodeUtil;
import com.joy.utils.UserVerifyUtil;
import com.joy.utils.VerifyCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
    public Result<String> emailVerify(SysLoginDto loginInfo) {
        if (StringUtils.isEmpty(loginInfo.getUsername())) {//判断用户名
            return Result.badRequest("username_cannot_empty");
        }
        if (StringUtils.isEmpty(loginInfo.getEmail()) || !UserVerifyUtil.emailFormat(loginInfo.getEmail())) {//判断邮箱
            return Result.badRequest("email_format_incorrect");
        }
        //校验邮箱或者账户是否存在
        QueryWrapper<SysUser> query = new QueryWrapper<>();
        query.eq("email", loginInfo.getEmail()).eq("username", loginInfo.getUsername());
        List<SysUser> userList = list(query);
        if (userList.isEmpty()) {
            return Result.badRequest("account_email_incorrect");
        }
        SysUser user = userList.get(0);
        log.info("user=>>>>>>>>>" + JSON.toJSONString(user));
        //获取配置邮箱
        QueryWrapper<SysConfigMail> mailQuery = new QueryWrapper<>();
        mailQuery.eq("state", 1);
        List<SysConfigMail> mailList = sysConfigMailMapper.selectList(mailQuery);
        if (mailList.isEmpty()) {
            log.info("============>>>>>>>>>邮箱未配");
            return Result.internalServerError();
        }
        SysConfigMail mail = mailList.get(0);
        log.info("=======mail=>>>>" + JSON.toJSONString(mail));
        //获取验证码有效期
        QueryWrapper<SysConfig> config = new QueryWrapper<>();
        config.eq("config_key", "email_auth_valid_time");
        SysConfig validConfig = sysConfigMapper.selectOne(config);
        if (validConfig == null) {
            log.info("============>>>>>>>>>邮箱有效期未配置");
            return Result.internalServerError();
        }
        Long validTime = NumberUtils.parseNumber(validConfig.getConfigValue(), Long.class);
        boolean bl = VerifyCodeUtil.sendVerificationCode(mail.getUsername(), loginInfo.getEmail(), validTime, mail, redisTemplate);
        String message = bl ? "verification_code_sent" : "verification_code_sent_again";
        return Result.success(message);
    }

    /**
     * 验证码
     *
     * @return
     */
    @Override
    public Result<CaptchaDto> getCaptcha() {
        CaptchaDto captcha = new CaptchaDto();
        CaptchaCodeUtil.getCaptcha(captcha);
        return Result.success(captcha);
    }

    private String loginVerify(SysLoginDto loginInfo, SysUser user){
        if (user == null)
            return "username_password_incorrect";
        if(user.getState().equals(2))
            return "account_banned";
        if(!BCrypt.checkpw(loginInfo.getPassword(), user.getPassword()))
            return "username_password_incorrect";
        return null;
    }

    /**
     * 后台管理人员登录
     * <p>
     * 。0.0.。
     * ++
     *
     * @param loginInfo
     * @return
     */
    @Override
    public Result<SysUserInfoDto> login(SysLoginDto loginInfo) {

        String message = CaptchaCodeUtil.checkImageCode(loginInfo.getNonceStr(), loginInfo.getMove());
        if (!StringUtils.isEmpty(message))
            return Result.badRequest(message);

        QueryWrapper<SysUser> query = new QueryWrapper<>();
        if (loginInfo.getLoginType() == 1) {
            query.eq("username", loginInfo.getUsername());
        }
        SysUser user = getOne(query);
        String mes = loginVerify(loginInfo,user);
        if (!StringUtils.isEmpty(mes))
            return Result.badRequest(mes);

        StpUtil.login(user.getId(),loginInfo.getRemember());

        SysUserInfoDto sysUser = new SysUserInfoDto();
        sysUser.setEmail(user.getEmail());
        sysUser.setBirthday(user.getBirthday());
        sysUser.setPhone(user.getPhone());
        sysUser.setSignature(user.getSignature());
        sysUser.setState(user.getState());
        sysUser.setAvatar(user.getAvatar());
        sysUser.setNickname(user.getNickname());
        sysUser.setToken(StpUtil.getTokenValue());

        log.info("=======登录来了=======>>>>>>>>>>" + user.toString());
        return Result.success(sysUser);
    }
}
