package com.joy.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joy.common.Result;
import com.joy.dto.content.SearchParamDto;
import com.joy.entity.content.ContentBlogpost;
import com.joy.entity.content.ContentColumn;
import com.joy.mapper.content.ContentBlogpostMapper;
import com.joy.mapper.content.ContentColumnMapper;
import com.joy.service.ContentColumnService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ContentColumnServiceImpl extends ServiceImpl<ContentColumnMapper, ContentColumn> implements ContentColumnService {

    @Autowired
    private ContentBlogpostMapper blogpostMapper;

    /**
     * 获取专栏列表
     * @param params
     * @return
     */
    @Override
    public Result<Page<ContentColumn>> getColumn(SearchParamDto params) {
        Page<ContentColumn> page = new Page<>(params.getPage(),params.getSize());

        QueryWrapper<ContentColumn> query = new QueryWrapper<>();
        if(!StringUtils.isEmpty(params.getName()))
            query.like("name",params.getName());
        if(params.getState() != null)
            query.eq("state",params.getState());
        query.ne("state",4);
        return Result.success(this.page(page,query));
    }

    /**
     * 新增专栏
     * @param column
     * @return
     */
    @Override
    public Result<String> addColumn(ContentColumn column) {
        column.setUserId(StpUtil.getLoginIdAsLong());
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
    public Result<String> editColumn(ContentColumn column) {
        //判断是否是该专栏创建者
        Long userId = StpUtil.getLoginIdAsLong();
        if(!userId.equals(column.getUserId()))
            return Result.unauthorized();

        if (StringUtils.isEmpty(column.getName()) || StringUtils.isEmpty(column.getCover()) || StringUtils.isEmpty(column.getIntroduction()))
            return Result.badRequest();
        ContentColumn info = new ContentColumn();
        info.setId(column.getId());
        info.setUpdateTime(new Date());
        info.setCover(column.getCover());
        info.setName(column.getName());
        info.setIntroduction(column.getIntroduction());
        return this.updateById(info) ? Result.success() : Result.internalServerError();
    }

    /**
     * 删除专栏
     * @param column
     * @return
     */
    @Override
    public Result<String> delColumn(ContentColumn column) {
        ContentColumn detail = this.getById(column.getId());

        //判断是否是该专栏创建者
        Long userId = StpUtil.getLoginIdAsLong();
        if(!userId.equals(column.getUserId()))
            return Result.unauthorized();

        QueryWrapper<ContentBlogpost> postQuery = new QueryWrapper<>();
        postQuery.eq("column_id",detail.getId()).ne("state",8);
        List<ContentBlogpost> list = blogpostMapper.selectList(postQuery);
        if(!list.isEmpty())
            return Result.internalServerError("delete_articles_column");
        detail.setState(4);
        return this.updateById(detail) ? Result.success() : Result.internalServerError();
    }

    /**
     * 预审专栏
     * @param column
     * @return
     */
    @Override
    public Result<String> reviewColumn(ContentColumn column) {
        ContentColumn info = new ContentColumn();
        info.setId(column.getId());
        info.setState(column.getState());
        return this.updateById(info) ? Result.success() : Result.internalServerError();
    }
}
