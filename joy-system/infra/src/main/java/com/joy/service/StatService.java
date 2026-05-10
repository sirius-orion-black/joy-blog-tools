package com.joy.service;

import com.joy.dto.stat.StatTrackDTO;

import java.util.Map;

public interface StatService {
    void recordAccess(String ip, StatTrackDTO stat, Map<String, String> extra);
}
