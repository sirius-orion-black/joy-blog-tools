package com.joy.entity.files;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName
public class FeFilesRecord {

    //主键id
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    //文件地址
    @TableField
    private String url;

    //文件名称
    @TableField
    private String storedName;

    //文件原始名称
    @TableField
    private String originalName;

    //文件在服务器或云存储中的完整路径
    @TableField
    private String path;

    //扩展名
    @TableField
    private String fileExt;

    //文件大小（单位：字节），用于验证和统计
    @TableField
    private Long fileSize;

    //关联上传用户的ID
    @TableField
    private Long userId;

    //上传ID，仅在分片上传使用
    @TableField
    private String uploadId;

    //1 后台管理 2 web端 3 微信小程序 5 支付宝小程序 6 app
    @TableField
    private Integer userPlatform;

    //类型: cover 封面
    @TableField
    private String fileType;

    //创建时间
    @TableField
    private Date createTime;
}
