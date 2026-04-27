package com.joy.entity.content;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@TableName
public class ContentMoments {
    //主键id
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    //用户id
    @TableField
    private Long userId;

    //说说内容
    @TableField
    private String content;

    //图片URL列表，用逗号分隔
    @TableField
    private String imageUrls;

    //视频URL
    @TableField
    private String videoUrl;

    //隐私: 1公开,2好友,3私密
    @TableField
    private Integer privacy;

    //状态: 1 上架 2 删除 3 草稿 4 违规
    @TableField
    private Integer state;

    //地理位置
    @TableField
    private String location;

    //创建者来源 1: 前端 2：后台管理者
    @TableField
    private Integer userSource;

    //创建时间
    @TableField
    private Date createTime;

    //更新时间
    @TableField
    private Date updateTime;

    //标签ID list
    @TableField(exist = false)
    private List<Long> labels;

    //标签名称
    @TableField(exist = false)
    private List<String> labelNames;

    //作者名称
    @TableField(exist = false)
    private String userName;

    //作者头像
    @TableField(exist = false)
    private String userAvatar;

}