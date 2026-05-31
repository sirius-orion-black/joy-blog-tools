package com.joy.service.impl;

import com.joy.mapper.stat.StatDailyTrafficMapper;
import com.joy.service.SysDashBoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SysDashBoardServiceImpl implements SysDashBoardService {

    @Autowired
    private StatDailyTrafficMapper statMapper;

    /**
     * 获取仪表盘数据
     * @return 返回数据
     */
    @Override
    public Map<String, Object> dashBlog() {
        //这里三个查询可以并行发起，我懒了，就这样吧
        Map<String, Object> map = new HashMap<>();
        map.put("sumCounts",statMapper.getSummaryCounts());
        map.put("visits",statMapper.getWeeklyVisits());
        map.put("classifyArticles",statMapper.countArticlesByClassify());
        return map;
    }
}
