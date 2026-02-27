package com.joy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joy.common.Result;
import com.joy.config.apiPrefix.ApiPrefixAdminRestController;
import com.joy.dto.content.SearchParamDto;
import com.joy.entity.content.ContentClassify;
import com.joy.service.ContentClassifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@ApiPrefixAdminRestController
@RequestMapping("/content/classify")
@Slf4j
public class ContentClassifyController {

    @Autowired
    private ContentClassifyService contentClassifyService;

    /**
     * 获取类型列表
     * @param params
     * @return
     */
    @GetMapping("/getList")
    public Result<Page<ContentClassify>> getClassify(SearchParamDto params){
        return contentClassifyService.getClassify(params);
    }

    /**
     * 新增类型
     * @param classify
     * @return
     */
    @PostMapping("/add")
    public Result<String> addClassify(@RequestBody ContentClassify classify){
        return contentClassifyService.addClassify(classify);
    }

    /**
     * 修改类型
     * @param classify
     * @return
     */
    @PostMapping("/edit")
    public Result<String> editClassify(@RequestBody ContentClassify classify){
        return contentClassifyService.editClassify(classify);
    }

    /**
     * 删除类型
     * @param classifyIds
     * @return
     */
    @PostMapping("/delete")
    public Result<String> delClassify(@RequestBody List<Long> classifyIds){
        return contentClassifyService.delClassify(classifyIds);
    }
}
