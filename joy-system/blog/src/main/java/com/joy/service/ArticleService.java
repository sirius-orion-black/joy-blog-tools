package com.joy.service;

import com.joy.entity.content.ContentBlogpost;

import java.util.List;

public interface ArticleService {
    List<ContentBlogpost> getBlogpost(Long id);
}
