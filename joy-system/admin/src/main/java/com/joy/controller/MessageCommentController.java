package com.joy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joy.common.Result;
import com.joy.config.apiPrefix.ApiPrefixAdminRestController;
import com.joy.dto.message.CommentParamDto;
import com.joy.entity.message.MessageComment;
import com.joy.service.MessageCommentServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@ApiPrefixAdminRestController
@RequestMapping("/message/comment")
public class MessageCommentController {

    @Autowired
    private MessageCommentServer commentServer;

    /**
     *
     * @param params 参数
     * @return 返回数据
     */
    @GetMapping("getList")
    public Result<Page<MessageComment>> getComment(CommentParamDto params){
        return Result.success(commentServer.getComment(params));
    }

    /**
     *
     * @param params 参数
     * @return 返回数据
     */
    @PostMapping("review")
    public Result<Boolean> review(@RequestBody CommentParamDto params){
        return Result.success(commentServer.review(params));
    }

}
