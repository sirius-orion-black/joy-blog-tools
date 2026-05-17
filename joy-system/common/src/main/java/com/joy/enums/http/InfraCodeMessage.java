package com.joy.enums.http;

import com.joy.common.HttpCodeMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum InfraCodeMessage implements HttpCodeMessage {
    FILE_EMPTY(HttpStatus.BAD_REQUEST, "file_cannot_empty"),      // 文件不能为空
    NO_FILE(HttpStatus.BAD_REQUEST, "please_select_file"),        // 请选择文件
    FILE_SAVING_FAILED(HttpStatus.BAD_REQUEST, "file_saving_failed");        // 请选择文件


    private final HttpStatus httpStatus;
    private final String message;

    InfraCodeMessage(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}