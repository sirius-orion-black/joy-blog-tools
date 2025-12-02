package com.joy.dao.sysUser;


import lombok.Data;

import java.util.Date;

@Data
public class SysUserDao {

    //主键ID
    private Integer id;

    //用户名：16个字母以内
    private String username;

    //密码：由16个大小字母、数字和符号(? @ #)组中的三种成
    private String password;

    //用户名：16个字母以内
    private String email;

    //邮箱
    private String avatar;

    //性别：1 男，2 女，3 未知
    private Integer sex;

    //手机号码
    private String phone;

    //登录信息（IP 设备 区域等）
    private String loginInfo;

    //状态：1 正常，2 禁用，3  邮箱未验证
    private Integer state;

    //签名
    private String signature;

    //生日
    private Date birthday;

    //创建时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //邮箱验证时间
    private Date emailVerifiedTime;

}
