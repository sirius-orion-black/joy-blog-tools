package com.joy.mapper.sysConfig;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.joy.entity.sysConfig.SysUserMenu;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMenuMapper extends BaseMapper<SysUserMenu> {

        // 批量插入方法
        @Insert("<script>" +
                "INSERT INTO sys_user_menu(user_id, menu_id) VALUES " +
                "<foreach item='item' collection='list' separator=','>" +
                "(#{item.userId}, #{item.menuId})" +
                "</foreach>" +
                "</script>")
        void insertBatch(@Param("list") List<SysUserMenu> relations);

}
