package com.joy.service;

import com.joy.common.Result;
import com.joy.dto.common.auth.EmailVerifyDto;
import com.joy.dto.common.user.*;
import com.joy.entity.admin.sysConfig.SysCloudCredential;
import jakarta.servlet.http.HttpServletRequest;

import javax.validation.Valid;
import java.util.Map;

public interface AuthService {

    SysCloudCredential getCredential(String bizType, String provider);

    Result<Map<String,Integer>> emailCode(EmailVerifyDto loginInfo, HttpServletRequest request) throws Exception;

    Result<Map<String,Integer>> emailInfo(EmailVerifyDto loginInfo, String key) throws Exception;

    Result<String> register(RegisterDto user);

    Result<UserInfoDto> login(@Valid LoginDto user);

    Result<String> resetPassword(@Valid ResetPasswordDto user);

    Result<UserInfoDto> wxLogin(Map<String, String> req);

    Result<UserInfoDto> wxBind(@Valid WxBindDto req);
}
