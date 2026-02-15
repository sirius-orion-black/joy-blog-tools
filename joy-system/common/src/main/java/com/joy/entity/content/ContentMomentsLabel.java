package com.joy.entity.content;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName
public class ContentMomentsLabel {
    //说说id
    @TableField
    private Long momentsId;

    //标签id
    @TableField
    private Long labelId;
}
