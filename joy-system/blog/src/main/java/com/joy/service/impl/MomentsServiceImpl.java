package com.joy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joy.common.Result;
import com.joy.entity.content.ContentMoments;
import com.joy.entity.sysUser.SysUser;
import com.joy.entity.user.User;
import com.joy.mapper.content.ContentMomentsMapper;
import com.joy.mapper.sysUser.SysUserMapper;
import com.joy.mapper.user.UserMapper;
import com.joy.service.MomentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MomentsServiceImpl extends ServiceImpl<ContentMomentsMapper,ContentMoments> implements MomentsService {

    @Autowired
    private ContentMomentsMapper momentsMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 获取朋友圈列表
     * @return 返回的数据
     */
    @Override
    public List<ContentMoments> moments() {
        List<ContentMoments> moments = momentsMapper.selectMomentsPublishPublicly();
        // 去除重复用户ID的分组结果
        Map<Integer, Set<Long>> ids = moments.stream()
                .collect(Collectors.groupingBy(
                        ContentMoments::getUserSource,
                        Collectors.mapping(
                                ContentMoments::getUserId,
                                Collectors.toSet() // 先转换为Set去除重复
                        )
                ));
        // 如果需要保持List结构，可以再转换回List
        Map<Integer, List<Long>> idsList = ids.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> new ArrayList<>(entry.getValue())
                ));

        //获取用户
        List<User> user = new ArrayList<>();
        if (idsList.containsKey(1) && !idsList.get(1).isEmpty())
            user = userMapper.selectBatchIds(idsList.get(1));
        Map<Long, User> userMap = user.stream().collect(Collectors.toMap(User::getId, m -> m));
        List<SysUser> sysUser = new ArrayList<>();
        if (idsList.containsKey(2) && !idsList.get(2).isEmpty())
            sysUser = sysUserMapper.selectBatchIds(idsList.get(2));
        Map<Long, SysUser> sysUserMap = sysUser.stream().collect(Collectors.toMap(SysUser::getId, m -> m));

        moments.forEach(tag -> {
            //存储用户名
            if (tag.getUserSource() == 1) {
                tag.setUserName(userMap.get(tag.getUserId()).getNickname());
                tag.setUserAvatar(userMap.get(tag.getUserId()).getAvatar());
            } else {
                tag.setUserName(sysUserMap.get(tag.getUserId()).getNickname());
                tag.setUserAvatar(sysUserMap.get(tag.getUserId()).getAvatar());
            }
        });
        return moments;
    }
}
