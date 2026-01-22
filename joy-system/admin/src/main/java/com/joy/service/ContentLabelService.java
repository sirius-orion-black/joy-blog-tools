package com.joy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joy.common.Result;
import com.joy.dto.content.SearchParamDto;
import com.joy.entity.content.FeContentLabel;

import java.util.List;

public interface ContentLabelService {
    Result<Page<FeContentLabel>> getLabel(SearchParamDto params);

    Result<String> editLabel(FeContentLabel label);

    Result<String> delLabel(List<Long> labelIds);
}
