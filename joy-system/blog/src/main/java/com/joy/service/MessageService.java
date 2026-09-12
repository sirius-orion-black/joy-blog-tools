package com.joy.service;

import com.joy.dto.blog.content.PostCommentDto;
import com.joy.entity.blog.message.ContentMessageComment;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface MessageService {
    String postComment(HttpServletRequest request, PostCommentDto comment) throws Exception;

    List<ContentMessageComment> messageList();
}
