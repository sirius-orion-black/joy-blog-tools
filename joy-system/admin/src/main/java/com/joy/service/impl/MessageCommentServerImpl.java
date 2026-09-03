package com.joy.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joy.dto.blog.message.CommentParamDto;
import com.joy.entity.blog.message.ContentMessageComment;
import com.joy.mapper.blog.message.ContentMessageCommentMapper;
import com.joy.service.MessageCommentServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageCommentServerImpl extends ServiceImpl<ContentMessageCommentMapper, ContentMessageComment> implements MessageCommentServer {

    @Autowired
    private ContentMessageCommentMapper commentMapper;
    /**
     *
     * @param params 参数
     * @return 返回数据
     */
    @Override
    public Page<ContentMessageComment> getComment(CommentParamDto params) {
        Page<ContentMessageComment> page = new Page<>(params.getPage(),params.getSize());
        if(params.getState() == null)
            params.setState(2);
        return commentMapper.selectCommentPage(params.getState(),page);
    }

    /**
     *
     * @param params 参数
     * @return 返回数据
     */
    @Override
    public Boolean review(CommentParamDto params) {
        ContentMessageComment comment = new ContentMessageComment();
        comment.setId(params.getId());
        comment.setState(params.getState());
        return this.updateById(comment);
    }
}
