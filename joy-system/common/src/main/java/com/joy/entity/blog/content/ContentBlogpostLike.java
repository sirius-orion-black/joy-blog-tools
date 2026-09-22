package com.joy.entity.blog.content;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName
public class ContentBlogpostLike {

    //主键id
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    //博客文章id
    @TableField
    private Long blogpostId;

    //用户id
    @TableField
    private Long userId;

}
