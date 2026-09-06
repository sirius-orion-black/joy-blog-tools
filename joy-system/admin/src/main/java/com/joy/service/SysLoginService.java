package com.joy.service;

import com.joy.common.Result;
import com.joy.dto.auth.CaptchaDto;
import com.joy.dto.auth.EmailVerifyDto;
import com.joy.dto.sysUser.SysLoginDto;
import com.joy.dto.sysUser.SysUserInfoDto;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface SysLoginService {
    Result<CaptchaDto> getCaptcha();

    Result<SysUserInfoDto> login(SysLoginDto loginInfo);

    Result<Map<String,Integer>> emailVerify(EmailVerifyDto loginInfo, HttpServletRequest request) throws Exception;

    Result<String> logout();
}
