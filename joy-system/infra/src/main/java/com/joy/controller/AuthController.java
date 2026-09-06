package com.joy.controller;

import com.joy.common.Result;
import com.joy.config.annotation.RateLimiter;
import com.joy.config.apiPrefix.ApiPrefixInfraRestController;
import com.joy.dto.auth.EmailVerifyDto;
import com.joy.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
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
    public Result<Map<String,Integer>> emailCode(EmailVerifyDto loginInfo, HttpServletRequest request) throws Exception {
        return authService.emailCode(loginInfo,request);
    }



}
