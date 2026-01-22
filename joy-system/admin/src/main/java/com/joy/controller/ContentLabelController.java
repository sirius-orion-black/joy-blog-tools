package com.joy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joy.common.Result;
import com.joy.config.apiPrefix.ApiPrefixAdminRestController;
import com.joy.dto.content.SearchParamDto;
import com.joy.entity.content.FeContentLabel;
import com.joy.service.ContentLabelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@ApiPrefixAdminRestController
@RequestMapping("/content/label")
@Slf4j
public class ContentLabelController {

    @Autowired
    private ContentLabelService contentLabelService;

    /**
     * 获取标签列表
     * @param params
     * @return
     */
    @GetMapping("/getList")
    public Result<Page<FeContentLabel>> getLabel(SearchParamDto params){
        return contentLabelService.getLabel(params);
    }

    /**
     * 修改标签
     * @param label
     * @return
     */
    @PostMapping("/edit")
    public Result<String> editLabel(@RequestBody FeContentLabel label){
        return contentLabelService.editLabel(label);
    }

    /**
     * 删除标签
     * @param labelIds
     * @return
     */
    @PostMapping("/delete")
    public Result<String> delLabel(@RequestBody List<Long> labelIds){
        return contentLabelService.delLabel(labelIds);
    }
}
