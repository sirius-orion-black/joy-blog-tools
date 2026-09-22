package com.joy.entity.dayByDay;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DayPhotoSize {

    //主键id
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    //尺寸名称
    @TableField
    private String name;

    //尺寸编码
    @TableField
    private String code;

    //物理宽度
    @TableField
    private BigDecimal widthMm;

    //物理高度
    @TableField
    private BigDecimal heightMm;

    //像素宽度
    @TableField
    private Integer widthPx;

    //像素高度
    @TableField
    private Integer heightPx;

    //DPI
    @TableField
    private Integer defaultDpi;

    //用途说明
    @TableField
    private String description;

    //排序权重
    @TableField
    private Integer sortOrder;

    //是否启用 0-禁用 1-启用
    @TableField
    private Integer isEnabled;

    //创建时间
    @TableField
    private LocalDateTime createTime;
}
