package com.joy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joy.common.Result;
import com.joy.dto.content.SearchParamDto;
import com.joy.entity.content.ContentBlogpostLabel;
import com.joy.entity.content.ContentLabel;
import com.joy.entity.content.ContentMomentsLabel;
import com.joy.enums.http.CommonCodeMessage;
import com.joy.mapper.content.ContentBlogpostLabelMapper;
import com.joy.mapper.content.ContentLabelMapper;
import com.joy.mapper.content.ContentMomentsLabelMapper;
import com.joy.service.ContentLabelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ContentLabelServiceImpl extends ServiceImpl<ContentLabelMapper, ContentLabel> implements ContentLabelService {

    @Autowired
    private ContentBlogpostLabelMapper blogpostLabelMapper;
    @Autowired
    private ContentMomentsLabelMapper momentsLabelMapper;

    /**
     * 获取标签列表
     *
     * @param params
     * @return
     */
    @Override
    public Result<Page<ContentLabel>> getLabel(SearchParamDto params) {
        Page<ContentLabel> page = new Page<>(params.getPage(), params.getSize());
        QueryWrapper<ContentLabel> query = new QueryWrapper<>();
        if (!StringUtils.isEmpty(params.getName()))
            query.like("name", params.getName());
        if (params.getState() != null)
            query.eq("state", params.getState());
        if (params.getType() != null)
            query.eq("type", params.getType());
        return Result.success(this.page(page, query));
    }

    /**
     * 新增标签
     *
     * @param label
     * @return
     */
    @Override
    public Result<String> addLabel(ContentLabel label) {
        if (StringUtils.isEmpty(label.getName()))
            CommonCodeMessage.BAD_REQUEST.throwIt();
        return this.save(label) ? Result.success() : Result.fail(CommonCodeMessage.INTERNAL_SERVER_ERROR.getHttpStatus());
    }

    /**
     * 修改标签
     *
     * @param label
     * @return
     */
    @Override
    public Result<String> editLabel(ContentLabel label) {
        ContentLabel oldLabel = this.getById(label.getId());
        if (oldLabel == null)
            CommonCodeMessage.BAD_REQUEST.throwIt();
        label.setType(oldLabel.getType());
        return this.updateById(label) ? Result.success() : Result.fail(CommonCodeMessage.INTERNAL_SERVER_ERROR.getHttpStatus());
    }

    /**
     * 删除标签
     *
     * @param labelIds
     * @return
     */
    @Override
    public Result<String> delLabel(List<Long> labelIds) {
        if (labelIds.isEmpty())
            return Result.success();
        List<ContentLabel> list = this.listByIds(labelIds);
        //获取标签类型分组数据
        Map<Integer, List<Long>> labelMap = list.stream().collect(Collectors.groupingBy(ContentLabel::getType, Collectors.mapping(ContentLabel::getId, Collectors.toList())));

        // 按类型删除关联数据
        List<Long> blogpostIds = labelMap.getOrDefault(1, Collections.emptyList());
        if(!CollectionUtils.isEmpty(blogpostIds)){
            QueryWrapper<ContentBlogpostLabel> blogpost = new QueryWrapper<>();
            blogpost.in("label_id",blogpostIds);
            blogpostLabelMapper.delete(blogpost);
        }
        List<Long> momentsIds = labelMap.getOrDefault(2, Collections.emptyList());
        if(!CollectionUtils.isEmpty(momentsIds)){
            QueryWrapper<ContentMomentsLabel> moments = new QueryWrapper<>();
            moments.in("label_id",momentsIds);
            momentsLabelMapper.delete(moments);
        }

        return this.removeBatchByIds(labelIds) ? Result.success() : Result.fail(CommonCodeMessage.INTERNAL_SERVER_ERROR.getHttpStatus());
    }
}
