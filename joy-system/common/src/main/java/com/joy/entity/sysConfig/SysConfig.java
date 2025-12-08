package com.joy.entity.sysConfig;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_config")
public class SysConfig {

    //主键id
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    //配置名称
    @TableField
    private String configName;

    //配置类型
    @TableField
    private String configType;

    //配置的键
    @TableField
    private String configKey;

    //配置的值
    @TableField
    private String configValue;

    //排序
    @TableField
    private Integer configSort;

    //创建时间
    @TableField
    private Date createTime;

    //更新时间
    @TableField
    private Date updateTime;

}
