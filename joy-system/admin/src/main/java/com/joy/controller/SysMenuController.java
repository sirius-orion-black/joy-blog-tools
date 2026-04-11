package com.joy.controller;

import com.joy.common.Result;
import com.joy.config.apiPrefix.ApiPrefixAdminRestController;
import com.joy.entity.sysConfig.SysConfig;
import com.joy.entity.sysConfig.SysMenu;
import com.joy.entity.sysConfig.SysMenuIcon;
import com.joy.service.SysMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ApiPrefixAdminRestController
@RequestMapping("/menu")
@Slf4j
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 获取菜单
     * @return
     */
    @GetMapping("/getList")
    public Result<List<SysMenu>> getMenu(){
        return sysMenuService.getMenu();
    }

    /**
     * 新增menu
     * @param menu
     * @return
     */
    @PostMapping("/add")
    public Result<String> addMenu(@RequestBody SysMenu menu){
        return sysMenuService.addMenu(menu);
    }

    /**
     * 修改menu
     * @param menu
     * @return
     */
    @PostMapping("/edit")
    public Result<String> editMenu(@RequestBody SysMenu menu){
        return sysMenuService.edit(menu);
    }

    /**
     * 删除菜单
     * @return
     */
    @PostMapping("/delete")
    public Result<String> delMenu(@RequestBody List<Long> menuIds){
        return sysMenuService.delMenu(menuIds);
    }

    /**
     * 获取icons
     * @return
     */
    @GetMapping("/icons")
    public Result<List<SysMenuIcon>> getIcons(){
        return sysMenuService.getIcons();
    }
}
