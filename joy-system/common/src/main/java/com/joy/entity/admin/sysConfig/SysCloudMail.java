package com.joy.entity.admin.sysConfig;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName
public class SysCloudMail {

    //主键id
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    //发信地址
    @TableField
    private String accountName;

    //发信人昵称
    @TableField
    private String fromAlias;

    //云模板ID
    @TableField
    private String templateCode;

    //状态：1启用 2禁用
    @TableField
    private Integer state;

    //有效时间，单位秒
    @TableField
    private Integer validTime;

}
