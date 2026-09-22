package com.joy.entity.dayByDay;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

public class DayPhotoRecord {

    //主键id
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    //用户id
    @TableField
    private Long userId;

    //原图相对存储路径
    @TableField
    private String originPath;

    //最终结果图相对存储路径
    @TableField
    private String resultPath;

    //AI抠图前景透明PNG缓存路径
    @TableField
    private String fgPath;

    //尺寸编码
    @TableField
    private String sizeCode;

    //尺寸名称
    @TableField
    private String sizeName;

    //输出宽度
    @TableField
    private Integer widthPx;

    //输出高度
    @TableField
    private Integer heightPx;

    //输出DPI
    @TableField
    private Integer dpi;

    //背景色HEX值
    @TableField
    private String bgColor;

    //背景色名称
    @TableField
    private String bgColorName;

    //输出格式 jpg/png
    @TableField
    private String format;

    //结果文件大小(KB)
    @TableField
    private Integer fileSizeKb;

    //是否开启美颜 0-否 1-是
    @TableField
    private Integer beautyEnable;

    //磨皮程度 0-100,0表示未设置
    @TableField
    private Integer skinSmooth;

    //美白程度 0-100,0表示未设置
    @TableField
    private Integer skinWhiten;

    //瘦脸程度 0-100,0表示未设置
    @TableField
    private Integer faceThin;

    //大眼程度 0-100,0表示未设置
    @TableField
    private Integer eyeEnlarge;

    //是否换装 0 否 1 是
    @TableField
    private Integer suitEnable;

    //换装素材id
    @TableField
    private Long suitId;

    //服装类型编码
    @TableField
    private String suitType;

    //服装名称
    @TableField
    private String suitName;

    //处理状态 0 待处理 1 处理中 2 成功 3 失败
    @TableField
    private Integer processStatus;

    //处理信息/错误详情
    @TableField
    private String processMsg;

    //累计下载次数
    @TableField
    private Integer downloadCount;

    //逻辑删除 0 未删 1 已删
    @TableField
    @TableLogic
    private Integer isDeleted;

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 非表字段,给前端直接用 */
    @TableField(exist = false)
    private String originUrl;
    @TableField(exist = false)
    private String resultUrl;

}
