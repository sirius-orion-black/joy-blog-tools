package com.joy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joy.entity.sysConfig.SysConfig;
import com.joy.mapper.sysConfig.SysConfigMapper;
import com.joy.service.GenerateJsonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.apache.commons.text.CaseUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class GenerateJsonServiceImpl implements GenerateJsonService {

    @Autowired
    private SysConfigMapper configMapper;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 确保目录存在
     */
    private boolean ensureDirectoryExists(File directory) {
        if (!directory.exists()) {
            // 尝试创建目录
            boolean created = directory.mkdirs();
            if (!created) {
                // 创建失败，尝试更详细的诊断
                if (directory.getParentFile() != null && !directory.getParentFile().canWrite()) {
                    log.info("父目录不可写: {}",directory.getParentFile().getAbsolutePath());
                } else {
                    log.info("创建目录失败，可能原因：权限不足或磁盘空间不足");
                }
                return false;
            }

            // 验证目录是否真的被创建
            if (!directory.exists() || !directory.isDirectory()) {
                log.info("目录创建后验证失败");
                return false;
            }
        } else if (!directory.isDirectory()) {
            log.info("路径存在但不是目录: {}" , directory.getAbsolutePath());
            return false;
        }

        // 额外检查：确保目录可写
        if (!directory.canWrite()) {
            log.info("目录存在但不可写: {}", directory.getAbsolutePath());
            return false;
        }

        return true;
    }

    /**
     * 输出web config json
     */
    @Override
    @Async
    public void webConfig() throws IOException {
        //获取web config
        QueryWrapper<SysConfig> config = new QueryWrapper<>();
        config.eq("config_type", "web_config").ne("config_state", 2);
        List<SysConfig> list = configMapper.selectList(config);
        //获取路径
        QueryWrapper<SysConfig> path = new QueryWrapper<>();
        path.eq("config_key", "web-config");
        SysConfig url = configMapper.selectOne(path);
        if (url.getConfigState().equals(2) && StringUtils.isEmpty(url.getConfigValue()))
            return;
        //处理数据
        Map<String, String> configMap = new HashMap<>();
        for (SysConfig m : list) {
            configMap.put(CaseUtils.toCamelCase(m.getConfigKey(), false, '-'), m.getConfigValue());
        }
        // 确保目录存在
        String filePath = url.getConfigValue();
        File directory = new File(filePath);
        if (!ensureDirectoryExists(directory)) {
            log.info("无法创建目录: {}", filePath);
        } else {
            File targetFile = new File(filePath + CaseUtils.toCamelCase(url.getConfigKey(), false, '-')+".json");
//            objectMapper.writerWithDefaultPrettyPrinter().writeValue(targetFile, configMap);
            objectMapper.writeValue(targetFile, configMap);
            log.info("JSON文件生成成功: {}", targetFile.getAbsolutePath());
            log.info("文件大小: {}字节" , targetFile.length() );
            // 写入JSON文件
        }

    }


}
