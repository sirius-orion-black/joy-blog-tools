package com.joy.entity.sysConfig;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@TableName
public class SysMenu {

    //主键id
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    //菜单名称
    @TableField
    private String name;

    //icon
    @TableField
    private String icon;

    //权限标识符
    @TableField
    private String permission;

    //路由
    @TableField
    private String router;

    //组件
    @TableField
    private String component;

    //排序
    @TableField
    private Integer sort;

    //外链： 1 是 2 否
    @TableField
    private Integer isExternal;

    //父级id
    @TableField
    private Long parentId;

    //url
    @TableField
    private String path;

    //类型 1 directory 2 menu 3 permission
    @TableField
    private Integer type;

    //状态 1 正常 2 禁用
    @TableField
    private Integer state;

    //描述
    @TableField
    private String description;

    //创建时间
    @TableField
    private Date createTime;

    //更新时间
    @TableField
    private Date updateTime;

    @TableField(exist = false)
    private List<SysMenu> children;

}
