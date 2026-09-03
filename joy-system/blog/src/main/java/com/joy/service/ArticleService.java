package com.joy.service;

import com.joy.entity.blog.content.ContentBlogpost;

import java.util.List;

public interface ArticleService {
    List<ContentBlogpost> getBlogpost(Long id);
}
