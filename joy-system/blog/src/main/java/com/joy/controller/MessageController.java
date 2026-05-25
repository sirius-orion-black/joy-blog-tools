package com.joy.controller;

import com.joy.common.Result;
import com.joy.config.apiPrefix.ApiPrefixBlogVoRestController;
import com.joy.dto.content.PostCommentDto;
import com.joy.entity.content.ContentMessage;
import com.joy.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ApiPrefixBlogVoRestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    /**
     * 提交留言
     * @param request 头部信息
     * @param comment 提交的数据
     * @return 返回的数据
     */
    @PostMapping("/post")
    public Result<String> postComment(HttpServletRequest request, @RequestBody PostCommentDto comment) throws Exception {
        return Result.success(messageService.postComment(request,comment));
    }

    /**
     * 获取列表
     * @return 返回的数据
     */
    @GetMapping("/getList")
    public Result<List<ContentMessage>> messageList() {
        return Result.success(messageService.messageList());
    }

}
