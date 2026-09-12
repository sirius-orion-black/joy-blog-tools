package com.joy.mapper.stat;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.joy.entity.stat.StatDailyTraffic;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface StatDailyTrafficMapper extends BaseMapper<StatDailyTraffic> {
    //所有的求和
    @Select("SELECT " +
            "(SELECT COUNT(*) FROM content_message_comment WHERE state in (1,2)) as messages, " +
            "(SELECT COUNT(*) FROM content_moments WHERE state=1) as moments, " +
            "(SELECT COUNT(*) FROM user WHERE state in (1,2)) as users, " +
            "(SELECT SUM(`uv`) FROM stat_daily_traffic WHERE page_path = \"global\") as vists, " +
            "(SELECT COUNT(*) FROM content_blogpost WHERE state in (1,2,4)) as articles")
    Map<String, Object> getSummaryCounts();

    // 最近一周每天访问量
    @Select("SELECT stat_date as date,pv,uv,`ip_count` as ipCount FROM stat_daily_traffic " +
            "WHERE page_path = \"global\" AND " +
            "stat_date >= CURDATE() - INTERVAL 6 DAY ")
    List<Map<String, Object>> getWeeklyVisits();

    // 根据分类获取文章数
    @Select("SELECT c.id AS classifyId,c.name AS classifyName, COUNT(b.id) AS articleCount " +
            "FROM content_classify c " +
            "LEFT JOIN content_blogpost b ON c.id = b.classify_id " +
            "GROUP BY c.id, c.name " +
            "ORDER BY articleCount DESC")
    List<Map<String, Object>> countArticlesByClassify();

}






