package com.joy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joy.common.Result;
import com.joy.dto.content.SearchParamDto;
import com.joy.entity.content.FeContentBlogpostLabel;
import com.joy.entity.content.FeContentLabel;
import com.joy.entity.content.FeContentMomentsLabel;
import com.joy.mapper.content.FeContentBlogpostLabelMapper;
import com.joy.mapper.content.FeContentLabelMapper;
import com.joy.mapper.content.FeContentMomentsLabelMapper;
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
public class ContentLabelServiceImpl extends ServiceImpl<FeContentLabelMapper, FeContentLabel> implements ContentLabelService {

    @Autowired
    private FeContentBlogpostLabelMapper feContentBlogpostLabelMapper;
    @Autowired
    private FeContentMomentsLabelMapper feContentMomentsLabelMapper;

    /**
     * 获取标签列表
     *
     * @param params
     * @return
     */
    @Override
    public Result<Page<FeContentLabel>> getLabel(SearchParamDto params) {
        Page<FeContentLabel> page = new Page<>(params.getPage(), params.getSize());
        QueryWrapper<FeContentLabel> query = new QueryWrapper<>();
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
    public Result<String> addLabel(FeContentLabel label) {
        if (StringUtils.isEmpty(label.getName()))
            return Result.badRequest();
        return this.save(label) ? Result.success() : Result.internalServerError();
    }

    /**
     * 修改标签
     *
     * @param label
     * @return
     */
    @Override
    public Result<String> editLabel(FeContentLabel label) {
        FeContentLabel oldLabel = this.getById(label.getId());
        if (oldLabel == null)
            return Result.badRequest();
        label.setType(oldLabel.getType());
        return this.updateById(label) ? Result.success() : Result.internalServerError();
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
        List<FeContentLabel> list = this.listByIds(labelIds);
        //获取标签类型分组数据
        Map<Integer, List<Long>> labelMap = list.stream().collect(Collectors.groupingBy(FeContentLabel::getType, Collectors.mapping(FeContentLabel::getId, Collectors.toList())));

        // 按类型删除关联数据
        List<Long> blogpostIds = labelMap.getOrDefault(1, Collections.emptyList());
        if(!CollectionUtils.isEmpty(blogpostIds)){
            QueryWrapper<FeContentBlogpostLabel> blogpost = new QueryWrapper<>();
            blogpost.in("label_id",blogpostIds);
            feContentBlogpostLabelMapper.delete(blogpost);

        }
        List<Long> momentsIds = labelMap.getOrDefault(2, Collections.emptyList());
        if(!CollectionUtils.isEmpty(momentsIds)){
            QueryWrapper<FeContentMomentsLabel> moments = new QueryWrapper<>();
            moments.in("label_id",momentsIds);
            feContentMomentsLabelMapper.delete(moments);
        }

        return this.removeBatchByIds(labelIds) ? Result.success() : Result.internalServerError();
    }
}
