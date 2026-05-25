package com.joy.service;

import com.joy.dto.content.PostCommentDto;
import com.joy.entity.content.ContentMessage;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface MessageService {
    String postComment(HttpServletRequest request, PostCommentDto comment) throws Exception;

    List<ContentMessage> messageList();
}
