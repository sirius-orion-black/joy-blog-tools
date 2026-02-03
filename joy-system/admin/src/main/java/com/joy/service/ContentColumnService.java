package com.joy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joy.common.Result;
import com.joy.dto.content.SearchParamDto;
import com.joy.entity.content.FeContentColumn;

public interface ContentColumnService {
    Result<Page<FeContentColumn>> getColumn(SearchParamDto params);

    Result<String> addColumn(FeContentColumn column);

    Result<String> editColumn(FeContentColumn column);

    Result<String> delColumn(FeContentColumn column);
}
