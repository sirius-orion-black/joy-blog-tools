package com.joy.mapper.content;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.joy.entity.content.ContentBlogpostLabel;
import com.joy.entity.content.ContentMomentsLabel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ContentMomentsLabelMapper extends BaseMapper<ContentMomentsLabel> {

    @Insert({
            "<script>",
            "INSERT INTO content_moments_label (moments_id, label_id) VALUES ",
            "<foreach collection='list' item='item' separator=','>",
            "   (#{item.momentsId}, #{item.labelId})",
            "</foreach>",
            "</script>"
    })
    void batchInsert(@Param("list") List<ContentMomentsLabel> items);

}
