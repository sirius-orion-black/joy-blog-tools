package com.joy.mapper.content;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joy.entity.content.ContentMoments;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ContentMomentsMapper extends BaseMapper<ContentMoments> {

    /**
     * 查询列表，包含标签信息
     * 可选：按单个或多个用户ID查询
     */
    Page<ContentMoments> selectMomentsWithLabels(Page<ContentMoments> page, @Param("userIds") List<Long> userIds);

    @Select("SELECT id FROM user WHERE nickname = #{nickname} " +
            "UNION ALL " +
            "SELECT id FROM sys_user WHERE nickname = #{nickname}")
    List<Long> findAllUserIdsByNickname(@Param("nickname") String nickname);

    List<ContentMoments> selectMomentsPublishPublicly();

}
