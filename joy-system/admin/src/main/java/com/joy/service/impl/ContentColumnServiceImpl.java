package com.joy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joy.common.Result;
import com.joy.dto.content.SearchParamDto;
import com.joy.entity.content.FeContentBlogpost;
import com.joy.entity.content.FeContentColumn;
import com.joy.mapper.content.FeContentBlogpostMapper;
import com.joy.mapper.content.FeContentColumnMapper;
import com.joy.service.ContentColumnService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ContentColumnServiceImpl extends ServiceImpl<FeContentColumnMapper, FeContentColumn> implements ContentColumnService {

    @Autowired
    private FeContentBlogpostMapper feContentBlogpostMapper;

    /**
     * 获取专栏列表
     * @param params
     * @return
     */
    @Override
    public Result<Page<FeContentColumn>> getColumn(SearchParamDto params) {
        Page<FeContentColumn> page = new Page<>(params.getPage(),params.getSize());

        QueryWrapper<FeContentColumn> query = new QueryWrapper<>();
        if(!StringUtils.isEmpty(params.getName()))
            query.like("name",params.getName());
        if(params.getState() != null)
            query.eq("state",params.getState());
        return Result.success(this.page(page,query));
    }

    /**
     * 新增专栏
     * @param column
     * @return
     */
    @Override
    public Result<String> addColumn(FeContentColumn column) {
        if (StringUtils.isEmpty(column.getName()) || StringUtils.isEmpty(column.getCover()) || StringUtils.isEmpty(column.getIntroduction()))
            return Result.badRequest();
        return this.save(column) ? Result.success() : Result.internalServerError();
    }

    /**
     * 修改专栏
     * @param column
     * @return
     */
    @Override
    public Result<String> editColumn(FeContentColumn column) {
        if (StringUtils.isEmpty(column.getName()) || StringUtils.isEmpty(column.getCover()) || StringUtils.isEmpty(column.getIntroduction()))
            return Result.badRequest();
        return this.updateById(column) ? Result.success() : Result.internalServerError();
    }

    /**
     * 删除专栏
     * @param column
     * @return
     */
    @Override
    public Result<String> delColumn(FeContentColumn column) {
        FeContentColumn detail = this.getById(column.getId());

        QueryWrapper<FeContentBlogpost> postQuery = new QueryWrapper<>();
        postQuery.eq("column_id",detail.getId()).ne("state",8);
        List<FeContentBlogpost> list = feContentBlogpostMapper.selectList(postQuery);
        if(list != null )
            return Result.internalServerError("delete_articles_column");
        detail.setState(4);
        return this.updateById(detail) ? Result.success() : Result.internalServerError();
    }
}
