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
public class ContentMessage {
    //主键id
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    //父留言id(0表示顶级留言)
    @TableField
    private Long parentId;

    //留言人id
    @TableField
    private Long userId;

    //留言内容
    @TableField
    private String content;

    //地理位置
    @TableField
    private String location;

    //留言状态 1:正常 2:待审核 3:已屏蔽 4:违规 5:删除
    @TableField
    private Integer state;

    //创建时间
    @TableField
    private Date createTime;

    // 嵌套返回
    @TableField(exist = false)
    private List<ContentMessage> children;
}
