package com.joy.utils;

import com.joy.enums.http.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息码查找工具类
 * 提供统一的消息码验证和查找功能
 */
public class CodeMessageLookup {

    /**
     * 消息码映射表，用于验证消息码是否存在
     */
    private static final Map<String, String> MESSAGE_MAP = new HashMap<>();

    /**
     * 静态初始化块，初始化所有消息码映射
     */
    static {
        initMessageMap();
    }

    /**
     * 初始化消息码映射表
     */
    private static void initMessageMap() {
        // 添加通用消息码
        for (CommonCodeMessage message : CommonCodeMessage.values()) {
            MESSAGE_MAP.put(message.getMessage(), message.getMessage());
        }

        // 添加权限消息码
        for (UnauthorizedCodeMessage message : UnauthorizedCodeMessage.values()) {
            MESSAGE_MAP.put(message.getMessage(), message.getMessage());
        }

        // 添加网关消息码
        for (GatewayCodeMessage message : GatewayCodeMessage.values()) {
            MESSAGE_MAP.put(message.getMessage(), message.getMessage());
        }

        // 添加基础设施消息码
        for (InfraCodeMessage message : InfraCodeMessage.values()) {
            MESSAGE_MAP.put(message.getMessage(), message.getMessage());
        }

        // 添加管理消息码
        for (AdminCodeMessage message : AdminCodeMessage.values()) {
            MESSAGE_MAP.put(message.getMessage(), message.getMessage());
        }
    }

    /**
     * 检查消息码是否存在
     * @param message 消息码
     * @return 存在返回true，否则返回false
     */
    public static boolean containsMessage(String message) {
        return MESSAGE_MAP.containsKey(message);
    }

    /**
     * 根据消息码获取对应的字符串
     * @param message 消息码
     * @return 对应的字符串，如果不存在返回null
     */
    public static String getMessage(String message) {
        return MESSAGE_MAP.get(message);
    }
}
