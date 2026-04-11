package com.joy.controller;

import com.joy.common.Result;
import com.joy.config.apiPrefix.ApiPrefixAdminRestController;
import com.joy.entity.sysConfig.SysConfig;
import com.joy.service.SysWebConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@ApiPrefixAdminRestController
@RequestMapping("/config/web/")
@Slf4j
public class SysWebConfigController {

    @Autowired
    private SysWebConfigService sysWebConfigService;

    /**
     * 获取所有配置
     * @return
     */
    @GetMapping("/get")
    public Result<Map<String, List<SysConfig>>> getConfig(){
        return sysWebConfigService.getConfig();
    }

    /**
     * 更新配置
     * @param config
     * @return
     */
    @PostMapping("edit")
    public Result<String> editConfig(@RequestBody SysConfig config) throws IOException {
        return sysWebConfigService.editConfig(config);
    }



}
