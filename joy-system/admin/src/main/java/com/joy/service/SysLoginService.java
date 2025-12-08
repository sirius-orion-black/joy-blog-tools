package com.joy.service;

import com.joy.common.Result;
import com.joy.dao.sysUser.SysLoginDao;

public interface SysLoginService {
    Result<String> emailVerify(SysLoginDao loginInfo);
}
