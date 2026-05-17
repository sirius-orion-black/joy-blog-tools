package com.joy.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joy.common.Result;
import com.joy.dto.content.BlogPostParamDto;
import com.joy.dto.content.BlogpostUpdateDto;
import com.joy.entity.content.*;
import com.joy.entity.sysUser.SysUser;
import com.joy.entity.user.User;
import com.joy.enums.http.AdminCodeMessage;
import com.joy.enums.http.CommonCodeMessage;
import com.joy.mapper.content.*;
import com.joy.mapper.sysUser.SysUserMapper;
import com.joy.mapper.user.UserMapper;
import com.joy.service.ContentBlogpostService;
import com.joy.service.GenerateJsonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ContentBlogpostServiceImpl extends ServiceImpl<ContentBlogpostMapper, ContentBlogpost> implements ContentBlogpostService {

    @Autowired
    private ContentBlogpostMapper blogpostMapper;

    @Autowired
    private ContentBlogpostLabelMapper blogpostLabelMapper;

    @Autowired
    private ContentLabelMapper labelMapper;

    @Autowired
    private ContentColumnMapper columnMapper;

    @Autowired
    private ContentClassifyMapper classifyMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private GenerateJsonService generateJsonService;

    /**
     * 获取文章列表
     *
     * @param params
     * @return
     */
    @Override
    public Result<Page<ContentBlogpost>> getBlogpost(BlogPostParamDto params) {
        Page<ContentBlogpost> page = new Page<>(params.getPage(), params.getSize());
        Page<ContentBlogpost> result = blogpostMapper.selectBlogpostPageWithLike(page, params.getTitle(), params.getClassifyId(), params.getState());
        List<ContentBlogpost> blog = result.getRecords();

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
        return Result.success(result);
    }


    // 1. 定义校验规则（扩展性强）
    public boolean validateArticle(ContentBlogpost article) {
        return article == null ||
                StringUtils.isBlank(article.getTitle()) ||
                StringUtils.isBlank(article.getIntroduction()) ||
                StringUtils.isBlank(article.getCover()) ||
                StringUtils.isBlank(article.getContent()) ||
                article.getClassifyId() == null;
    }

    /**
     * 创建博客文章
     *
     * @param blogpost
     * @return
     */
    @Override
    public Result<String> createBlogpost(ContentBlogpost blogpost) throws IOException {
        // 1. 保存主文章
        ContentBlogpost article = new ContentBlogpost();
        BeanUtils.copyProperties(blogpost, article);
        if(article.getId() != null)
            CommonCodeMessage.BAD_REQUEST.throwIt();
        if (validateArticle(article)) {
            AdminCodeMessage.INFORMATION_INCOMPLETE.throwIt();
        }
        if (article.getState() == null)
            article.setState(4); // 初始状态：预审
        if (article.getIsOriginal() == 2)
            article.setReprintAddress(null);
        article.setUserSource(2);
        Long userId = StpUtil.getLoginIdAsLong();
        article.setUserId(userId);
        this.save(article);
        // 2. 保存标签关联
        List<ContentBlogpostLabel> labels = blogpost.getLabels().stream()
                .limit(5) // 限制只处理前5个元素
                .map(labelId -> new ContentBlogpostLabel(article.getId(), labelId))
                .collect(Collectors.toList());

        if (!labels.isEmpty()) {
            blogpostLabelMapper.batchInsert(labels);
        }
        generateJsonService.blogpost();
        return Result.success();

    }

    /**
     * 修改博客文章
     *
     * @param blogpost
     * @return
     */
    @Override
    public Result<String> editBlogpost(ContentBlogpost blogpost) throws IOException {
        //判断是否是该文章创建者
        Long userId = StpUtil.getLoginIdAsLong();
        if (!userId.equals(blogpost.getUserId()))
            CommonCodeMessage.UNAUTHORIZED.throwIt();

        if (validateArticle(blogpost)) {
            AdminCodeMessage.INFORMATION_INCOMPLETE.throwIt();
        }
        if (blogpost.getIsOriginal() == 2)
            blogpost.setReprintAddress(null);
        UpdateWrapper<ContentBlogpost> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", blogpost.getId())
                .set("title", blogpost.getTitle())
                .set("introduction", blogpost.getIntroduction())
                .set("cover", blogpost.getCover())
                .set("classify_id", blogpost.getClassifyId())
                .set("column_id", blogpost.getColumnId())
                .set("read_type", blogpost.getReadType())
                .set("is_original", blogpost.getIsOriginal())
                .set("reprint_address", blogpost.getReprintAddress())
                .set("content", blogpost.getContent())
                .set("update_time", new Date());
        this.update(wrapper);
        //删除关联标签
        QueryWrapper<ContentBlogpostLabel> query = new QueryWrapper<>();
        query.eq("blogpost_id", blogpost.getId());
        blogpostLabelMapper.delete(query);
        // 2. 保存关联标签
        List<ContentBlogpostLabel> labels = blogpost.getLabels().stream()
                .map(labelId -> new ContentBlogpostLabel(blogpost.getId(), labelId))
                .collect(Collectors.toList());
        if (!labels.isEmpty()) {
            blogpostLabelMapper.batchInsert(labels);
        }
        generateJsonService.blogpost();
        return Result.success();
    }

    /**
     * 删除博客文章
     *
     * @param blogpost
     * @return
     */
    @Override
    public Result<String> delBlogpost(ContentBlogpost blogpost) throws IOException {
        //判断是否是该文章创建者
        Long userId = StpUtil.getLoginIdAsLong();
        if (!userId.equals(blogpost.getUserId()))
            CommonCodeMessage.UNAUTHORIZED.throwIt();
        if (blogpost.getId() == null)
            CommonCodeMessage.BAD_REQUEST.throwIt();
        // 删除主记录
//        this.removeById(blogpost.getId());
//        // 删除关联数据
//        labelMapper.delete(new QueryWrapper<ContentBlogpostLabel>()
//                .eq("blogpost_id", blogpost.getId()));
//        likeMapper.delete(new QueryWrapper<ContentBlogpostLike>()
//                .eq("blogpost_id", blogpost.getId()));

        //修改文章到删除状态
        ContentBlogpost article = new ContentBlogpost();
        article.setId(blogpost.getId());
        article.setState(8);
        this.updateById(article);
        generateJsonService.blogpost();
        return Result.success();
    }

    /**
     * 更新博客文章
     *
     * @param blogpost
     * @return
     */
    @Override
    public Result<String> updateBlogpost(BlogpostUpdateDto blogpost) throws IOException {
        ContentBlogpost blog = this.getById(blogpost.getId());
        if (blog == null)
            AdminCodeMessage.ARTICLE_NOT_EXIST.throwIt();
        UpdateWrapper<ContentBlogpost> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", blogpost.getId())
                .set("update_time", new Date());
        switch (blogpost.getAction().toLowerCase()) {
            case "stick":
                wrapper.setSql("is_stick = CASE WHEN is_stick = 1 THEN 2 WHEN is_stick = 2 THEN 1 ELSE is_stick END"); // 切换置顶状态
                break;
            case "recommend":
                wrapper.setSql("is_recommend = CASE WHEN is_recommend = 1 THEN 2 WHEN is_recommend = 2 THEN 1 ELSE is_recommend END");// 切换推荐状态
                break;
            case "publish":
                wrapper.set("state", 1); // 上架（已发布）
                break;
            case "return":
                wrapper.set("state", 5); // 退回
                break;
            default:
                AdminCodeMessage.INVALID_OPERATION.throwIt();
        }
        this.update(wrapper);
        generateJsonService.blogpost();
        return Result.success();
    }

    /**
     * 获取分类
     *
     * @return
     */
    @Override
    public Result<List<ContentClassify>> getBlogpostClassify() {
        return Result.success(classifyMapper.selectList(new QueryWrapper<ContentClassify>().eq("state", "1")));
    }

    /**
     * 获取标签
     *
     * @return
     */
    @Override
    public Result<List<ContentLabel>> getBlogpostLabel() {
        return Result.success(labelMapper.selectList(new QueryWrapper<ContentLabel>().eq("state", 1).eq("type", 1)));
    }

    /**
     * 获取用户专栏
     *
     * @return
     */
    @Override
    public Result<List<ContentColumn>> getUserColumn() {
        Long userId = StpUtil.getLoginIdAsLong();
        return Result.success(columnMapper.selectList(new QueryWrapper<ContentColumn>().eq("user_id", userId).eq("state", 1)));
    }
}
