package com.joy.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joy.common.Result;
import com.joy.dto.sysUser.SysUserDto;
import com.joy.dto.sysUser.UserMenuDto;
import com.joy.entity.sysConfig.SysUserMenu;
import com.joy.entity.sysUser.SysUser;
import com.joy.mapper.sysConfig.SysUserMenuMapper;
import com.joy.mapper.sysUser.SysUserMapper;
import com.joy.service.SysUserService;
import com.joy.utils.UserVerifyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserMenuMapper sysUserMenuMapper;

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
        String strVerify = UserVerifyUtil.sysUserVerify(sysUser);
        if (strVerify.equals("success")) {//用户信息校验
            if (sysUserMapper.countByUsername(sysUser.getUsername()) > 0) {
                strVerify = "username_already_exists";//用户名已经存在
            }
            if (sysUserMapper.countByEmail(sysUser.getEmail()) > 0) {
                strVerify = "email_already_exists";//邮箱已经存在
            }
            sysUser.setState(6);

            String passwordStr = UserVerifyUtil.generatePassword();
            sysUser.setPassword(BCrypt.hashpw(passwordStr, BCrypt.gensalt()));
            Map<String, String> map = new HashMap<>();
            map.put("password", passwordStr);
            map.put("nickname", sysUser.getNickname());
            return this.save(sysUser) ? Result.success(map) : Result.badRequest(strVerify);
        }
        return Result.badRequest(strVerify);
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
        log.info("email-format====>" + !UserVerifyUtil.emailFormat(sysUser.getEmail()) + "," + StringUtils.isEmpty(sysUser.getEmail()));
        log.info("email-format====>" + !UserVerifyUtil.phoneFormat(sysUser.getPhone()) + "," + StringUtils.isEmpty(sysUser.getPhone()));
        if (!UserVerifyUtil.emailFormat(sysUser.getEmail()) || StringUtils.isEmpty(sysUser.getEmail()))
            return Result.badRequest("email_format_incorrect");
        else if (!UserVerifyUtil.phoneFormat(sysUser.getPhone()) || StringUtils.isEmpty(sysUser.getPhone()))
            return Result.badRequest("phone_number_incorrect");
        SysUser user = new SysUser();
        user.setId(sysUser.getId());
        user.setEmail(sysUser.getEmail());
        user.setPhone(sysUser.getPhone());
        return this.updateById(user) ? Result.success() : Result.internalServerError();

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
        query.in("user_id",userIds);
        sysUserMenuMapper.delete(query);
        return this.updateBatchById(users) ? Result.success() : Result.internalServerError();
    }

    /**
     * 解封拉黑管理人员
     *
     * @param users
     * @return
     */
    @Override
    public Result<String> bannedUser(List<SysUserDto> users) {
        log.info("banned user==========>" + JSON.toJSONString(users));
        //获取所有id放进集合
        List<Long> ids = users.stream()
                .map(SysUserDto::getId)
                .collect(Collectors.toList());
        List<SysUser> userList = this.listByIds(ids);
        log.info("user list==========>" + JSON.toJSONString(userList));
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

        return this.updateBatchById(userList) ? Result.success() : Result.internalServerError();
    }

    /**
     *获取管理人员权限
     * @param users
     * @return
     */
    @Override
    public Result<UserMenuDto> getUserMenu(UserMenuDto users) {
        QueryWrapper<SysUserMenu> query = new QueryWrapper<>();
        query.eq("user_id",users.getUserId());
        List<SysUserMenu> menus = sysUserMenuMapper.selectList(query);
        if(!menus.isEmpty()){
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
            return Result.badRequest();
        log.info("user menu==========>" + JSON.toJSONString(users));
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

}
