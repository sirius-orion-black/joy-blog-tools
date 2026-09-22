package com.joy.controller;

import com.joy.common.Result;
import com.joy.config.annotation.RateLimiter;
import com.joy.config.apiPrefix.ApiPrefixInfraRestController;
import com.joy.dto.common.auth.EmailVerifyDto;
import com.joy.dto.common.user.*;
import com.joy.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@ApiPrefixInfraRestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 邮箱验证
     * @param loginInfo
     * @return
     */
    @PostMapping("/email/verifyCode")
    @RateLimiter(key = "vc:email_code", maxCount = 3, time = 60, timeUnit = TimeUnit.SECONDS, message = "too_many_requests")
    public Result<Map<String,Integer>> emailCode(@RequestBody EmailVerifyDto loginInfo, HttpServletRequest request) throws Exception {
        return authService.emailCode(loginInfo,request);
    }

    /**
     *
     * @param user 用户注册信息
     * @return 返回是否成功
     */
    @PostMapping("/register")
    public Result<String> register(@Valid @RequestBody RegisterDto user) {
        return  authService.register(user);
    }

    /**
     * 登录
     * @param user 用户登录信息
     * @return 返回相应信息
     */
    @PostMapping("/login")
    public Result<UserInfoDto> login(@Valid @RequestBody LoginDto user) {
        return  authService.login(user);
    }

    /**
     * 忘记密码
     * @param user 用户忘记密码信息
     * @return 返回是否成功
     */
    @PostMapping("/reset/password")
    public Result<String> resetPassword(@Valid @RequestBody ResetPasswordDto user) {
        return  authService.resetPassword(user);
    }

    /**
     * 微信一键登录
     * @param req //微信回传code
     * @return 返回是否需要绑定或者用户信息
     */
    @PostMapping("/wxLogin")
    public Result<UserInfoDto> wxLogin(@RequestBody Map<String, String> req) {
        return  authService.wxLogin(req);
    }

    /**
     *
     * 微信用户绑定
     * @param req 微信用户与用户绑定
     * @return 返回用户信息
     */
    @PostMapping("/wxBind")
    public Result<UserInfoDto> wxBind(@Valid @RequestBody WxBindDto req) {
        return  authService.wxBind(req);
    }


}
