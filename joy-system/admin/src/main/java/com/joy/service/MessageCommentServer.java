package com.joy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joy.dto.message.CommentParamDto;
import com.joy.entity.message.MessageComment;

public interface MessageCommentServer {
    Page<MessageComment> getComment(CommentParamDto params);

    Boolean review(CommentParamDto params);
}
