package com.joy.config.apiPrefix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.SimpleDateFormat;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // 为博客模块Controller添加 /blog 前缀
        configurer.addPathPrefix("/blog",
                c -> c.isAnnotationPresent(ApiPrefixBlogRestController.class));
        // 为用户模块Controller添加 /file 前缀
        configurer.addPathPrefix("/infra",
                c -> c.isAnnotationPresent(ApiPrefixInfraRestController.class));
        // 为后台模块Controller添加 /admin 前缀
        configurer.addPathPrefix("/admin",
                c -> c.isAnnotationPresent(ApiPrefixAdminRestController.class));
    }


    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new FormatToDateConverter());
    }

    @Bean
    public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss"));
//        objectMapper.setTimeZone(java.util.TimeZone.getTimeZone("GMT+8")); // 设置时区
//        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//        converter.setObjectMapper(objectMapper);
        converter.setObjectMapper(objectMapper);  // 使用共享的 ObjectMapper
        return converter;
    }
}
