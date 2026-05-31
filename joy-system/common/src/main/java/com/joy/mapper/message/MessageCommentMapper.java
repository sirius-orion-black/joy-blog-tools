package com.joy.mapper.message;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joy.entity.message.MessageComment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface MessageCommentMapper extends BaseMapper<MessageComment> {

    @Select("SELECT c.*, u.nickname " +
            "FROM message_comment c " +
            "LEFT JOIN user u ON c.user_id = u.id " +
            "WHERE c.state = #{state} " +
            "AND c.state != 5 " +
            "AND (c.parent_id = 0 " +
            "     OR (SELECT p.state FROM message_comment p WHERE p.id = c.parent_id) != 5) " +
            "ORDER BY c.create_time DESC")
    Page<MessageComment> selectCommentPage(@Param("state") Integer state, Page<?> page);

}
