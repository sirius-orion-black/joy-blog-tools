package com.joy.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName
public class User {

    //主键id
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    //用户名：长度必须在5-16个字符之间，且只能为大小写字母，唯一
    @TableField
    private String username;

    //邮箱
    @TableField
    private String email;

    //密码：由8-16个大写字母、小写字母、数字和符号(? @ #) 组成
    @TableField
    private String password;

    //昵称：长度30以内
    @TableField
    private String nickname;

}
