package com.joy.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joy.dto.content.PostCommentDto;
import com.joy.entity.content.ContentMessage;
import com.joy.mapper.content.ContentMessageMapper;
import com.joy.service.MessageService;
import com.joy.utils.IpRegionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl extends ServiceImpl<ContentMessageMapper, ContentMessage> implements MessageService {

    /**
     *
     * @param request 头部信息
     * @param comment 提交的数据
     * @return 返回的数据
     */
    @Override
    public String postComment(HttpServletRequest request, PostCommentDto comment) throws Exception {
        // 1. 校验内容
        if (StringUtils.isBlank(comment.getContent()) || comment.getContent().trim().length() > 300) {
            return "内容不能为空或者长度超过了300个字";
        }
        // 2. 通过 Sa-Token 判断登录状态
        Long userId = null;
        if (StpUtil.isLogin()) {
            userId = StpUtil.getLoginIdAsLong();
        }

        ContentMessage message = new ContentMessage();

        if (comment.getParentId() == null || comment.getParentId() == 0L) {
            message.setParentId(0L);
        } else {
            // 评论：校验父留言是否存在
            ContentMessage parent = this.getById(comment.getParentId());
            if (parent == null) {
                throw new IllegalArgumentException("被评论的留言不存在");
            }
            // 只允许两级：父留言必须是顶级留言
            if (parent.getParentId() != 0L || parent.getState() != 1) {
                throw new IllegalArgumentException("该留言不允许评论");
            }
        }

        String ip = IpRegionUtil.getClientIpAddress(request);
        String location = IpRegionUtil.getSimpleLocation(ip);

        message.setParentId(comment.getParentId());
        message.setUserId(userId);
        message.setContent(comment.getContent());
        message.setState(2);              // 待审核
        message.setCreateTime(new Date());
        message.setLocation(location);
        this.save(message);
        return "";
    }

    /**
     * 获取列表
     * @return 返回的数据
     */
    @Override
    public List<ContentMessage> messageList() {
        // 1. 查出所有审核通过的留言，按时间正序
        List<ContentMessage> all = this.list(
                new LambdaQueryWrapper<ContentMessage>()
                        .in(ContentMessage::getState, 1,2)
                        .orderByAsc(ContentMessage::getCreateTime)
        );

        // 2. 分离顶级留言和评论
        List<ContentMessage> topMessages = all.stream()
                .filter(m -> m.getParentId() == 0L)
                .collect(Collectors.toList());

        Map<Long, List<ContentMessage>> childrenMap = all.stream()
                .filter(m -> m.getParentId() != 0L)
                .collect(Collectors.groupingBy(ContentMessage::getParentId));

        // 3. 把评论塞进对应顶级留言的 children 字段
        topMessages.forEach(parent ->
                parent.setChildren(childrenMap.getOrDefault(parent.getId(), Collections.emptyList()))
        );
        return topMessages;
    }
}
