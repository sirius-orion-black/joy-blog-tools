package com.joy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joy.entity.sysUser.SysUser;
import com.joy.mapper.SysUserMapper;
import com.joy.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public String addUser(SysUser sysUser) {
        // 校验用户名
        if (sysUser.getUsername() == null || sysUser.getUsername().isEmpty()) {
            return "username_cannot_empty";//用户名不能为空
        }
        if (!sysUser.getUsername().matches("^[A-Za-z]{5,16}$")) {
            return "username_between_5_16";//用户名长度必须在5-16个字符之间
        }
        if (sysUserMapper.countByUsername(sysUser.getUsername()) > 0) {
            return "username_already_exists";//用户名已经存在
        }
        // 校验邮箱
        if (sysUser.getEmail() == null || sysUser.getEmail().isEmpty()) {
            return "email_cannot_empty";//邮箱不能为空
        }
        log.info("qq-email:" + sysUser.getEmail().matches("^[1-9]\\\\d{4,}@qq\\\\.com$"));
        log.info("普通email:" + sysUser.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\\\.[A-Z|a-z]{2,}$"));
        if (!sysUser.getEmail().matches("^[1-9]\\\\d{4,}@qq\\\\.com$") && !sysUser.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\\\.[A-Z|a-z]{2,}$")) {
            return "email_format_incorrect";//邮箱格式不正确
        }
        if (sysUserMapper.countByEmail(sysUser.getEmail()) > 0) {
            return "email_already_exists";//邮箱已经存在
        }
        // 校验密码
        if (sysUser.getPassword() == null || sysUser.getPassword().isEmpty()) {
            return "password_cannot_empty";//密码不能为空
        }
        if (!sysUser.getPassword().matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[?@#]).{8,16}$")) {
            return "password_number_incorrect";//密码格式不正确
        }
        // 校验手机号
        if (sysUser.getPhone() == null || sysUser.getPhone().isEmpty()) {
            return "phone_cannot_empty";//手机号不能为空
        }
        if (!sysUser.getPhone().matches("^1[3-9]\\d{9}$")) {
            return "phone_number_incorrect";//手机号格式不正确
        }
        SysUser user = new SysUser();
        user.setEmail(sysUser.getEmail());
        // 保存用户
        return save(user) ? "操作成功" : "系统异常";
    }
}
