package com.joy.configurer;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // 为流程模块Controller添加 /blog 前缀
        configurer.addPathPrefix("/blog",
                c -> c.isAnnotationPresent(ApiPrefixBlogRestController.class));

        // 为用户模块Controller添加 /file 前缀
        configurer.addPathPrefix("/file",
                c -> c.isAnnotationPresent(ApiPrefixFileRestController.class));
    }
}
