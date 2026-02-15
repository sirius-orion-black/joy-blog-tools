package com.joy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joy.common.Result;
import com.joy.dto.content.SearchParamDto;
import com.joy.entity.content.ContentColumn;

public interface ContentColumnService {
    Result<Page<ContentColumn>> getColumn(SearchParamDto params);

    Result<String> addColumn(ContentColumn column);

    Result<String> editColumn(ContentColumn column);

    Result<String> delColumn(ContentColumn column);

    Result<String> reviewColumn(ContentColumn column);
}
