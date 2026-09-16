package com.joy.dto.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 微信用户绑定
 */
@Data
public class WxBindDto {

    @NotBlank(message = "微信token不能为空")
    private String wxToken;

    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotBlank(message = "code不能为空")
    private String code;

    @Email(message = "邮箱格式不正确")
    private String email;

    @NotNull(message = "时间不能为空")
    private Integer validTime;
}
