package com.joy.dto.sysUser;


import lombok.Data;


@Data
public class SysLoginDto {
    //用户名：长度必须在5-16个字符之间，且只能为大小写字母，唯一
    private String username;

    //密码：由8-16个大写字母、小写字母、数字和符号(? @ #) 中至少三种
    private String password;

    //邮箱
    private String email;

    //手机号码
    private String phone;

    //登录信息（IP 设备 区域等）
    private String loginInfo;

    //类型：1 密码登录，2 邮箱登录
    private Integer loginType;

    //移动的距离
    private Integer move;

    //验证码key
    private String nonceStr;

    //记住我
    private Boolean remember;
}
