package com.joy.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName
public class User {

    //主键id
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    //邮箱
    @TableField
    private String email;

    //密码：由8-16个大写字母、小写字母、数字和符号(? @ #) 组成
    @TableField
    private String password;

    //昵称：长度30以内
    @TableField
    private String nickname;

    //头像URL
    @TableField
    private String avatar;

    //性别：1 男，2 女，3 未知
    @TableField
    private Integer sex;

    //生日
    @TableField
    private Date birthday;

    //签名
    @TableField
    private String signature;

    //手机号
    @TableField
    private String phone;

    //微信号
    @TableField
    private String wechat;

    //QQ号
    @TableField
    private String qq;

    //状态：1 正常，2 冻结，3 封号，4 注销，5 邮箱或手机号码未验证
    @TableField
    private Integer state;

    //创建时间
    @TableField
    private Date createTime;

    //更新时间
    @TableField
    private Date updateTime;

    //邮箱验证时间
    @TableField
    private Date emailVerifiedTime;

    //手机号验证时间
    @TableField
    private Date phoneVerifiedTime;

    //文章数量
    @TableField
    private Long post_count;

    //评论数量
    @TableField
    private Long comment_count;

    //获赞数量
    @TableField
    private Long like_count;

}
