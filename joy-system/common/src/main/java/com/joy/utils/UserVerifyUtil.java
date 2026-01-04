package com.joy.utils;

import com.joy.entity.sysUser.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class UserVerifyUtil {

    /**
     * 用户名校验，5-16个字符之间，且只能为大小写字母
     * @param str
     * @return
     */
    public static boolean usernameFormat(String str) {
        String usernameRegex = "^[A-Za-z]{5,16}$";
        return Pattern.matches(usernameRegex, str);
    }
    /**
     * 邮箱校验
     *
     * @param str
     * @return
     */
    public static boolean emailFormat(String str) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        String qqEmailRegex = "^[a-zA-Z0-9_+&*-]+@qq\\.com$";
        return Pattern.matches(emailRegex, str) || Pattern.matches(qqEmailRegex, str);
    }
    /**
     * 密码校验，由8-16个大写字母、小写字母、数字和符号(? @ #) 组成
     * @param str
     * @return
     */
    public static boolean passwordFormat(String str) {
        String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[?@#]).{8,16}$";
        return Pattern.matches(passwordRegex, str);
    }
    /**
     * 手机号校验
     * @param str
     * @return
     */
    public static boolean phoneFormat(String str) {
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
            return "username_between_5_16";//用户名为5-16个大小写字母
        }
        if (StringUtils.isEmpty(sysUser.getEmail())) {
            return "email_cannot_empty";//邮箱不能为空
        }
        if (!emailFormat(sysUser.getEmail())) {
            return "email_format_incorrect";//邮箱格式不正确
        }
        if (StringUtils.isEmpty(sysUser.getPhone())) {
            return "phone_cannot_empty";//手机号不能为空
        }
        if (!phoneFormat(sysUser.getPhone())) {
            return "phone_number_incorrect";//手机号格式不正确
        }
        return str;
    }

    public static String generatePassword(){
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String symbols = "?@#";
        String allChars = upper + lower + digits + symbols;
        SecureRandom RANDOM = new SecureRandom();
        // 生成长度：8-16位
        int length = RANDOM.nextInt(9) + 8;
        // 确保每类字符至少出现一次
        String upperChar = String.valueOf(upper.charAt(RANDOM.nextInt(upper.length())));
        String lowerChar = String.valueOf(lower.charAt(RANDOM.nextInt(lower.length())));
        String digitChar = String.valueOf(digits.charAt(RANDOM.nextInt(digits.length())));
        String symbolChar = String.valueOf(symbols.charAt(RANDOM.nextInt(symbols.length())));

        // 生成剩余随机字符
        StringBuilder randomStr = new StringBuilder();
        for (int i = 0; i < length - 4; i++) {
            randomStr.append(allChars.charAt(RANDOM.nextInt(allChars.length())));
        }

        // 合并并打乱顺序
        String combined = upperChar + lowerChar + digitChar + symbolChar + randomStr;
        List<Character> chars = combined.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(chars);

        return chars.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

}
