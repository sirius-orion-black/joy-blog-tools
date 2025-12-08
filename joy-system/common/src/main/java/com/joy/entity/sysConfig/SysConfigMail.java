package com.joy.entity.sysConfig;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_config_mail")
public class SysConfigMail {

    //主键id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //SMTP服务器地址
    @TableField
    private String host;

    //SMTP端口
    @TableField
    private Integer port;

    //邮箱账号
    @TableField
    private String username;

    //邮箱密码或授权码
    @TableField
    private String password;

    //协议类型
    @TableField
    private String protocol;

    //身份验证
    @TableField
    private String smtpAuth;

    //SSL加密
    @TableField
    private String smtpSslEnable;

    //TLS加密
    @TableField
    private String smtpStarttlsEnable;

    //调试模式，生产环境可设为false
    @TableField
    private String debug;

    //连接超时时间（毫秒）
    @TableField
    private String connectionTimeout;

    //取超时时间（毫秒）
    @TableField
    private String timeout;

    //写入超时时间（毫秒）
    @TableField
    private String writeTimeout;

    //邮箱状态： 1 启用，2 关闭，3删除
    @TableField
    private Integer state;

    //创建时间
    @TableField
    private Date createTime;

    //更新时间
    @TableField
    private Date updateTime;

}
