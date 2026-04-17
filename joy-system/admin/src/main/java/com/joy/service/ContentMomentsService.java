package com.joy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joy.common.Result;
import com.joy.dto.content.SearchParamDto;
import com.joy.entity.content.ContentLabel;
import com.joy.entity.content.ContentMoments;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public interface ContentMomentsService {
    Result<Page<ContentMoments>> getMoments(SearchParamDto params);

    Result<String> createMoments(HttpServletRequest request, ContentMoments moments) throws IOException;

    Result<String> updMoments(ContentMoments moments, String action) throws IOException;

    Result<List<ContentLabel>> getMomentsLabel();
}
