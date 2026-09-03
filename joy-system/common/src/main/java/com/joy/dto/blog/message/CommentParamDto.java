package com.joy.dto.blog.message;

import lombok.Data;

@Data
public class CommentParamDto {

    //id
    private Long id;

    //状态
    private Integer state;

    //当前页
    private Integer page;

    //条数
    private Integer size;

}
