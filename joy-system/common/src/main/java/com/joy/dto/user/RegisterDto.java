package com.joy.dto.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用户注册
 */
@Data
public class RegisterDto {

    @NotBlank(message = "手机号不能为空")
    private String phone;

    @Email(message = "邮箱格式不正确")
    private String email;

    @NotBlank(message = "验证码不能为空")
    private String code;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotNull(message = "时间不能为空")
    private Integer validTime;

}
