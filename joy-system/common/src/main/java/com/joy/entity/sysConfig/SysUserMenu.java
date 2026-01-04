package com.joy.entity.sysConfig;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName
public class SysUserMenu {

    //后台用户id
//    @TableId(value = "user_id",type = IdType.NONE)
    @TableField
    private Long userId;

    //后台菜单id
    @TableField
    private Long menuId;

}
