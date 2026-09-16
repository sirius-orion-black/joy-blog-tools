package com.joy.dto.user;

import lombok.Data;


/**
 * 用户信息
 */
@Data
public class UserInfoDto {
    //token
    private String token;

    //id
    private Long id;

    //昵称
    private String nickname;

    //头像
    private String avatar;

    //手机号
    private String phone;

    //邮箱
    private String email;

    //性别
    private Integer sex;

    //状态
    private Integer state;

    //需要绑定
    private Boolean needBind = false;
}
