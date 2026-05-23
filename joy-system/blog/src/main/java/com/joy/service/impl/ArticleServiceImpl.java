package com.joy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joy.entity.content.ContentBlogpost;
import com.joy.entity.sysUser.SysUser;
import com.joy.entity.user.User;
import com.joy.mapper.content.ContentBlogpostMapper;
import com.joy.mapper.sysUser.SysUserMapper;
import com.joy.mapper.user.UserMapper;
import com.joy.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ContentBlogpostMapper,ContentBlogpost> implements ArticleService {

    @Autowired
    private ContentBlogpostMapper blogpostMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 获取文章列表
     * @return 返回数据
     */
    @Override
    public List<ContentBlogpost> getBlogpost(Long id) {
        log.info("======文章ID：{}",id);
        List<ContentBlogpost> blog = blogpostMapper.selectBlogpostPublishPublicly(id);
        // 去除重复用户ID的分组结果
        Map<Integer, Set<Long>> ids = blog.stream()
                .collect(Collectors.groupingBy(
                        ContentBlogpost::getUserSource,
                        Collectors.mapping(
                                ContentBlogpost::getUserId,
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

        blog.forEach(tag -> {
            // 存储标签ID的列表
            List<Long> labelIds = new ArrayList<>();
            // 存储标签名称的列表
            List<String> labelNames = new ArrayList<>();
            List<String> str = tag.getLabelNames();
            if (str.size() == 2) {
                labelIds = Arrays.stream(str.get(0).split(","))
                        .map(String::trim)  // 去除空格
                        .filter(s -> !s.isEmpty())  // 过滤空字符串
                        .map(Long::parseLong)  // 转换为Long类型
                        .collect(Collectors.toList());
                labelNames = Arrays.asList(str.get(1).split(","));
            }
            tag.setLabelNames(labelNames);
            tag.setLabels(labelIds);
            if (tag.getUserSource() == 1) {
                tag.setUserName(userMap.get(tag.getUserId()).getNickname());
                tag.setUserAvatar(userMap.get(tag.getUserId()).getAvatar());
            } else {
                tag.setUserName(sysUserMap.get(tag.getUserId()).getNickname());
                tag.setUserAvatar(sysUserMap.get(tag.getUserId()).getAvatar());
            }
        });

        return blog;
    }
}
