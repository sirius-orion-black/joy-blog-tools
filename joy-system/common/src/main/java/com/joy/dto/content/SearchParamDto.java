package com.joy.dto.content;

import lombok.Data;

@Data
public class SearchParamDto {

    //名称
    private String name;

    //状态
    private Integer state;

    //类型
    private Integer type;

    //当前页
    private Integer page;

    //条数
    private Integer size;
}
