package com.joy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joy.common.Result;
import com.joy.dto.content.SearchParamDto;
import com.joy.entity.content.FeContentLabel;
import com.joy.mapper.content.FeContentLabelMapper;
import com.joy.service.ContentLabelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ContentLabelServiceImpl extends ServiceImpl<FeContentLabelMapper, FeContentLabel> implements ContentLabelService {

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

//        List<FeContentLabel> labels = new ArrayList<>();
//        labelIds.forEach(m -> {
//            FeContentLabel label = new FeContentLabel();
//            label.setId(m);
//            labels.add(label);
//        });
        return this.removeBatchByIds(labelIds) ? Result.success() : Result.internalServerError();
    }
}
