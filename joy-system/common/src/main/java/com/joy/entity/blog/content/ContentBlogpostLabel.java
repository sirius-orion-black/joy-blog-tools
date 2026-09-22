package com.joy.entity.blog.content;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName
public class ContentBlogpostLabel {

    //主键id
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    //文章id
    @TableField
    private Long blogpostId;

    //标签id
    @TableField
    private Long labelId;

    public ContentBlogpostLabel(Long blogpostId, Long labelId) {
        this.blogpostId = blogpostId;
        this.labelId = labelId;
    }
}
