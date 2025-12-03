package com.joy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joy.entity.sysUser.SysUser;
import com.joy.mapper.SysUserMapper;
import com.joy.service.SysUserService;
import com.joy.untils.UserVerify;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public String addUser(SysUser sysUser) {
        String strVerify = UserVerify.sysUserVerify(sysUser);
        if(!strVerify.equals("success")){//用户信息校验
            return strVerify;
        }
        if (sysUserMapper.countByUsername(sysUser.getUsername()) > 0) {
            return "username_already_exists";//用户名已经存在
        }
        if (sysUserMapper.countByEmail(sysUser.getEmail()) > 0) {
            return "email_already_exists";//邮箱已经存在
        }
        if (!sysUser.getPhone().matches("^1[3-9]\\d{9}$")) {
            return "phone_number_incorrect";//手机号格式不正确
        }
        SysUser user = new SysUser();
        user.setEmail(sysUser.getEmail());
        // 保存用户
        return save(sysUser) ? "操作成功" : "系统异常";
    }
}
