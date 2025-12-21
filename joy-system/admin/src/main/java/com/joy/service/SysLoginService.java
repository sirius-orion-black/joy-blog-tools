package com.joy.service;

import com.joy.common.Result;
import com.joy.dto.auth.CaptchaDto;
import com.joy.dto.sysUser.SysLoginDto;
import com.joy.dto.sysUser.SysUserInfoDto;

public interface SysLoginService {
    Result<String> emailVerify(SysLoginDto loginInfo);

    Result<CaptchaDto> getCaptcha();

    Result<SysUserInfoDto> login(SysLoginDto loginInfo);

}
