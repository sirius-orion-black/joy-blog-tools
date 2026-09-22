package com.joy.entity.admin.sysConfig;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName
public class SysCloudCredential {

    //主键id
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    //业务标识: ID_PHOTO / ALI_MAIL / SMS / OSS
    @TableField
    private String bizType;

    //云厂商: ALIYUN / TENCENT / HUAWEI
    @TableField
    private String provider;

    //AccessKeyId
    @TableField
    private String accessKeyId;

    //AccessKeySecret
    @TableField
    private String accessKeySecret;

    //扩展配置(如OSS的bucket/endpoint, 邮箱的smtp地址等)
    @TableField
    private String extraConfig; // JSON字符串

    //1 启用 0 禁用
    @TableField
    private Integer state;
}
