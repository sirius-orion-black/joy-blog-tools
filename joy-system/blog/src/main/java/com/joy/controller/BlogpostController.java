package com.joy.controller;


import com.joy.common.Result;
import com.joy.config.apiPrefix.ApiPrefixBlogVoRestController;
import com.joy.entity.content.ContentBlogpost;
import com.joy.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@ApiPrefixBlogVoRestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class BlogpostController {

    private final ArticleService articleService;

    /**
     * 获取文章列表
     */
    @GetMapping("/article")
    public  Result<List<ContentBlogpost>> getBlogpost(@RequestParam(defaultValue = "0") Long id){
        return Result.success(articleService.getBlogpost(id));
    }

}


