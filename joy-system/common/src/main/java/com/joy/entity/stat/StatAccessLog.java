package com.joy.entity.stat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName
public class StatAccessLog {

    //主键id
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    //用户IP
    @TableField
    private String requestIp;

    //前端生成的设备UUID
    @TableField
    private String deviceId;

    //访问路径
    @TableField
    private String pagePath;

    //标识
    @TableField
    private String userAgent;

    //终端类型: PC, H5, APP, MINI_PROGRAM
    @TableField
    private String terminalType;

    //访问时间
    @TableField
    private LocalDateTime accessTime;
}
