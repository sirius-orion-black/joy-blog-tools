package com.joy.entity.dayByDay;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName
public class DayPhotoSuit {

    //主键id
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    //服装显示名称
    @TableField
    private String name;

    //服装类型编码
    @TableField
    private String type;

    //适用性别 0 通用 1 男 2 女
    @TableField
    private Integer gender;

    //服装素材PNG路径
    @TableField
    private String imagePath;

    //缩略图路径
    @TableField
    private String thumbPath;

    //锚点X百分比
    @TableField
    private Integer anchorX;

    //锚点Y百分比
    @TableField
    private Integer anchorY;

    //Y轴额外偏移补偿(像素)，正数向下，负数向上
    @TableField
    private Integer offsetY;

    //素材中两肩之间的参考宽度(像素)，用于计算缩放比
    @TableField
    private Integer referenceShoulderWidth;

    //排序
    @TableField
    private Integer sortOrder;

    //是否启用 0 禁用 1 启用
    @TableField
    private Integer isEnabled;

    //创建时间
    @TableField
    private LocalDateTime createTime;

}
