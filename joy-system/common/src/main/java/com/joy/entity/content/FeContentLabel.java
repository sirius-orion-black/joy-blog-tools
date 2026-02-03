package com.joy.entity.content;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName
public class FeContentLabel {

    //主键id
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    //标签名称
    @TableField
    private String name;

    //1 正常 2 禁用 3 删除
    @TableField
    private Integer state;

    //1 博客 2 说说
    @TableField
    private Integer type;

    //描述
    @TableField
    private String description;

    //创建时间
    @TableField
    private Date createTime;
}
