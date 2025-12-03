package com.joy.dao.sysUser;


import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class SysUserDao {

    //主键ID
    private Integer id;

    //用户名：长度必须在5-16个字符之间，且只能为大小写字母，唯一
    @NotBlank(message = "username_cannot_empty")//用户名不能为空
    @Size(min = 3, max = 20, message = "username_between_3_20")//用户名长度必须在3-20个字符之间
    private String username;

    //密码：由8-16个大写字母、小写字母、数字和符号(? @ #) 中至少三种
    @NotBlank(message = "password_cannot_empty")//密码不能为空
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[?@#]).{8,16}$", message = "phone_number_incorrect")//手机号格式不正确
    private String password;

    //邮箱
    @NotBlank(message = "mailbox_cannot_empty")//邮箱不能为空
    @Email(message = "email_format_incorrect")//邮箱格式不正确
    private String email;

    //头像URL
    private String avatar;

    //性别：1 男，2 女，3 未知
    private Integer sex;

    //手机号码
    @NotBlank(message = "phone_cannot_empty")//邮箱不能为空
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "phone_number_incorrect")//手机号格式不正确
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
