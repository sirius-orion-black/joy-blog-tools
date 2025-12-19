package com.joy.controller;

import com.joy.common.Result;
import com.joy.config.apiPrefix.ApiPrefixAdminRestController;
import com.joy.dao.sysUser.SysLoginDao;
import com.joy.service.SysLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@ApiPrefixAdminRestController
@Slf4j
@RequestMapping("/auth")
public class SysLoginController {

    @Autowired
    private SysLoginService sysLoginService;

    /**
     * 后台管理人员邮箱验证
     *
     * @param loginInfo
     * @return
     */
    @PostMapping("/emailVerify")
    public Result<String> emailVerify(SysLoginDao loginInfo) {
        return sysLoginService.emailVerify(loginInfo);
    }

    /**
     * 后台管理人员登录
     *
     * @param loginInfo
     * @return
     */
    @PostMapping("login")
    public Result<SysLoginDao> login(SysLoginDao loginInfo) {
        return sysLoginService.login(loginInfo);
    }

}
