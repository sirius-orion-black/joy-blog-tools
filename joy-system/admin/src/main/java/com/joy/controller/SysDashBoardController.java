package com.joy.controller;

import com.joy.common.Result;
import com.joy.config.apiPrefix.ApiPrefixAdminRestController;
import com.joy.service.SysDashBoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@ApiPrefixAdminRestController
@Slf4j
@RequestMapping("/dash")
public class SysDashBoardController {

    @Autowired
    private SysDashBoardService dashService;

    /**
     * 获取仪表盘数据
     * @return 返回数据
     */
    @GetMapping("/blog")
    public Result<Map<String,Object>> dashBlog(){
        return Result.success(dashService.dashBlog());
    }

}
