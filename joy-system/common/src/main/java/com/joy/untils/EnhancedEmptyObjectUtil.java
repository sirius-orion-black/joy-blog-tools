package com.joy.untils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Collections;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnhancedEmptyObjectUtil {

    /**
     * 单例实例，确保全局只有一个空对象
     */
    private static final EnhancedEmptyObjectUtil INSTANCE = new EnhancedEmptyObjectUtil();

    /**
     * 私有构造函数 确保只能通过getInstance()获取实例
     */
    private EnhancedEmptyObjectUtil() {}

    /**
     * 获取空对象单例实例 使用此方法进行反序列化
     */
    @JsonCreator
    public static EnhancedEmptyObjectUtil getInstance() {
        return INSTANCE;
    }

    /**
     * 返回空Map确保JSON序列化为{}
     */
    @JsonValue
    public Map<String, Object> toMap() {
        return Collections.emptyMap();
    }

    /**
     * 与其他对象比较 - 空对象只等于其他空对象
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof EnhancedEmptyObjectUtil;
    }

    /**
     * 如果当前为空对象，则执行指定操作
     * action 要执行的操作
     * 返回当前空对象实例，支持链式调用
     */
    public EnhancedEmptyObjectUtil ifEmpty(Runnable action) {
        action.run();
        return this;
    }

    /**
     * 判断对象是否为空
     * obj 要检查的对象
     * 如果对象为null或EnhancedEmptyObject实例则返回true
     */
    public static boolean isEmpty(Object obj) {
        return obj == null || obj instanceof EnhancedEmptyObjectUtil;
    }

    /**
     * 确保数据不为空
     * data 原始数据
     * 如果data不为null则返回data，否则返回空对象
     */
    public static <T> T ensureNotEmpty(T data) {
        return data != null ? data : (T) INSTANCE;
    }

    @Override
    public String toString() {
        return "{}";
    }
}
