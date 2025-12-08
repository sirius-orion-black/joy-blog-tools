package com.joy.service;

import com.joy.common.Result;
import com.joy.entity.sysUser.SysUser;

public interface SysUserService {

    Result<String> addUser(SysUser sysUser);

}
