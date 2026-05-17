package com.joy.common;

/**
 * 业务消息码接口
 * 所有模块的业务消息码都需要实现此接口
 * 按模块拆分业务消息码：
 * - HttpCodeMessage: 通用HTTP消息码
 * - UnauthorizedCodeMessage: 权限相关消息码
 * - GatewayCodeMessage: 网关相关消息码
 * - InfraCodeMessage: Infra相关消息码
 * - AdminCodeMessage: Admin相关消息码
 */
public interface HttpCodeMessage {
    /**
     * 获取消息内容
     * @return 消息内容字符串
     */
    String getMessage();
}