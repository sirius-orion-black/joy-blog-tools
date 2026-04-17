package com.joy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joy.common.Result;
import com.joy.dto.content.BlogPostParamDto;
import com.joy.dto.content.BlogpostUpdateDto;
import com.joy.entity.content.ContentBlogpost;
import com.joy.entity.content.ContentClassify;
import com.joy.entity.content.ContentColumn;
import com.joy.entity.content.ContentLabel;

import java.io.IOException;
import java.util.List;

public interface ContentBlogpostService {
    Result<Page<ContentBlogpost>> getBlogpost(BlogPostParamDto params);

    Result<String> createBlogpost(ContentBlogpost blogpost) throws IOException;

    Result<String> editBlogpost(ContentBlogpost blogpost) throws IOException;

    Result<String> delBlogpost(ContentBlogpost blogpost) throws IOException;

    Result<String> updateBlogpost(BlogpostUpdateDto blogpost) throws IOException;

    Result<List<ContentClassify>> getBlogpostClassify();

    Result<List<ContentLabel>> getBlogpostLabel();

    Result<List<ContentColumn>> getUserColumn();
}
