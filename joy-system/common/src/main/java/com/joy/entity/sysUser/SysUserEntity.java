package com.joy.entity.sysUser;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_user")
public class SysUserEntity {

    //主键ID
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    //用户名：16个字母以内
    @TableField
    private String username;

    //密码：由16个大小字母、数字和符号(? @ #)组中的三种成
    @TableField
    private String password;

    //用户名：16个字母以内
    @TableField
    private String email;

    //邮箱
    @TableField
    private String avatar;

    //性别：1 男，2 女，3 未知
    @TableField
    private Integer sex;

    //手机号码
    @TableField
    private String phone;

    //登录信息（IP 设备 区域等）
    @TableField
    private String loginInfo;

    //状态：1 正常，2 禁用，3  邮箱未验证
    @TableField
    private Integer state;

    //签名
    @TableField
    private String signature;

    //生日
    @TableField
    private Date birthday;

    //创建时间
    @TableField
    private Date createTime;

    //更新时间
    @TableField
    private Date updateTime;

    //邮箱验证时间
    @TableField
    private Date emailVerifiedTime;

}