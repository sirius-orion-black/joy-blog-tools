package com.joy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joy.entity.content.ContentBlogpost;
import com.joy.mapper.content.ContentBlogpostMapper;
import com.joy.service.ContentBlogpostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ContentBlogpostServiceImpl extends ServiceImpl<ContentBlogpostMapper, ContentBlogpost> implements ContentBlogpostService {
}
