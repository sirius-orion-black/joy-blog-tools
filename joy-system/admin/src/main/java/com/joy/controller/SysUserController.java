package com.joy.controller;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joy.common.Result;
import com.joy.config.apiPrefix.ApiPrefixAdminRestController;
import com.joy.dto.sysUser.ChangePasswordDto;
import com.joy.dto.sysUser.SysLoginDto;
import com.joy.dto.sysUser.SysUserDto;
import com.joy.dto.sysUser.UserMenuDto;
import com.joy.entity.sysConfig.SysMenu;
import com.joy.entity.sysUser.SysUser;
import com.joy.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@ApiPrefixAdminRestController
@RequestMapping("/user")
@Slf4j
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 获取用户列表
     * @param userDto
     * @return
     */
    @GetMapping("/getList")
    public Result<Page<SysUser>> getUser(SysUserDto userDto){
        return sysUserService.getUser(userDto);
    }

    /**
     * 新增管理人员
     * @param sysUser
     * @return
     */
    @PostMapping("/add")
    public Result<Map<String,String>> addUser(@RequestBody SysUser sysUser){
        return sysUserService.addUser(sysUser);
    }

    /**
     * 编辑管理人员
     * @param sysUser
     * @return
     */
    @PostMapping("/edit")
    public Result<String> editUser(@RequestBody SysUser sysUser){
        return sysUserService.editUser(sysUser);
    }

    /**
     * 编辑管理人员
     * @param userIds
     * @return
     */
    @PostMapping("/delete")
    public Result<String> delUser(@RequestBody List<Long> userIds){
        return sysUserService.delUser(userIds);
    }

    /**
     * 解封拉黑管理人员
     * @param users
     * @return
     */
    @PostMapping("/isBanned")
    public Result<String> bannedUser(@RequestBody List<SysUserDto> users){
        return sysUserService.bannedUser(users);
    }

    /**
     * 获取管理人员权限
     * @param users
     * @return
     */
    @GetMapping("/getPermission")
    public Result<UserMenuDto> getUserMenu(UserMenuDto users){
        return sysUserService.getUserMenu(users);
    }

    /**
     * 编辑管理人员权限
     * @param users
     * @return
     */
    @PostMapping("/editPermission")
    public Result<String> editUserMenu(@RequestBody UserMenuDto users){
        return sysUserService.editUserMenu(users);
    }

    /**
     * 获取对应的用户菜单列表
     * @return
     */
    @GetMapping("/menuList")
    public Result<List<SysMenu>> menuList(){
        return sysUserService.menuList();
    }

    /**
     * 修改密码
     * @param user
     * @return
     */
    @PostMapping("/changePassword")
    public Result<String> changePassword(ChangePasswordDto user){
        return sysUserService.changePassword(user);
    }


}
