package com.joy.controller;

import com.joy.common.Result;
import com.joy.config.apiPrefix.ApiPrefixAdminRestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@ApiPrefixAdminRestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class SysUserController {
    @GetMapping("/addUser")
    public Result<String> addUser(){
        log.info("add");
        return Result.success("123");
    }
}
