package com.joy.dto.content;

import lombok.Data;

@Data
public class BlogpostUpdateDto {

    //主键id
    private Long id;

    //是否置顶:1 置顶 2 不置顶
//    private Integer isStick;

    //是否推荐: 1 推荐 2 不推荐
//    private Integer isRecommend;

    //状态: 1 上架 2 下架 3 草稿 4 预审 5 已退回 6 已下线 7 已归档 8 删除
//    private Integer state;

    //操作
    private String action;

}
