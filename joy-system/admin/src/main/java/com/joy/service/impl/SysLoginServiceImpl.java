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
import com.joy.enums.http.AdminCodeMessage;
import com.joy.mapper.sysConfig.SysConfigMailMapper;
import com.joy.mapper.sysConfig.SysConfigMapper;
import com.joy.mapper.sysUser.SysUserMapper;
import com.joy.service.SysLoginService;
import com.joy.utils.CaptchaCodeUtil;
import com.joy.utils.IpRegionUtil;
import com.joy.utils.UserVerifyUtil;
import com.joy.utils.VerifyCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SysLoginServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysLoginService {

    @Autowired
    private SysConfigMailMapper sysConfigMailMapper;

    @Autowired
    private SysConfigMapper sysConfigMapper;

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

    /**
     * 登录校验
     *
     * @param loginInfo
     * @param user
     * @return
     */
    private void loginVerify(SysLoginDto loginInfo, SysUser user) {
        if (user == null || !BCrypt.checkpw(loginInfo.getPassword(), user.getPassword())){
            AdminCodeMessage.USERNAME_PASSWORD_INCORRECT.throwIt();
            return;
        }
        if (user.getState().equals(2))
            AdminCodeMessage.ACCOUNT_BANNED.throwIt();
    }

    /**
     * 封装登录数据
     *
     * @param user
     * @return
     */
    @NotNull
    private static SysUserInfoDto getUserInfoDto(SysUser user) {
        SysUserInfoDto sysUser = new SysUserInfoDto();
        sysUser.setId(user.getId());
        sysUser.setEmail(user.getEmail());
        sysUser.setBirthday(user.getBirthday());
        sysUser.setPhone(user.getPhone());
        sysUser.setSignature(user.getSignature());
        sysUser.setState(user.getState());
        sysUser.setAvatar(user.getAvatar());
        sysUser.setNickname(user.getNickname());
        sysUser.setUsername(user.getUsername());
        sysUser.setToken(StpUtil.getTokenValue());
        return sysUser;
    }

    /**
     * 后台管理人员登录
     *
     * @param loginInfo
     * @return
     */
    @Override
    public Result<SysUserInfoDto> login(SysLoginDto loginInfo) {
        QueryWrapper<SysUser> query = new QueryWrapper<>();
        SysUser user = null;
        String mes = "";
        if (loginInfo.getLoginType() == 1) {//账号密码登录
            if (StringUtils.isBlank(loginInfo.getUsername()) || StringUtils.isEmpty(loginInfo.getPassword())) {//判断用户名密码不为空
                AdminCodeMessage.USERNAME_PASSWORD_INCORRECT.throwIt();
            }
            CaptchaCodeUtil.checkImageCode(loginInfo.getNonceStr(), loginInfo.getMove());
            query.eq("username", loginInfo.getUsername());
            user = this.getOne(query);
            loginVerify(loginInfo, user);
        } else {//邮箱登录loginInfo.getLoginType() == 2，现在还没有第三种登录，所以这里直接else了
            if (StringUtils.isBlank(loginInfo.getUsername()) || StringUtils.isEmpty(loginInfo.getEmail())) {//判断用户名邮箱不为空
                AdminCodeMessage.ACCOUNT_EMAIL_INCORRECT.throwIt();
            }
            VerifyCodeUtil.verifyCode(
                    loginInfo.getEmail(),
                    "vc:vf:login:code:",
                    loginInfo.getCode(),
                    loginInfo.getValidTime());
            query.eq("email", loginInfo.getEmail()).eq("username", loginInfo.getUsername());
            user = this.getOne(query);
        }
        if(user == null){
            AdminCodeMessage.USER_INFO_CORRECT.throwIt();
            return null;
        }
        StpUtil.login(user.getId(), loginInfo.getRemember());

        SysUserInfoDto sysUser = getUserInfoDto(user);

        log.info("=======登录来了=======>>>>>>>>>>{}", user.toString());
        return Result.success(sysUser);
    }

    /**
     * 后台管理人员邮箱验证
     *
     * @param loginInfo
     * @param request
     * @return
     */
    @Override
    public Result<Map<String,Integer>> emailVerify(SysLoginDto loginInfo, HttpServletRequest request) {
        if (StringUtils.isBlank(loginInfo.getUsername())) {//判断用户名
            AdminCodeMessage.USERNAME_CANNOT_EMPTY.throwIt();
        }
        if (StringUtils.isBlank(loginInfo.getEmail()) || !UserVerifyUtil.emailFormat(loginInfo.getEmail())) {//判断邮箱
            AdminCodeMessage.EMAIL_FORMAT_INCORRECT.throwIt();
        }
        String ip = IpRegionUtil.getClientIpAddress(request);

        VerifyCodeUtil.checkSendAllowed(loginInfo.getEmail(), ip);
        //校验邮箱或者账户是否存在
        QueryWrapper<SysUser> query = new QueryWrapper<>();
        query.eq("email", loginInfo.getEmail()).eq("username", loginInfo.getUsername());
        SysUser user = this.getOne(query);
        if (user == null) {
            AdminCodeMessage.ACCOUNT_EMAIL_INCORRECT.throwIt();
        }
        log.info("user=>>>>>>>>>{}", JSON.toJSONString(user));
        //获取是否启用云邮箱配置
        QueryWrapper<SysConfig> config = new QueryWrapper<>();
        config.eq("config_key", "is_cloud_email");
        SysConfig cloudEmail = sysConfigMapper.selectOne(config);
        if (cloudEmail == null) {
            log.info("============>>>>>>>>>是否启用云邮箱未配置");
            AdminCodeMessage.ENABLE_CLOUD_EMAIL_CONFIG.throwIt();
            return null;
        }
        // 生成验证码
        String verificationCode = RandomStringUtils.randomAlphanumeric(6);
        if (cloudEmail.getConfigValue().equals("yes")) {
            log.info("yes");
        } else {
            //获取配置邮箱
            QueryWrapper<SysConfigMail> mailQuery = new QueryWrapper<>();
            mailQuery.eq("state", 1);
            List<SysConfigMail> mailList = sysConfigMailMapper.selectList(mailQuery);
            if (mailList.isEmpty()) {
                log.info("============>>>>>>>>>邮箱未配");
                AdminCodeMessage.EMAIL_NOT_CONFIG.throwIt();
            }
            SysConfigMail mail = mailList.get(0);
            //发送邮件
            boolean bl = VerifyCodeUtil.sendEmail(
                    verificationCode,
                    VerifyCodeUtil.buildEmailVerifyContent(mail, verificationCode),
                    loginInfo.getEmail(),
                    mail);
            String message = AdminCodeMessage.VERIFICATION_CODE_SENT_AGAIN.getMessage();
            if (bl) {
                message = AdminCodeMessage.VERIFICATION_CODE_SENT.getMessage();
                VerifyCodeUtil.markSent(loginInfo.getEmail());
                VerifyCodeUtil.saveCodeAsync(loginInfo.getEmail(), "vc:vf:login:code:", verificationCode, mail.getValidTime());
            }
            Map<String,Integer> result = new HashMap<>();
            result.put("validTime",mail.getValidTime());
            return Result.success(result,message);
        }
        return Result.success();
    }

    /**
     * 后台管理人员登出
     *
     * @return
     */
    @Override
    public Result<String> logout() {
        StpUtil.logout();
        return Result.success();
    }
}
