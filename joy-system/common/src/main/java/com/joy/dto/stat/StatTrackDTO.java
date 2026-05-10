package com.joy.dto.stat;

import lombok.Data;

@Data
public class StatTrackDTO {
    //访问路径
    private String path;
    //前端生成的设备UUID
    private String deviceId;
    //终端类型: PC, H5, APP, MINI_PROGRAM
    private String terminalType;
    //设备信息
    private  String userAgent;
}
