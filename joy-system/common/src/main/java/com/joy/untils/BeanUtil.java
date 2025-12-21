package com.joy.untils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BeanUtil {

    private static ApplicationContext context;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    /**
     * 获取指定类型的Bean
     * @param requiredType 目标Bean类型
     * @param <T> 泛型类型
     * @return Bean实例
     */
    public static <T> T getBean(Class<T> requiredType) {
        return context.getBean(requiredType);
    }
}
