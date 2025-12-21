package com.joy.dto.sysUser;

import lombok.Data;

import java.util.Date;

@Data
public class SysUserInfoDto {

    //用户名：长度必须在5-16个字符之间，且只能为大小写字母，唯一
    private String username;

    //邮箱
    private String email;

    //手机号码
    private String phone;

    //昵称
    private String nickname;

    //头像
    private String avatar;

    //登录IP
    private String ip;

    //登录设备
    private String device;

    //登录区域
    private String region;

    //状态：1 正常，2 禁用，3  邮箱未验证
    private Integer state;

    //签名
    private String signature;

    //生日
    private Date birthday;

    //token
    private String token;

    //创建时间
    private Date createTime;

}
