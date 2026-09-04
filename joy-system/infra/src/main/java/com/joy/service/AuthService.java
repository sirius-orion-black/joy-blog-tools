package com.joy.service;

import com.joy.common.Result;
import com.joy.dto.common.LoginDto;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface AuthService {

    Result<Map<String,Integer>> emailCode(LoginDto loginInfo, HttpServletRequest request) throws Exception;

    Result<Map<String,Integer>> emailInfo(LoginDto loginInfo, String key) throws Exception;

}
