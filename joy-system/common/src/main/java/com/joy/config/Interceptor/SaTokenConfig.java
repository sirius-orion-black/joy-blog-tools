package com.joy.config.Interceptor;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Sa-Token路由拦截配置类
 * 实现WebMvcConfigurer接口，注册Sa-Token拦截器
 */
@Slf4j
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {
    /**
     * 添加Sa-Token拦截器
     * @param registry 拦截器注册表
     */
    @Autowired
    private HeadInterceptor headInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handler -> {
            // 使用SaRouter定义路由规则
            SaRouter.match("/**") // 拦截所有路径
                    .notMatch("/**/blog/v1/**", "/**/admin/auth/**","/**/infra/stat/track", "/error") // 排除认证接口、公开接口和错误页面
                    .check(r -> StpUtil.checkLogin()); // 执行登录校验
        })).addPathPatterns("/**"); // 拦截所有请求

        registry.addInterceptor(headInterceptor).addPathPatterns("/**");
    }
}
