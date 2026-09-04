package com.joy.service;

import com.joy.common.Result;
import com.joy.dto.auth.CaptchaDto;
import com.joy.dto.common.LoginDto;
import com.joy.dto.sysUser.SysUserInfoDto;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface SysLoginService {
    Result<CaptchaDto> getCaptcha();

    Result<SysUserInfoDto> login(LoginDto loginInfo);

    Result<Map<String,Integer>> emailVerify(LoginDto loginInfo, HttpServletRequest request) throws Exception;

    Result<String> logout();
}
