package com.joy.entity.dayByDay;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
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
    private BigDecimal anchorX;

    //锚点Y百分比
    @TableField
    private BigDecimal anchorY;

    //默认缩放比例
    @TableField
    private BigDecimal scaleRatio;

    //排序
    @TableField
    private Integer sortOrder;

    //是否启用 0 禁用 1 启用
    @TableField
    private Integer isEnabled;

    //创建时间
    @TableField
    private LocalDateTime createTime;

    /** 非表字段:缩略图完整URL */
    @TableField(exist = false)
    private String thumbUrl;

}
