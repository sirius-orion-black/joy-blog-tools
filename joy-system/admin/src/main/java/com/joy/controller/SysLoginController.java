package com.joy.controller;

import com.joy.common.Result;
import com.joy.config.annotation.RateLimiter;
import com.joy.config.apiPrefix.ApiPrefixAdminRestController;
import com.joy.dto.auth.CaptchaDto;
import com.joy.dto.sysUser.SysLoginDto;
import com.joy.dto.sysUser.SysUserInfoDto;
import com.joy.service.SysLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@ApiPrefixAdminRestController
@Slf4j
@RequestMapping("/auth")
public class SysLoginController {

    @Autowired
    private SysLoginService sysLoginService;

    /**
     * 验证码
     * @return
     */
    @GetMapping("/getCaptcha")
    public Result<CaptchaDto> getCaptcha(){
        return sysLoginService.getCaptcha();
    }

    /**
     * 后台管理人员登录
     * @param loginInfo
     * @return
     */
    @PostMapping("/login")
    public Result<SysUserInfoDto> login(SysLoginDto loginInfo) {
        return sysLoginService.login(loginInfo);
    }

    /**
     * 后台管理人员邮箱验证
     * @param loginInfo
     * @return
     */
    @PostMapping("/emailVerifyCode")
    @RateLimiter(key = "vc:email_code", maxCount = 3, time = 60, timeUnit = TimeUnit.SECONDS, message = "too_many_requests")
    public Result<Map<String,Integer>> emailVerify(SysLoginDto loginInfo, HttpServletRequest request) {
        return sysLoginService.emailVerify(loginInfo,request);
    }

    /**
     * 后台管理人员登出
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return sysLoginService.logout();
    }

}
