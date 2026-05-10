package com.joy.entity.stat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@TableName
public class StatDailyTraffic {

    //主键id
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    //统计日期
    @TableField
    private LocalDate statDate;

    //页面路径，__global__代表全站
    @TableField
    private String pagePath;

    //页面访问量
    @TableField
    private Long pv;

    //独立访客数
    @TableField
    private Long uv;

    //独立IP数
    @TableField
    private Integer ipCount;

    //创建时间
    @TableField
    private Date createTime;

    //更新时间
    @TableField
    private Date updateTime;
}