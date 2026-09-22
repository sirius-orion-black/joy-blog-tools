package com.joy.dto.common.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 重置密码
 */
@Data
public class ResetPasswordDto {

    @Email(message = "邮箱格式不正确")
    private String email;

    @NotBlank(message = "验证码不能为空")
    private String code;

    @NotBlank(message = "新密码不能为空")
    private String newPassword;

    @NotNull(message = "时间不能为空")
    private Integer validTime;

}
