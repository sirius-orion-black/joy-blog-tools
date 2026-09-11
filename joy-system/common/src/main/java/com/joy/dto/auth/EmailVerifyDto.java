package com.joy.dto.auth;

import lombok.Data;

/**
 * 邮箱验证表单
 */
@Data
public class EmailVerifyDto {

    //用户名：长度必须在5-16个字符之间，且只能为大小写字母，唯一
    private String username;

    //邮箱
    private String email;

    //手机号码
    private String phone;

    //操作
    private String action = "verify";

}
