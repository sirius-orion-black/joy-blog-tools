package com.joy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joy.common.Result;
import com.joy.dto.content.SearchParamDto;
import com.joy.entity.content.ContentLabel;

import java.util.List;

public interface ContentLabelService {
    Result<Page<ContentLabel>> getLabel(SearchParamDto params);

    Result<String> addLabel(ContentLabel label);

    Result<String> editLabel(ContentLabel label);

    Result<String> delLabel(List<Long> labelIds);

}
