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

import java.util.List;

@ApiPrefixBlogVoRestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/post")
    public Result<String> postComment(@RequestBody PostCommentDto comment){
        return Result.success(messageService.postComment(comment));
    }

    @GetMapping("/getList")
    public Result<List<ContentMessage>> messageList() {
        return Result.success(messageService.messageList());
    }

}
