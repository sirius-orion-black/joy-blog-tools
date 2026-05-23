package com.joy.controller;

import com.joy.common.Result;
import com.joy.config.apiPrefix.ApiPrefixBlogVoRestController;
import com.joy.service.WebConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@ApiPrefixBlogVoRestController
@RequestMapping("/web")
@RequiredArgsConstructor
public class WebConfigController {
    private final WebConfigService configService;

    /**
     * 获取网站基本信息
     */
    @GetMapping("/getInfo")
    public Result<Map<String, String>> webConfig(){
        return Result.success(configService.webConfig());
    }
}
