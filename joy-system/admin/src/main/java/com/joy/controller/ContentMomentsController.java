package com.joy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joy.common.Result;
import com.joy.config.apiPrefix.ApiPrefixAdminRestController;
import com.joy.dto.content.SearchParamDto;
import com.joy.entity.content.ContentLabel;
import com.joy.entity.content.ContentMoments;
import com.joy.service.ContentMomentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@ApiPrefixAdminRestController
@RequestMapping("/content/moments")
public class ContentMomentsController {

    @Autowired
    private ContentMomentsService momentsService;

    /**
     *获取朋友圈列表
     * @return
     */
    @GetMapping("/getList")
    public Result<Page<ContentMoments>> getMoments(SearchParamDto params){
        return momentsService.getMoments(params);
    }

    /**
     * 发表朋友圈
     * @param moments
     * @return
     */
    @PostMapping("/add")
    public Result<String> addMoments(HttpServletRequest request, @RequestBody ContentMoments moments) throws IOException {
        return momentsService.createMoments(request, moments);
    }

    /**
     * 删除朋友圈
     * @param moments
     * @return
     */
    @PostMapping("/delete")
    public Result<String> delMoments(@RequestBody ContentMoments moments) throws IOException {
        return momentsService.updMoments(moments,"del");
    }

    /**
     * 审核朋友圈
     * @param moments
     * @return
     */
    @PostMapping("/review")
    public  Result<String> reviewMoments(@RequestBody ContentMoments moments) throws IOException {
        return momentsService.updMoments(moments,"review");
    }

    /**
     * 获取标签
     */
    @GetMapping("getLabel")
    public Result<List<ContentLabel>> getMomentsLabel(){
        return momentsService.getMomentsLabel();
    }

}
