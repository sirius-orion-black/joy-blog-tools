package com.joy.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joy.common.Result;
import com.joy.entity.sysConfig.SysConfig;
import com.joy.entity.sysConfig.SysMenu;
import com.joy.entity.sysConfig.SysMenuIcon;
import com.joy.mapper.sysConfig.SysConfigMapper;
import com.joy.mapper.sysConfig.SysMenuIconMapper;
import com.joy.mapper.sysConfig.SysMenuMapper;
import com.joy.service.SysMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Autowired
    private SysMenuIconMapper sysMenuIconMapper;

    /**
     * 获取菜单
     *
     * @return
     */
    @Override
    public Result<List<SysMenu>> getMenu() {
        // 1. 获取并排序菜单列表
        List<SysMenu> sortedMenus = this.list(new LambdaQueryWrapper<SysMenu>()
                .orderByAsc(SysMenu::getSort).orderByAsc(SysMenu::getId));

        // 2. 构建子菜单映射
        Map<Long, List<SysMenu>> childrenMap = sortedMenus.stream()
                .filter(menu -> menu.getParentId() != 0) // 过滤非根菜单
                .collect(Collectors.groupingBy(SysMenu::getParentId)); // 按父ID分组

        // 3. 设置子菜单引用
        sortedMenus.forEach(menu -> menu.setChildren(childrenMap.get(menu.getId())));
//        sortedMenus.forEach(menu -> menu.setChildren(childrenMap.getOrDefault(menu.getId(), Collections.emptyList())));

        // 4. 提取并返回根菜单
        return Result.success(sortedMenus.stream()
                .filter(menu -> menu.getParentId() == 0) // 过滤根菜单
                .collect(Collectors.toList()));
    }

    /**
     * 新增menu
     *
     * @param menu
     * @return
     */
    @Override
    public Result<String> addMenu(SysMenu menu) {
        if (menu.getId() == null || menu.getId().equals(0L)) {
            menu.setId(null);
            if (menu.getType().equals(2))
                menu.setPermission("query");
            else {
                menu.setComponent("");
                menu.setPath("");
                menu.setRouter("");
            }
            return this.save(menu) ? Result.success() : Result.internalServerError();
        } else
            return Result.badRequest();

    }

    /**
     * 修改menu
     *
     * @param menu
     * @return
     */
    @Override
    public Result<String> edit(SysMenu menu) {
        if (menu.getId() != null && !menu.getId().equals(0L)) {
            menu.setUpdateTime(new Date());
            if (menu.getType().equals(2))
                menu.setPermission("query");
            else {
                menu.setComponent("");
                menu.setPath("");
                menu.setRouter("");
            }
            this.updateById(menu);
            return this.updateById(menu) ? Result.success() : Result.internalServerError();
        } else
            return Result.badRequest();
    }

    /**
     * 删除菜单
     *
     * @param menuIds
     * @return
     */
    @Override
    public Result<String> delMenu(List<Long> menuIds) {
        if (menuIds.isEmpty())
            Result.success();
        List<SysMenu> menus = this.list();
        //构建快速索引
        Map<Long, SysMenu> menuMap = menus.stream()
                .collect(Collectors.toMap(SysMenu::getId, m -> m));
        //去除顶级菜单
        Map<Long, List<Long>> parentChildMap = menus.stream()
                .filter(m -> m.getParentId() != 0)
                .collect(Collectors.groupingBy(
                        SysMenu::getParentId,
                        Collectors.mapping(SysMenu::getId, Collectors.toList())
                ));
        //获取所有要删除的menu
        Set<Long> allIds = new HashSet<>();
        Queue<Long> queue = new LinkedList<>(menuIds);
        while (!queue.isEmpty()) {
            Long id = queue.poll();
            allIds.add(id);
            // 添加所有子节点到队列
            if (parentChildMap.containsKey(id)) {
                queue.addAll(parentChildMap.get(id));
            }
        }
        log.info(JSON.toJSONString(menuIds) + "menu=============>" + JSON.toJSONString(allIds));
        return this.removeBatchByIds(new ArrayList<>(allIds)) ? Result.success() : Result.internalServerError();
    }

    /**
     * 获取icons
     *
     * @return
     */
    @Override
    public Result<List<SysMenuIcon>> getIcons() {
        QueryWrapper<SysMenuIcon> query = new QueryWrapper<>();
        List<SysMenuIcon> list = sysMenuIconMapper.selectList(query);
        return Result.success(list);
    }

}
