package com.joy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joy.common.Result;
import com.joy.config.apiPrefix.ApiPrefixAdminRestController;
import com.joy.dto.content.SearchParamDto;
import com.joy.entity.content.ContentColumn;
import com.joy.service.ContentColumnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@ApiPrefixAdminRestController
@RequestMapping("/content/column")
@Slf4j
public class ContentColumnController {

    @Autowired
    private ContentColumnService contentColumnService;

    /**
     * 获取专栏列表
     * @param params
     * @return
     */
    @GetMapping("/getList")
    public Result<Page<ContentColumn>> getColumn(SearchParamDto params){
        return  contentColumnService.getColumn(params);
    }

    /**
     *新增专栏
     * @param column
     * @return
     */
    @PostMapping("/add")
    public Result<String> addColumn(@RequestBody ContentColumn column){
        return contentColumnService.addColumn(column);
    }

    /**
     * 修改专栏
     * @param column
     * @return
     */
    @PostMapping("/edit")
    public Result<String> editColumn(@RequestBody ContentColumn column){
        return contentColumnService.editColumn(column);
    }

    /**
     * 删除专栏
     * @param column
     * @return
     */
    @PostMapping("/delete")
    public  Result<String> delColumn(@RequestBody ContentColumn column){
        return contentColumnService.delColumn(column);
    }

    /**
     * 预审专栏
     * @param column
     * @return
     */
    @PostMapping("/review")
    public  Result<String> reviewColumn(@RequestBody ContentColumn column){
        return contentColumnService.reviewColumn(column);
    }


}
