package com.joy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joy.common.Result;
import com.joy.dto.content.SearchParamDto;
import com.joy.entity.content.ContentClassify;
import com.joy.mapper.content.ContentClassifyMapper;
import com.joy.service.ContentClassifyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ContentClassifyServiceImpl extends ServiceImpl<ContentClassifyMapper, ContentClassify> implements ContentClassifyService {

    /**
     * 获取类型列表
     * @param params
     * @return
     */
    @Override
    public Result<Page<ContentClassify>> getClassify(SearchParamDto params) {
        Page<ContentClassify> page = new Page<>(params.getPage(),params.getSize());
        QueryWrapper<ContentClassify> query = new QueryWrapper<>();
        query.ne("state",3);
        if(!StringUtils.isEmpty(params.getName()))
            query.like("name",params.getName());
        if(params.getState() != null)
            query.eq("state",params.getState());
        return Result.success(this.page(page,query));
    }

    @Override
    public Result<String> addClassify(ContentClassify classify) {
        if (StringUtils.isEmpty(classify.getName()))
            return Result.badRequest();
        return this.save(classify) ? Result.success() : Result.internalServerError();
    }

    /**
     * 修改类型
     * @param classify
     * @return
     */
    @Override
    public Result<String> editClassify(ContentClassify classify) {
        return this.updateById(classify) ? Result.success() : Result.badRequest();
    }


    /**
     * 删除类型
     * @param classifyIds
     * @return
     */
    @Override
    public Result<String> delClassify(List<Long> classifyIds) {
        if (classifyIds.isEmpty())
            return Result.success();
        List<ContentClassify> types = new ArrayList<>();
        classifyIds.forEach(m -> {
            ContentClassify type = new ContentClassify();
            type.setId(m);
            type.setState(3);
            types.add(type);
        });
        return this.updateBatchById(types) ? Result.success() : Result.internalServerError();
    }
}
