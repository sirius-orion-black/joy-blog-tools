package com.joy.entity.blog.content;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName
public class ContentMomentsLabel {

    //主键id
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    //说说id
    @TableField
    private Long momentsId;

    //标签id
    @TableField
    private Long labelId;

    public ContentMomentsLabel(Long momentsId, Long labelId) {
        this.momentsId = momentsId;
        this.labelId = labelId;
    }
}
