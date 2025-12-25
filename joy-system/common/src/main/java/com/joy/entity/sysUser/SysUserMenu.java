package com.joy.entity.sysUser;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName
public class SysUserMenu {

    //后台用户id
    @TableId(value = "user_id",type = IdType.NONE)
    private Long userId;

    //后台菜单id
    @TableField
    private Long menuId;

}
