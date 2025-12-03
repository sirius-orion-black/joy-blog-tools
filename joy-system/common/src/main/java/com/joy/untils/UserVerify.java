package com.joy.untils;

import com.joy.entity.sysUser.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

@Slf4j
public class UserVerify {

    /**
     * 用户名校验，5-16个字符之间，且只能为大小写字母
     * @param str
     * @return
     */
    private static boolean usernameFormat(String str) {
        String usernameRegex = "^[A-Za-z]{5,16}$";
        return Pattern.matches(usernameRegex, str);
    }
    /**
     * 邮箱校验
     *
     * @param str
     * @return
     */
    private static boolean emailFormat(String str) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        String qqEmailRegex = "^[a-zA-Z0-9_+&*-]+@qq\\.com$";
        return Pattern.matches(emailRegex, str) || Pattern.matches(qqEmailRegex, str);
    }
    /**
     * 密码校验，由8-16个大写字母、小写字母、数字和符号(? @ #) 组成
     * @param str
     * @return
     */
    private static boolean passwordFormat(String str) {
        String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[?@#]).{8,16}$";
        return Pattern.matches(passwordRegex, str);
    }
    /**
     * 手机号校验
     * @param str
     * @return
     */
    private static boolean phoneFormat(String str) {
        String phoneRegex = "^1[3-9]\\d{9}$";
        return Pattern.matches(phoneRegex, str);
    }
    /**
     * 后台用户新增信息校验
     * @param sysUser
     * @return
     */
    public static String sysUserVerify(SysUser sysUser) {
        String str = "success";
        if (StringUtils.isEmpty(sysUser.getUsername())) {
            return "username_cannot_empty";//用户名不能为空
        }
        if (!usernameFormat(sysUser.getUsername())) {
            return "username_between_5_16";//用户名长度必须在5-16个字符之间
        }
        if (StringUtils.isEmpty(sysUser.getEmail())) {
            return "email_cannot_empty";//邮箱不能为空
        }
        if (!emailFormat(sysUser.getEmail())) {
            return "email_format_incorrect";//邮箱格式不正确
        }
        if (StringUtils.isEmpty(sysUser.getPassword())) {
            return "password_cannot_empty";//密码不能为空
        }
        if (!passwordFormat(sysUser.getPassword())) {
            return "password_number_incorrect";//密码格式不正确
        }
        if (StringUtils.isEmpty(sysUser.getPhone())) {
            return "phone_cannot_empty";//手机号不能为空
        }
        if (!phoneFormat(sysUser.getPhone())) {
            return "phone_number_incorrect";//手机号格式不正确
        }
        return str;
    }

}
