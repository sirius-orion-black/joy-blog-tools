package com.joy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joy.common.Result;
import com.joy.config.apiPrefix.ApiPrefixAdminRestController;
import com.joy.dto.content.SearchParamDto;
import com.joy.entity.content.FeContentType;
import com.joy.service.ContentTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@ApiPrefixAdminRestController
@RequestMapping("/content/type")
@Slf4j
public class ContentTypeController {

    @Autowired
    private ContentTypeService contentTypeService;

    /**
     * 获取类型列表
     * @param params
     * @return
     */
    @GetMapping("/getList")
    public Result<Page<FeContentType>> getType(SearchParamDto params){
        return contentTypeService.getType(params);
    }

    /**
     * 修改类型
     * @param type
     * @return
     */
    @PostMapping("/edit")
    public Result<String> editType(@RequestBody FeContentType type){
        return contentTypeService.editType(type);
    }

    /**
     * 删除类型
     * @param typeIds
     * @return
     */
    @PostMapping("/delete")
    public Result<String> delType(@RequestBody List<Long> typeIds){
        return contentTypeService.delType(typeIds);
    }
}
