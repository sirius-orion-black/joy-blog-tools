package com.joy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joy.common.Result;
import com.joy.dto.content.SearchParamDto;
import com.joy.entity.content.ContentType;
import com.joy.mapper.content.ContentTypeMapper;
import com.joy.service.ContentTypeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ContentTypeServiceImpl extends ServiceImpl<ContentTypeMapper, ContentType> implements ContentTypeService {

    /**
     * 获取类型列表
     * @param params
     * @return
     */
    @Override
    public Result<Page<ContentType>> getType(SearchParamDto params) {
        Page<ContentType> page = new Page<>(params.getPage(),params.getSize());
        QueryWrapper<ContentType> query = new QueryWrapper<>();
        query.ne("state",3);
        if(!StringUtils.isEmpty(params.getName()))
            query.like("name",params.getName());
        if(params.getState() != null)
            query.eq("state",params.getState());
        return Result.success(this.page(page,query));
    }

    @Override
    public Result<String> addType(ContentType type) {
        if (StringUtils.isEmpty(type.getName()))
            return Result.badRequest();
        return this.save(type) ? Result.success() : Result.internalServerError();
    }

    /**
     * 修改类型
     * @param type
     * @return
     */
    @Override
    public Result<String> editType(ContentType type) {
        return this.updateById(type) ? Result.success() : Result.badRequest();
    }


    /**
     * 删除类型
     * @param typeIds
     * @return
     */
    @Override
    public Result<String> delType(List<Long> typeIds) {
        if (typeIds.isEmpty())
            return Result.success();
        List<ContentType> types = new ArrayList<>();
        typeIds.forEach(m -> {
            ContentType type = new ContentType();
            type.setId(m);
            type.setState(3);
            types.add(type);
        });
        return this.updateBatchById(types) ? Result.success() : Result.internalServerError();
    }
}
