package com.joy.dto.content;

import lombok.Data;

@Data
public class BlogPostParamDto {
    //标题
    private String title;

    //状态
    private Integer state;

    //分类id
    private Long classifyId;

    //当前页
    private Integer page;

    //条数
    private Integer size;
}
