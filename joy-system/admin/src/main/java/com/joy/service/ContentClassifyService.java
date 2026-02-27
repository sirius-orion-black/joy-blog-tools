package com.joy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joy.common.Result;
import com.joy.dto.content.SearchParamDto;
import com.joy.entity.content.ContentClassify;

import java.util.List;

public interface ContentClassifyService {
    Result<Page<ContentClassify>> getClassify(SearchParamDto params);

    Result<String> addClassify(ContentClassify classify);

    Result<String> editClassify(ContentClassify classify);

    Result<String> delClassify(List<Long> classifyIds);

}
