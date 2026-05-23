package com.joy.controller;

import com.joy.common.Result;
import com.joy.config.apiPrefix.ApiPrefixBlogVoRestController;
import com.joy.entity.content.ContentMoments;
import com.joy.service.MomentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@ApiPrefixBlogVoRestController
@RequestMapping("/moments")
@RequiredArgsConstructor
public class MomentsController {

    private final MomentsService momentsService;

    /**
     * 获取朋友圈列表
     */
    @GetMapping("/getList")
    public Result<List<ContentMoments>> moments(){
        return Result.success(momentsService.moments());
    }

}
