package com.joy.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joy.common.Result;
import com.joy.dto.content.SearchParamDto;
import com.joy.dto.utils.IpLocationDto;
import com.joy.entity.content.ContentLabel;
import com.joy.entity.content.ContentMoments;
import com.joy.entity.content.ContentMomentsLabel;
import com.joy.entity.sysUser.SysUser;
import com.joy.entity.user.User;
import com.joy.enums.http.AdminCodeMessage;
import com.joy.enums.http.CommonCodeMessage;
import com.joy.mapper.content.ContentLabelMapper;
import com.joy.mapper.content.ContentMomentsLabelMapper;
import com.joy.mapper.content.ContentMomentsMapper;
import com.joy.mapper.sysUser.SysUserMapper;
import com.joy.mapper.user.UserMapper;
import com.joy.service.ContentMomentsService;
import com.joy.service.GenerateJsonService;
import com.joy.utils.IpRegionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ContentMomentsServiceImpl extends ServiceImpl<ContentMomentsMapper, ContentMoments> implements ContentMomentsService {

    @Autowired
    private ContentMomentsMapper momentsMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private ContentLabelMapper labelMapper;

    @Autowired
    private ContentMomentsLabelMapper momentsLabelMapper;

    @Autowired
    private GenerateJsonService generateJsonService;

    /**
     * 获取朋友圈列表
     *
     * @param params
     * @return
     */
    @Override
    public Result<Page<ContentMoments>> getMoments(SearchParamDto params) {
        Page<ContentMoments> page = new Page<>(params.getPage(), params.getSize());
        List<Long> userIds = new ArrayList<>();
        if (StringUtils.isNotBlank(params.getName())) {
            userIds = momentsMapper.findAllUserIdsByNickname(params.getName());
        }
        Page<ContentMoments> resultPage = momentsMapper.selectMomentsWithLabels(page, userIds);
        List<ContentMoments> moments = resultPage.getRecords();
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

        return Result.success(resultPage);
    }

    // 1. 定义校验规则
    public boolean validateMoments(ContentMoments moments) {
        return moments == null ||
                (StringUtils.isBlank(moments.getContent()) &&
                        StringUtils.isBlank(moments.getImageUrls()) &&
                        StringUtils.isBlank(moments.getVideoUrl()));
    }

    /**
     * 发表朋友圈
     *
     * @param request
     * @param moments
     * @return
     */
    @Override
    public Result<String> createMoments(HttpServletRequest request, ContentMoments moments) throws IOException {
        //朋友圈信息拼装
        ContentMoments content = new ContentMoments();
        BeanUtils.copyProperties(moments, content);
        if (content.getId() != null)
            CommonCodeMessage.BAD_REQUEST.throwIt();
        if (validateMoments(content)) {
            AdminCodeMessage.INFORMATION_INCOMPLETE.throwIt();
        }
        Long userId = StpUtil.getLoginIdAsLong();
        content.setUserId(userId);
        IpLocationDto ipInfo = IpRegionUtil.getLocationFromRequest(request);
        content.setState(moments.getState() == null ? 1 : moments.getState());
        content.setPrivacy(moments.getPrivacy() == null ? 1 : moments.getPrivacy());
        content.setLocation(ipInfo.getFullAdr());
        content.setUserSource(2);
        this.save(content);
        // 保存标签关联
        List<ContentMomentsLabel> labels = moments.getLabels().stream()
                .limit(5) // 限制只处理前5个元素
                .map(labelId -> new ContentMomentsLabel(content.getId(), labelId))
                .collect(Collectors.toList());

        if (!labels.isEmpty()) {
            momentsLabelMapper.batchInsert(labels);
        }
        return Result.success();
    }

    /**
     * 更新朋友圈状态
     *
     * @param moments
     * @param action
     * @return
     */
    @Override
    public Result<String> updMoments(ContentMoments moments, String action) throws IOException {
        //判断是否是该文章创建者
        if (action.equals("del")) {
            Long userId = StpUtil.getLoginIdAsLong();
            if (!userId.equals(moments.getUserId()))
                CommonCodeMessage.UNAUTHORIZED.throwIt();
            if (moments.getId() == null)
                CommonCodeMessage.BAD_REQUEST.throwIt();
        }
        ContentMoments mts = new ContentMoments();
        mts.setId(moments.getId());
        mts.setState(action.equals("del") ? 2 : 4);
        this.updateById(mts);
        return Result.success();
    }

    /**
     * 获取标签
     *
     * @return
     */
    @Override
    public Result<List<ContentLabel>> getMomentsLabel() {
        return Result.success(labelMapper.selectList(new QueryWrapper<ContentLabel>().eq("state", 1).eq("type", 2)));
    }
}
