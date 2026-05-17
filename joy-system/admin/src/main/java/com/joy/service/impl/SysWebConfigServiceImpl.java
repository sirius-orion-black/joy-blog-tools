package com.joy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joy.common.Result;
import com.joy.entity.sysConfig.SysConfig;
import com.joy.enums.http.CommonCodeMessage;
import com.joy.mapper.sysConfig.SysConfigMapper;
import com.joy.service.GenerateJsonService;
import com.joy.service.SysWebConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SysWebConfigServiceImpl extends ServiceImpl<SysConfigMapper,SysConfig> implements SysWebConfigService {


    @Autowired
    private GenerateJsonService generateJsonService;

    /**
     * 获取所有配置
     * @return
     */
    @Override
    public Result<Map<String, List<SysConfig>>> getConfig() {
        LambdaQueryWrapper<SysConfig> lambda = Wrappers.lambdaQuery();
        lambda.orderByAsc(SysConfig::getConfigSort);
        return Result.success(this.list(lambda).stream().collect(Collectors.groupingBy(SysConfig::getConfigType)));
    }


    /**
     * 更新配置
     * @param config
     * @return
     */
    @Override
    public Result<String> editConfig(SysConfig config) throws IOException {
        if (config.getId() == null)
            CommonCodeMessage.BAD_REQUEST.throwIt();
        SysConfig cfg = new SysConfig();
        cfg.setId(config.getId());
        cfg.setConfigName(config.getConfigName());
        cfg.setConfigValue(config.getConfigValue());
        cfg.setConfigSort(config.getConfigSort());
        cfg.setConfigState(config.getConfigState());
        cfg.setConfigRestrict(config.getConfigRestrict());
        cfg.setUpdateTime(new Date());
        this.updateById(cfg);
        if(config.getConfigType().equals("web_config")){
            generateJsonService.webConfig();
            log.info("让我看看{}", config.getConfigType());
        }

        return Result.success();
    }
}
