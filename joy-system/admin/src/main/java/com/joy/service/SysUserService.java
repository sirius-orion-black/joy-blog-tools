package com.joy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joy.common.Result;
import com.joy.dto.sysUser.SysUserDto;
import com.joy.dto.sysUser.UserMenuDto;
import com.joy.entity.sysUser.SysUser;

import java.util.List;
import java.util.Map;

public interface SysUserService {
    
    Result<Page<SysUser>> getUser(SysUserDto userDto);

    Result<Map<String,String>> addUser(SysUser sysUser);

    Result<String> editUser(SysUser sysUser);

    Result<String> delUser(List<Long> userIds);

    Result<String> bannedUser(List<SysUserDto> users);

    Result<UserMenuDto> getUserMenu(UserMenuDto users);

    Result<String> editUserMenu(UserMenuDto users);
}
