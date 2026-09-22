package com.joy.entity.admin.sysConfig;

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

    //主键id
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    //后台用户id
    @TableField
    private Long userId;

    //后台菜单id
    @TableField
    private Long menuId;

    public SysUserMenu(Long userId, Long menuId) {
        this.userId = userId;
        this.menuId = menuId;
    }

}
