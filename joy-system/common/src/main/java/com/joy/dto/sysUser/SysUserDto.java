package com.joy.dto.sysUser;

import lombok.Data;

/**
 * 查询用户列表表单
 */
@Data
public class SysUserDto {

    //id
    private Long id;

    //用户名
    private String username;

    //状态
    private Integer state;

    //邮箱
    private String email;

    //手机号
    private String phone;

    //当前页
    private Integer page;

    //条数
    private Integer size;

}
