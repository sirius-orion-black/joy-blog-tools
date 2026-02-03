package com.joy.service;

import com.joy.common.Result;
import com.joy.entity.sysConfig.SysConfig;
import com.joy.entity.sysConfig.SysMenu;
import com.joy.entity.sysConfig.SysMenuIcon;

import java.util.List;

public interface SysMenuService {

    Result<List<SysMenu>> getMenu();

    Result<String> addMenu(SysMenu menu);

    Result<String> edit(SysMenu menu);

    Result<String> delMenu(List<Long> menuIds);

    Result<List<SysMenuIcon>> getIcons();

}
