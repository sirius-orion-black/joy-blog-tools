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
public class ContentBlogpost {

    //主键id
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    //文章标题
    @TableField
    private String title;

    //文章简介
    @TableField
    private String introduction;

    //文章封面地址
    @TableField
    private String cover;

    //类型id
    @TableField
    private Long classifyId;

    //专栏id
    @TableField
    private Long columnId;

    //用户id
    @TableField
    private Long userId;

    //文章内容
    @TableField
    private String content;

    //阅读方式:1 免费阅读 2 付费阅读 3 会员阅读 4 私有 5 登录阅读
    @TableField
    private Integer readType;

    //是否置顶:1 置顶 2 不置顶
    @TableField
    private Integer isStick;

    //状态: 1 上架 2 下架 3 草稿 4 预审 5 已退回 6 已下线 7 已归档 8 删除
    @TableField
    private Integer state;

    //是否原创:  1 转载 2 原创
    @TableField
    private Integer isOriginal;

    //转载地址url
    @TableField
    private String reprintAddress;

    //是否推荐: 1 推荐 2 不推荐
    @TableField
    private Integer isRecommend;

    //阅读量
    @TableField
    private Integer readership;

    //关键词
    @TableField
    private String keywords;

    //创建者来源 1: 前端 2：后台管理者
    @TableField
    private Integer userSource;

    //创建时间
    @TableField
    private Date createTime;

    //更新时间
    @TableField
    private Date updateTime;

    //点赞数
    @TableField(exist = false)
    private Long likeCount;

    //分类名称
    @TableField(exist = false)
    private String classifyName;

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

    //专栏名称
    @TableField(exist = false)
    private String columnName;

}
