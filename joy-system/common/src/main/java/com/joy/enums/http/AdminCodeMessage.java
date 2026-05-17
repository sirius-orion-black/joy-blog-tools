package com.joy.enums.http;

import com.joy.common.HttpCodeMessage;
import lombok.Getter;

/**
 * 管理后台相关业务消息码
 * 包含管理后台特定的业务消息定义
 */
@Getter
public enum AdminCodeMessage implements HttpCodeMessage {
    DEL_ARTICLES_COLUMN("delete_articles_column"); // 删除文章栏目

    private final String message;

    AdminCodeMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
