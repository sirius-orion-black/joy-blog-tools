package com.joy.entity.content;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName
public class ContentBlogpostLike {

    //博客文章id
    @TableField
    private Long blogpostId;

    //用户id
    @TableField
    private Long userId;

}
