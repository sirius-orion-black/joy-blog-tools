package com.joy.service;

import com.joy.common.Result;
import com.joy.dao.auth.CaptchaDao;
import com.joy.dao.sysUser.SysLoginDao;
import com.joy.dao.sysUser.SysUserInfoDao;

public interface SysLoginService {
    Result<String> emailVerify(SysLoginDao loginInfo);

    Result<CaptchaDao> getCaptcha();

    Result<SysUserInfoDao> login(SysLoginDao loginInfo);

}
