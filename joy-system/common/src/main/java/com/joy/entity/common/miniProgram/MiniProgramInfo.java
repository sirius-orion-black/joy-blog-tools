package com.joy.entity.common.miniProgram;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class MiniProgramInfo {

    //主键id
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    //小程序AppID
    @TableField
    private String appId;

    //小程序密钥
    @TableField
    private String appSecret;

    //小程序名称
    @TableField
    private String programName;

    //小程序原始ID
    @TableField
    private String originalId;

    //平台： 1 微信小程序 2 支付宝
    @TableField
    private Integer type;

    //状态：0未发布/1已发布/2已下架/3审核中
    @TableField
    private Integer status;

    //备注信息
    @TableField
    private String remark;

    //软删除标记：0-未删除 1-已删除
    @TableField
    private Integer isDeleted;

    //创建时间
    @TableField
    private Date createTime;

    //更新时间
    @TableField
    private Date updateTime;

}
