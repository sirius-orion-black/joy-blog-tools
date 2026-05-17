package com.joy.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joy.common.Result;
import com.joy.dto.sysUser.ChangePasswordDto;
import com.joy.dto.sysUser.SysUserDto;
import com.joy.dto.sysUser.UserMenuDto;
import com.joy.entity.sysConfig.SysMenu;
import com.joy.entity.sysConfig.SysUserMenu;
import com.joy.entity.sysUser.SysUser;
import com.joy.enums.http.AdminCodeMessage;
import com.joy.enums.http.CommonCodeMessage;
import com.joy.mapper.sysConfig.SysMenuMapper;
import com.joy.mapper.sysConfig.SysUserMenuMapper;
import com.joy.mapper.sysUser.SysUserMapper;
import com.joy.service.SysUserService;
import com.joy.utils.UserVerifyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserMenuMapper sysUserMenuMapper;

    @Autowired
    private SysMenuMapper sysMenuMapper;


    /**
     * 获取用户列表
     *
     * @param userDto
     * @return
     */
    @Override
    public Result<Page<SysUser>> getUser(SysUserDto userDto) {

        Page<SysUser> page = new Page<>(userDto.getPage(), userDto.getSize());

        QueryWrapper<SysUser> query = new QueryWrapper<>();

        if (!StringUtils.isEmpty(userDto.getUsername()))
            query.like("username", userDto.getUsername());
        if (!StringUtils.isEmpty(userDto.getPhone()))
            query.like("phone", userDto.getPhone());
        if (!StringUtils.isEmpty(userDto.getEmail()))
            query.like("email", userDto.getEmail());
        if (userDto.getState() != null)
            query.eq("state", userDto.getState());
        query.ne("state", 5);

        Page<SysUser> list = this.page(page, query);
        List<SysUser> records = list.getRecords();
        list.setRecords(records.stream()
                .peek(e -> e.setPassword(null)) // 仅清空目标字段
                .collect(Collectors.toList()));
        return Result.success(list);
    }

    /**
     * 新增管理人员
     *
     * @param sysUser
     * @return
     */
    @Override
    public Result<Map<String, String>> addUser(SysUser sysUser) {
        UserVerifyUtil.sysUserVerify(sysUser);//用户信息校验
        if (sysUserMapper.countByUsername(sysUser.getUsername()) > 0) {
            AdminCodeMessage.USERNAME_ALREADY_EXISTS.throwIt();//用户名已经存在
        }
        if (sysUserMapper.countByEmail(sysUser.getEmail()) > 0) {
            AdminCodeMessage.EMAIL_ALREADY_EXISTS.throwIt();//邮箱已经存在
        }
        sysUser.setState(6);

        String passwordStr = UserVerifyUtil.generatePassword();
        sysUser.setPassword(BCrypt.hashpw(passwordStr, BCrypt.gensalt()));
        Map<String, String> map = new HashMap<>();
        map.put("password", passwordStr);
        map.put("nickname", sysUser.getNickname());
        return this.save(sysUser) ? Result.success(map) : Result.fail(CommonCodeMessage.INTERNAL_SERVER_ERROR.getHttpStatus());
    }

    /**
     * 编辑管理人员
     *
     * @param sysUser
     * @return
     */
    @Override
    public Result<String> editUser(SysUser sysUser) {
        //格式校验
        if (!UserVerifyUtil.emailFormat(sysUser.getEmail()) || StringUtils.isEmpty(sysUser.getEmail()))
            AdminCodeMessage.EMAIL_FORMAT_INCORRECT.throwIt();
        else if (!UserVerifyUtil.phoneFormat(sysUser.getPhone()) || StringUtils.isEmpty(sysUser.getPhone()))
            AdminCodeMessage.PHONE_NUMBER_INCORRECT.throwIt();
        SysUser user = new SysUser();
        user.setId(sysUser.getId());
        user.setEmail(sysUser.getEmail());
        user.setPhone(sysUser.getPhone());
        return this.updateById(user) ? Result.success() : Result.fail(CommonCodeMessage.INTERNAL_SERVER_ERROR.getHttpStatus());

    }

    /**
     * 删除管理人员
     *
     * @param userIds
     * @return
     */
    @Override
    public Result<String> delUser(List<Long> userIds) {
        if (userIds.isEmpty())
            return Result.success();
        List<SysUser> users = new ArrayList<>();
        userIds.forEach(m -> {
            SysUser user = new SysUser();
            user.setId(m);
            user.setState(5);
            users.add(user);
        });
        //删除所有权限
        QueryWrapper<SysUserMenu> query = new QueryWrapper<>();
        query.in("user_id", userIds);
        sysUserMenuMapper.delete(query);
        return this.updateBatchById(users) ? Result.success() : Result.fail(CommonCodeMessage.INTERNAL_SERVER_ERROR.getHttpStatus());
    }

    /**
     * 解封拉黑管理人员
     *
     * @param users
     * @return
     */
    @Override
    public Result<String> bannedUser(List<SysUserDto> users) {
        log.info("banned user==========>{}", JSON.toJSONString(users));
        //获取所有id放进集合
        List<Long> ids = users.stream()
                .map(SysUserDto::getId)
                .collect(Collectors.toList());
        List<SysUser> userList = this.listByIds(ids);
        log.info("user list==========>{}", JSON.toJSONString(userList));
        //将List dto转换为map，，通过id可以快速查找，提高效率
        Map<Long, SysUserDto> userMap = users.stream().collect(Collectors.toMap(SysUserDto::getId, m -> m));
        //遍历数据并进行更新
        for (SysUser user : userList) {
            SysUserDto dto = userMap.get(user.getId());
            if (dto.getState().equals(2))
                user.setState(2);
            else if (dto.getState().equals(1)) {
                if (user.getEmailVerifiedTime() == null && user.getPhoneVerifiedTime() == null)
                    user.setState(3);
                else
                    user.setState(1);
            }
        }

        return this.updateBatchById(userList) ? Result.success() : Result.fail(CommonCodeMessage.INTERNAL_SERVER_ERROR.getHttpStatus());
    }

    /**
     * 获取管理人员权限
     *
     * @param users
     * @return
     */
    @Override
    public Result<UserMenuDto> getUserMenu(UserMenuDto users) {
        QueryWrapper<SysUserMenu> query = new QueryWrapper<>();
        query.eq("user_id", users.getUserId());
        List<SysUserMenu> menus = sysUserMenuMapper.selectList(query);
        if (!menus.isEmpty()) {
            List<Long> menuIds = menus.stream().map(SysUserMenu::getMenuId).collect(Collectors.toList());
            users.setMenuId(menuIds);
        }
        return Result.success(users);
    }

    /**
     * 编辑管理人员权限
     *
     * @param users
     * @return
     */
    @Override
    public Result<String> editUserMenu(UserMenuDto users) {
        Long userId = users.getUserId();
        List<Long> menuIds = users.getMenuId();
        if (userId == null || menuIds.isEmpty())
            CommonCodeMessage.BAD_REQUEST.throwIt();
        log.info("user menu==========>{}", JSON.toJSONString(users));
        // 删除用户原有权限
        QueryWrapper<SysUserMenu> query = new QueryWrapper<>();
        query.eq("user_id", users.getUserId());
        sysUserMenuMapper.delete(query);
        //  插入新权限 (批量操作)
        List<SysUserMenu> newRelations = menuIds.stream()
                .map(menuId -> new SysUserMenu(userId, menuId))
                .collect(Collectors.toList());
        sysUserMenuMapper.insertBatch(newRelations);
        return Result.success();
    }

    /**
     * 获取对应的用户菜单列表
     *
     * @return
     */
    @Override
    public Result<List<SysMenu>> menuList() {
        // 获取用户菜单id
        long userId = StpUtil.getLoginIdAsLong();
        List<SysUserMenu> userMenus = sysUserMenuMapper.selectList(new LambdaQueryWrapper<SysUserMenu>().eq(SysUserMenu::getUserId, userId));
        List<Long> menuIds = userMenus.stream().map(SysUserMenu::getMenuId).collect(Collectors.toList());
        // 获取用户menu并排序
        List<SysMenu> menus = sysMenuMapper.selectList(
                new LambdaQueryWrapper<SysMenu>()
                        .in(SysMenu::getId, menuIds)
                        .orderByAsc(SysMenu::getSort)
                        .orderByAsc(SysMenu::getId)
        );

        // 1. 一次性构建菜单映射和子菜单关系
        Map<Long, SysMenu> menuMap = menus.stream()
                .collect(Collectors.toMap(SysMenu::getId, Function.identity()));

        Map<Long, List<SysMenu>> childrenMap = menus.stream()
                .filter(menu -> menu.getParentId() != 0)
                .collect(Collectors.groupingBy(SysMenu::getParentId));

        // 2. 单次遍历完成权限设置和子菜单关联
        menus.forEach(menu -> {
            // 设置按钮权限标识（type=3）
            if (menu.getType() == 3) {
                SysMenu parent = menuMap.get(menu.getParentId());
                menu.setPermission(parent != null ? "permission:" + parent.getName() + ":" + menu.getPermission() : menu.getPermission());
            }
            // 关联子菜单
            menu.setChildren(childrenMap.getOrDefault(menu.getId(), Collections.emptyList()));
        });

        // 3. 直接返回根菜单
        return Result.success(
                menus.stream()
                        .filter(menu -> menu.getParentId() == 0)
                        .collect(Collectors.toList())
        );

    }

    /**
     * 修改密码
     *
     * @param user
     * @return
     */
    @Override
    public Result<String> changePassword(ChangePasswordDto user) {
        if (!UserVerifyUtil.passwordFormat(user.getPassword()))
            AdminCodeMessage.PASSWORD_NUMBER_INCORRECT.throwIt();
        long userId = StpUtil.getLoginIdAsLong();
        SysUser info = this.getById(userId);
        if (!BCrypt.checkpw(user.getOldPassword(), info.getPassword()))
            AdminCodeMessage.USERNAME_PASSWORD_INCORRECT.throwIt();
        info.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        info.setUpdateTime(new Date());
        this.updateById(info);
        return Result.success();
    }

}
