package com.joy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joy.common.Result;
import com.joy.config.apiPrefix.ApiPrefixAdminRestController;
import com.joy.dto.content.BlogPostParamDto;
import com.joy.dto.content.BlogpostUpdateDto;
import com.joy.entity.content.ContentBlogpost;
import com.joy.entity.content.ContentClassify;
import com.joy.entity.content.ContentColumn;
import com.joy.entity.content.ContentLabel;
import com.joy.service.ContentBlogpostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@ApiPrefixAdminRestController
@RequestMapping("/content/blogpost")
@Slf4j
public class ContentBlogpostController {

    @Autowired
    private ContentBlogpostService contentBlogpostService;

    /**
     * 获取文章列表
     * @param params
     * @return
     */
    @GetMapping("/getList")
    public Result<Page<ContentBlogpost>> getBlogpost(BlogPostParamDto params){
        return  contentBlogpostService.getBlogpost(params);
    }

    /**
     * 创建博客文章
     * @param blogpost
     * @return
     */
    @PostMapping("/add")
    public Result<String> createBlogpost(@RequestBody ContentBlogpost blogpost) throws IOException {
        return contentBlogpostService.createBlogpost(blogpost);
    }

    /**
     * 修改博客文章
     * @param blogpost
     * @return
     */
    @PostMapping("/edit")
    public Result<String> editBlogpost(@RequestBody ContentBlogpost blogpost) throws IOException {
        return contentBlogpostService.editBlogpost(blogpost);
    }

    /**
     * 删除博客文章
     * @param blogpost
     * @return
     */
    @PostMapping("/delete")
    public  Result<String> delBlogpost(@RequestBody ContentBlogpost blogpost) throws IOException {
        return contentBlogpostService.delBlogpost(blogpost);
    }

    /**
     * 更新状态
     * @param blogpost
     * @return
     */
    @PostMapping("/update")
    public  Result<String> updateBlogpost(@RequestBody BlogpostUpdateDto blogpost) throws IOException {
        return contentBlogpostService.updateBlogpost(blogpost);
    }

    /**
     * 获取分类
     * @return
     */
    @GetMapping("/getClassify")
    public Result<List<ContentClassify>> getBlogpostClassify(){
        return contentBlogpostService.getBlogpostClassify();
    }

    /**
     * 获取标签
     */
    @GetMapping("getLabel")
    public Result<List<ContentLabel>> getBlogpostLabel(){
        return contentBlogpostService.getBlogpostLabel();
    }

    /**
     * 获取用户专栏
     */
    @GetMapping("getColumn")
    public Result<List<ContentColumn>> getUserColumn(){
        return contentBlogpostService.getUserColumn();
    }

}
