package com.joy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joy.common.Result;
import com.joy.dto.content.SearchParamDto;
import com.joy.entity.content.ContentType;

import java.util.List;

public interface ContentTypeService {
    Result<Page<ContentType>> getType(SearchParamDto params);

    Result<String> addType(ContentType type);

    Result<String> editType(ContentType type);

    Result<String> delType(List<Long> typeIds);

}
