package com.joy.entity.content;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class ContentColumn {

    //主键id
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    //专栏名称
    @TableField
    private String name;

    //状态: 1 上架 2 审核 3 已下线 4 删除
    @TableField
    private Integer state;

    //专栏封面
    @TableField
    private String cover;

    //专栏简介
    @TableField
    private String introduction;

    //用户id
    @TableField
    private Long userId;

    //创建时间
    @TableField
    private Date createTime;

    //更新时间
    @TableField
    private Date updateTime;

}
