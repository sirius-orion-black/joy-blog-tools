package com.joy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joy.entity.content.ContentBlogpost;
import com.joy.entity.content.ContentMoments;
import com.joy.entity.sysConfig.SysConfig;
import com.joy.entity.sysUser.SysUser;
import com.joy.entity.user.User;
import com.joy.mapper.content.ContentBlogpostMapper;
import com.joy.mapper.content.ContentLabelMapper;
import com.joy.mapper.content.ContentMomentsLabelMapper;
import com.joy.mapper.content.ContentMomentsMapper;
import com.joy.mapper.sysConfig.SysConfigMapper;
import com.joy.mapper.sysUser.SysUserMapper;
import com.joy.mapper.user.UserMapper;
import com.joy.service.GenerateJsonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.apache.commons.text.CaseUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GenerateJsonServiceImpl implements GenerateJsonService {

    @Autowired
    private SysConfigMapper configMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ContentMomentsMapper momentsMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private ContentBlogpostMapper blogpostMapper;

    /**
     * 确保目录存在
     */
    private boolean ensureDirectoryExists(File directory) {
        if (!directory.exists()) {
            // 尝试创建目录
            boolean created = directory.mkdirs();
            if (!created) {
                // 创建失败，尝试更详细的诊断
                if (directory.getParentFile() != null && !directory.getParentFile().canWrite()) {
                    log.info("父目录不可写: {}", directory.getParentFile().getAbsolutePath());
                } else {
                    log.info("创建目录失败，可能原因：权限不足或磁盘空间不足");
                }
                return false;
            }

            // 验证目录是否真的被创建
            if (!directory.exists() || !directory.isDirectory()) {
                log.info("目录创建后验证失败");
                return false;
            }
        } else if (!directory.isDirectory()) {
            log.info("路径存在但不是目录: {}", directory.getAbsolutePath());
            return false;
        }

        // 额外检查：确保目录可写
        if (!directory.canWrite()) {
            log.info("目录存在但不可写: {}", directory.getAbsolutePath());
            return false;
        }

        return true;
    }

    /**
     * 输出web config json
     */
    @Override
    @Async
    public void webConfig() throws IOException {
        //获取web config
        QueryWrapper<SysConfig> config = new QueryWrapper<>();
        config.eq("config_type", "web_config").ne("config_state", 2);
        List<SysConfig> list = configMapper.selectList(config);
        //获取路径
        QueryWrapper<SysConfig> path = new QueryWrapper<>();
        path.eq("config_key", "web-config");
        SysConfig url = configMapper.selectOne(path);
        if (url.getConfigState().equals(2) && StringUtils.isEmpty(url.getConfigValue()))
            return;
        //处理数据
        Map<String, String> configMap = new HashMap<>();
        for (SysConfig m : list) {
            configMap.put(CaseUtils.toCamelCase(m.getConfigKey(), false, '-'), m.getConfigValue());
        }
        // 确保目录存在
        String filePath = url.getConfigValue();
        File directory = new File(filePath);
        if (ensureDirectoryExists(directory)) {
            File targetFile = new File(filePath + CaseUtils.toCamelCase(url.getConfigKey(), false, '-') + ".json");
            //objectMapper.writerWithDefaultPrettyPrinter().writeValue(targetFile, configMap);
            objectMapper.writeValue(targetFile, configMap);
            log.info("webConfigJSON文件生成成功: {}", targetFile.getAbsolutePath());
            log.info("webConfig文件大小: {}字节", targetFile.length());
        } else {
            log.info("webConfig无法创建目录: {}", filePath);
        }

    }

    /**
     * 输出朋友圈json
     *
     * @throws IOException
     */
    @Override
    @Async
    public void moments() throws IOException {
        List<ContentMoments> moments = momentsMapper.selectMomentsTransformJson();
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

        //获取路径
        QueryWrapper<SysConfig> path = new QueryWrapper<>();
        path.eq("config_key", "moments-content");
        SysConfig url = configMapper.selectOne(path);
        if (url.getConfigState().equals(2) && StringUtils.isEmpty(url.getConfigValue()))
            return;
        // 确保目录存在
        String filePath = url.getConfigValue();
        File directory = new File(filePath);
        if (ensureDirectoryExists(directory)) {
            File targetFile = new File(filePath + CaseUtils.toCamelCase(url.getConfigKey(), false, '-') + ".json");
            objectMapper.writeValue(targetFile, moments);
            log.info("momentsJSON文件生成成功: {}", targetFile.getAbsolutePath());
            log.info("moments文件大小: {}字节", targetFile.length());
        } else {
            log.info("moments无法创建目录: {}", filePath);
        }


    }

    /**
     * 输出文章json
     *
     * @throws IOException
     */
    @Override
    @Async
    public void blogpost() throws IOException {
        List<ContentBlogpost> blog = blogpostMapper.selectBlogpostTransformJson();
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

        //获取路径
        QueryWrapper<SysConfig> path = new QueryWrapper<>();
        path.in("config_key", "article-list", "article-details");
        List<SysConfig> urls = configMapper.selectList(path);
        Map<String, SysConfig> urlMap = urls.stream()
                .collect(Collectors.toMap(SysConfig::getConfigKey, config -> config));


        List<Map<String, Object>> articleList = new ArrayList<>();
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
            Map<String, Object> article = new HashMap<>();
            article.put("title", tag.getTitle());
            article.put("introduction", tag.getIntroduction());
            article.put("cover", tag.getCover());
            article.put("labelNames", tag.getLabelNames());
            article.put("userName", tag.getUserName());
            article.put("columnName", tag.getColumnName());
            article.put("userAvatar", tag.getUserAvatar());
            article.put("createTime", tag.getCreateTime());
            article.put("id", tag.getId());
            articleList.add(article);
            if (urlMap.get("article-details").getConfigState().equals(2) && StringUtils.isEmpty(urlMap.get("article-details").getConfigValue()))
                return;
            //确保目录存在
            String filePath = urlMap.get("article-details").getConfigValue();
            File directory = new File(filePath);
            if (ensureDirectoryExists(directory)) {
                File targetFile = new File(
                        filePath +
                                CaseUtils.toCamelCase(urlMap.get("article-details").getConfigKey(), false, '-') + "-" +
                                tag.getId() + ".json");
                try {
                    objectMapper.writeValue(targetFile, tag);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                log.info("博客文章{}内容JSON文件生成成功: {}",tag.getId(), targetFile.getAbsolutePath());
                log.info("博客文章{}内容文件大小: {}字节", tag.getId(),targetFile.length());
            } else {
                log.info("博客文章{}内容无法创建目录: {}",tag.getId(), filePath);
            }
        });

        if (urlMap.get("article-list").getConfigState().equals(2) && StringUtils.isEmpty(urlMap.get("article-list").getConfigValue()))
            return;
        //确保目录存在
        String filePath = urlMap.get("article-list").getConfigValue();
        File directory = new File(filePath);
        if (ensureDirectoryExists(directory)) {
            File targetFile = new File(filePath + CaseUtils.toCamelCase(urlMap.get("article-list").getConfigKey(), false, '-') + ".json");
            objectMapper.writeValue(targetFile, articleList);
            log.info("博客文章列表JSON文件生成成功: {}", targetFile.getAbsolutePath());
            log.info("博客文章列表文件大小: {}字节", targetFile.length());
        } else {
            log.info("博客文章列表无法创建目录: {}", filePath);
        }
    }

}
