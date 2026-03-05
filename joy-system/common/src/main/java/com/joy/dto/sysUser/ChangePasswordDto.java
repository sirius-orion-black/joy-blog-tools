package com.joy.dto.sysUser;

import lombok.Data;

@Data
public class ChangePasswordDto {

    //旧密码
    private String oldPassword;

    //密码：由8-16个大写字母、小写字母、数字和符号(? @ #) 中至少三种
    private String password;

}
