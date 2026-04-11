package com.joy.service;

import com.joy.common.Result;
import com.joy.entity.sysConfig.SysConfig;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface SysWebConfigService {
    Result<Map<String, List<SysConfig>>> getConfig();

    Result<String> editConfig(SysConfig config) throws IOException;
}
