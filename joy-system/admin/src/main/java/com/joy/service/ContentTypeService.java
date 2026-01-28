package com.joy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joy.common.Result;
import com.joy.dto.content.SearchParamDto;
import com.joy.entity.content.FeContentType;

import java.util.List;

public interface ContentTypeService {
    Result<Page<FeContentType>> getType(SearchParamDto params);

    Result<String> addType(FeContentType type);

    Result<String> editType(FeContentType type);

    Result<String> delType(List<Long> typeIds);

}
