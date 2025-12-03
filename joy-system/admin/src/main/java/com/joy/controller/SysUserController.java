package com.joy.controller;

import com.alibaba.fastjson2.JSON;
import com.joy.common.Result;
import com.joy.config.apiPrefix.ApiPrefixAdminRestController;
import com.joy.entity.sysUser.SysUser;
import com.joy.service.SysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@ApiPrefixAdminRestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/addUser")
    public Result<String> addUser(SysUser sysUser){
        log.info("add user:" + JSON.toJSONString(sysUser));
        return Result.success(sysUserService.addUser(sysUser));
    }

}
