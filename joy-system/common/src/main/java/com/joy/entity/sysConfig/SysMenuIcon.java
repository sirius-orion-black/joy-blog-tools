package com.joy.entity.sysConfig;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName
public class SysMenuIcon {

    //主键id
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    //icon 名称
    @TableField
    private String name;

    //icon key 值
    @TableField
    private String iconKey;

    //排序
    @TableField
    private Integer sort;

    //创建时间
    @TableField
    private Date createTime;
}
