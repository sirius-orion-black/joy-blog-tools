package com.joy.service;

import com.joy.common.Result;
import com.joy.dto.common.auth.CaptchaDto;
import com.joy.dto.common.auth.EmailVerifyDto;
import com.joy.dto.admin.sysUser.SysLoginDto;
import com.joy.dto.admin.sysUser.SysUserInfoDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface SysLoginService {
    Result<CaptchaDto> getCaptcha();

    Result<SysUserInfoDto> login(SysLoginDto loginInfo);

    Result<Map<String,Integer>> emailVerify(EmailVerifyDto loginInfo, HttpServletRequest request) throws Exception;

    Result<String> logout();
}
