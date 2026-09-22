package com.joy.mapper.admin.sysUser;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.joy.entity.admin.sysUser.SysUser;
import org.apache.ibatis.annotations.Select;

public interface SysUserMapper extends BaseMapper<SysUser> {

    @Select("SELECT COUNT(*) FROM sys_user WHERE username = #{username}")
    int countByUsername(String username);

    @Select("SELECT COUNT(*) FROM sys_user WHERE email = #{email}")
    int countByEmail(String email);

}
