package com.joy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joy.dto.blog.message.CommentParamDto;
import com.joy.entity.blog.message.ContentMessageComment;

public interface MessageCommentServer {
    Page<ContentMessageComment> getComment(CommentParamDto params);

    Boolean review(CommentParamDto params);
}
