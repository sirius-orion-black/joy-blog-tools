package com.joy.enums.http;

import com.joy.common.HttpCodeMessage;
import lombok.Getter;

/**
 * 基础设施相关业务消息码
 * 包含文件上传、存储等基础服务的业务消息定义
 */
@Getter
public enum InfraCodeMessage implements HttpCodeMessage {
    FILE_EMPTY("file_cannot_empty"),      // 文件不能为空
    NO_FILE("please_select_file");       // 请选择文件

    private final String message;

    InfraCodeMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}