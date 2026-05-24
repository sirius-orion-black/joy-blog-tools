package com.joy.dto.content;

import lombok.Data;

@Data
public class PostCommentDto {
    private String content;
    private Long parentId;   // 0 或 null 表示顶级留言，否则为被评论的留言ID
}
