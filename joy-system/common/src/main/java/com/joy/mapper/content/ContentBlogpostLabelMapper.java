package com.joy.mapper.content;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.joy.entity.content.ContentBlogpostLabel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ContentBlogpostLabelMapper extends BaseMapper<ContentBlogpostLabel> {
    @Insert({
            "<script>",
            "INSERT INTO content_blogpost_label (blogpost_id, label_id) VALUES ",
            "<foreach collection='list' item='item' separator=','>",
            "   (#{item.blogpostId}, #{item.labelId})",
            "</foreach>",
            "</script>"
    })
    void batchInsert(@Param("list") List<ContentBlogpostLabel> items);
}
