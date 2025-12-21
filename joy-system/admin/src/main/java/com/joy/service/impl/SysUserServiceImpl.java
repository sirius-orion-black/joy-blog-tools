package com.joy.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joy.common.Result;
import com.joy.entity.sysUser.SysUser;
import com.joy.mapper.sysUser.SysUserMapper;
import com.joy.service.SysUserService;
import com.joy.utils.UserVerifyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 新增管理人员
     *
     * @param sysUser
     * @return
     */
    @Override
    public Result<String> addUser(SysUser sysUser) {
        String strVerify = UserVerifyUtil.sysUserVerify(sysUser);
        if (strVerify.equals("success")) {//用户信息校验
            strVerify = "success";
        }
        if (sysUserMapper.countByUsername(sysUser.getUsername()) > 0) {
            strVerify = "username_already_exists";//用户名已经存在
        }
        if (sysUserMapper.countByEmail(sysUser.getEmail()) > 0) {
            strVerify = "email_already_exists";//邮箱已经存在
        }
        sysUser.setState(3);
        sysUser.setPassword(BCrypt.hashpw(sysUser.getPassword(), BCrypt.gensalt()));
        // 保存用户
        return save(sysUser) ? Result.success("success") : Result.badRequest(strVerify);
    }
}
