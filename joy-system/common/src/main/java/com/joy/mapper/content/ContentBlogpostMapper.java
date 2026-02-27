package com.joy.mapper.content;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joy.entity.content.ContentBlogpost;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ContentBlogpostMapper extends BaseMapper<ContentBlogpost> {

//    @Select("<script>" +
//            "SELECT b.*, " +
//            "  (SELECT COUNT(1) FROM content_blogpost_like l WHERE l.blogpost_id = b.id) AS like_count, " +
//            "  t.name AS type_name, " +
//            "  (SELECT GROUP_CONCAT(DISTINCT lbl.name SEPARATOR ',') " +
//            "   FROM content_blogpost_label bpl " +
//            "   JOIN content_label lbl ON bpl.label_id = lbl.id " +
//            "   WHERE bpl.blogpost_id = b.id " +
//            "   LIMIT 5) AS labels " +  // 获取最多5个标签
//            "FROM content_blogpost b " +
//            "LEFT JOIN content_type t ON b.type_id = t.id " +
//            "WHERE b.state != 8 " +  // 排除删除状态
//            "AND b.state != 3 " +    // 排除草稿状态
//            "<if test='state != null'>" +
//            "  AND b.state = #{state} " +  // 状态精确查询
//            "</if>" +
//            "<if test='title != null and title != \"\"'>" +
//            "  AND b.title LIKE CONCAT('%', #{title}, '%') " +  // 标题模糊查询
//            "</if>" +
//            "<if test='typeId != null'>" +
//            "  AND b.type_id = #{typeId} " +  // 分类ID精确查询
//            "</if>" +
//            "ORDER BY b.create_time DESC" +
//            "</script>")
Page<ContentBlogpost> selectBlogpostPageWithLike(
        @Param("page") Page<ContentBlogpost> page,
        @Param("title") String title,
        @Param("classifyId") Long classifyId,
        @Param("state") Integer state);

}
