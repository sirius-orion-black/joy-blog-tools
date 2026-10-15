package com.joy.dto.common.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


/**
 * 用户登录
 */
@Data
public class LoginDto {

    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotBlank(message = "密码不能为空")
    private String password;

}
