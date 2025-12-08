package com.joy.controller;


import com.joy.common.Result;
import com.joy.config.apiPrefix.ApiPrefixBlogRestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@ApiPrefixBlogRestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    @GetMapping("/login")
    public  Result<String> login(){
        return Result.success("123");
    }

}


