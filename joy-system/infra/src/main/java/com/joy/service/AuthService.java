package com.joy.service;

import com.joy.common.Result;
import com.joy.dto.auth.EmailVerifyDto;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface AuthService {

    Result<Map<String,Integer>> emailCode(EmailVerifyDto loginInfo, HttpServletRequest request) throws Exception;

    Result<Map<String,Integer>> emailInfo(EmailVerifyDto loginInfo, String key) throws Exception;

}
