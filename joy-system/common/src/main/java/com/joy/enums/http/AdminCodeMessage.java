package com.joy.enums.http;

import com.joy.common.HttpCodeMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AdminCodeMessage implements HttpCodeMessage {
    INFORMATION_INCOMPLETE(HttpStatus.OK, "information_incomplete"), // 信息不完全
    ARTICLE_NOT_EXIST(HttpStatus.OK, "article_not_exist"), // 文章不存在
    INVALID_OPERATION(HttpStatus.OK, "invalid_operation"), // 无效操作
    ARTICLES_COLUMN_NOTHING(HttpStatus.OK, "articles_column_nothing"), // 文章栏目不存在
    USERNAME_PASSWORD_INCORRECT(HttpStatus.OK, "username_password_incorrect"),// 用户名或密码错误
    ACCOUNT_BANNED(HttpStatus.OK, "account_banned"), //账户被禁
    ACCOUNT_EMAIL_INCORRECT(HttpStatus.OK, "account_email_incorrect"), // 用户名或者邮箱不正确
    USER_INFO_CORRECT(HttpStatus.OK, "user_info_correct"), //用户信息不有误
    USERNAME_CANNOT_EMPTY(HttpStatus.OK, "username_cannot_empty"), // 用户名不能为空
    EMAIL_FORMAT_INCORRECT(HttpStatus.OK, "email_format_incorrect"), // 邮箱格式不正确
    ENABLE_CLOUD_EMAIL_CONFIG(HttpStatus.OK, "enable_cloud_email_config"), // 是否启用云邮箱未配置
    EMAIL_NOT_CONFIG(HttpStatus.OK, "email_not_config"), // email_not_config
    VERIFICATION_CODE_SENT_AGAIN(HttpStatus.OK, "verification_code_sent_again"), // 验证码发送失败，请重试
    VERIFICATION_CODE_SENT(HttpStatus.OK, "verification_code_sent"), // 验证码已发送
    VERIFICATION_CODE_EXPIRED(HttpStatus.OK, "verification_code_expired"), // 验证码已失效
    VERIFICATION_FAILED_PUZZLE_GAP(HttpStatus.OK, "verification_failed_puzzle_gap"), // 验证失败，请控制拼图对齐缺口
    FAILED_OBTAIN_PUZZLE_RESOURCES(HttpStatus.OK, "failed_obtain_puzzle_resources"), //获取拼图资源失败
    IMAGE_CONVERSION_BASE64_FAILED(HttpStatus.OK, "image_conversion_base64_failed"), // 图片资源转换BASE64失败
    MANY_VERIFICATION_ERRORS(HttpStatus.OK, "many_verification_errors_retrieve"), // 验证码错误次数过多，请重新获取
    VERIFICATION_CODE_SECONDS(HttpStatus.OK, "verification_code_seconds"), // 请{}秒后再获取验证码
    VERIFICATION_CODES_FREQUENTLY(HttpStatus.OK, "verification_codes_frequently"), // 该邮箱获取验证码过于频繁，请1小时后再试
    MAXIMUM_VERIFICATION_CODES_TODAY(HttpStatus.OK, "maximum_verification_codes_today"), // 该邮箱今日获取验证码次数已达上限，请明天再试
    NETWORK_REQUESTS_FREQUENT(HttpStatus.OK, "network_requests_frequent"), // 当前网络请求过于频繁，请稍后再试
    NETWORK_REQUESTS_FREQUENT_HOUR(HttpStatus.OK, "network_requests_frequent_hour"), // 当前网络请求过于频繁，请1小时后再试
    NETWORK_REQUESTS_FREQUENT_TODAY(HttpStatus.OK, "network_requests_frequent_today"), // 当前网络今日请求次数已达上限，请明天再试
    VERIFICATION_CODE_ATTEMPTS(HttpStatus.OK, "verification_code_attempts"), // 验证码错误，剩余尝试次数：{}

    USERNAME_ALREADY_EXISTS(HttpStatus.OK, "username_already_exists"), // 用户名已经存在
    EMAIL_ALREADY_EXISTS(HttpStatus.OK, "email_already_exists"), // 邮箱已经存在
    USERNAME_BETWEEN(HttpStatus.OK, "username_between_5_16"), //用户名为5-16个大小写字母
    EMAIL_CANNOT_EMPTY(HttpStatus.OK, "email_cannot_empty"), // 邮箱不能为空
    PHONE_CANNOT_EMPTY(HttpStatus.OK, "phone_cannot_empty"),// 手机号不能为空
    PHONE_NUMBER_INCORRECT(HttpStatus.OK, "phone_number_incorrect"),// 手机号格式不正确
    PASSWORD_NUMBER_INCORRECT(HttpStatus.OK, "password_number_incorrect"),// 密码格式不正确


    DEL_ARTICLES_COLUMNes(HttpStatus.BAD_REQUEST, "DEL_ARTICLES_COLUMNes"); // 删除文章栏目

    private final HttpStatus httpStatus;
    private final String message;

    AdminCodeMessage(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}