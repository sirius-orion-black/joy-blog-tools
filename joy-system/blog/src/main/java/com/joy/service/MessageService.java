package com.joy.service;

import com.joy.dto.content.PostCommentDto;
import com.joy.entity.content.ContentMessage;

import java.util.List;

public interface MessageService {
    String postComment(PostCommentDto comment);

    List<ContentMessage> messageList();
}
