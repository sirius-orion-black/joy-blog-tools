package com.joy.entity.content;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName
public class FeContentBlogpost {

    //主键id
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    //文章名
    @TableField
    private String name;

    //文章简介
    @TableField
    private String introduction;

    //文章封面地址
    @TableField
    private String cover;

    //类型id
    @TableField
    private Long typeId;

    //专栏id
    @TableField
    private Long columnId;

}
