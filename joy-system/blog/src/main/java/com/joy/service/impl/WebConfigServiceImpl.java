package com.joy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joy.entity.sysConfig.SysConfig;
import com.joy.mapper.sysConfig.SysConfigMapper;
import com.joy.service.WebConfigService;
import org.apache.commons.text.CaseUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WebConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements WebConfigService {

    /**
     * 获取网站信息
     * @return 返回信息
     */
    @Override
    public Map<String, String> webConfig() {
        //获取web config
        QueryWrapper<SysConfig> config = new QueryWrapper<>();
        config.eq("config_type", "web_config").ne("config_state", 2);
        List<SysConfig> list = this.list(config);
        //处理数据
        Map<String, String> configMap = new HashMap<>();
        for (SysConfig m : list) {
            configMap.put(CaseUtils.toCamelCase(m.getConfigKey(), false, '-'), m.getConfigValue());
        }
        return configMap;
    }
}
