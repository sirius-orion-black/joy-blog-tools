package com.joy.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joy.common.Result;
import com.joy.dto.auth.EmailVerifyDto;
import com.joy.dto.user.*;
import com.joy.entity.common.miniProgram.MiniProgramInfo;
import com.joy.entity.sysConfig.SysCloudMail;
import com.joy.entity.sysConfig.SysConfig;
import com.joy.entity.sysConfig.SysConfigMail;
import com.joy.entity.common.user.User;
import com.joy.enums.http.RequestCodeMessage;
import com.joy.mapper.common.miniProgram.MiniProgramInfoMapper;
import com.joy.mapper.sysConfig.SysCloudMailMapper;
import com.joy.mapper.sysConfig.SysConfigMailMapper;
import com.joy.mapper.sysConfig.SysConfigMapper;
import com.joy.mapper.common.user.UserMapper;
import com.joy.service.AuthService;
import com.joy.utils.IpRegionUtil;
import com.joy.utils.UserVerifyUtil;
import com.joy.utils.VerifyCodeUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static com.joy.enums.http.RequestCodeMessage.PHONE_EMAIL_INCORRECT;

@Slf4j
@Service
public class AuthServiceImpl extends ServiceImpl<UserMapper, User> implements AuthService {

    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Autowired
    private SysConfigMailMapper configMailMapper;

    @Autowired
    private SysCloudMailMapper cloudMailMapper;

    @Autowired
    private MiniProgramInfoMapper programMapper;


    //邮箱配置
    private volatile SysConfig cachedConfig;
    //阿里云邮箱
    private volatile SysCloudMail cachedCloudMail;
    private volatile Long cachedCloudMailId = null;
    //139邮箱
    private volatile SysConfigMail cachedConfigMail;
    private volatile Long cachedConfigMailId = null;

    private volatile long cacheExpireTime = 0;
    private static final long CACHE_MS = TimeUnit.MINUTES.toMillis(30);
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * 获取是否启用云邮箱配置
     */
    public SysConfig getMailConfig() {
        if (cachedConfig != null && System.currentTimeMillis() < cacheExpireTime) {
            return cachedConfig;
        }
        lock.lock();
        try {
            if (cachedConfig != null && System.currentTimeMillis() < cacheExpireTime) {
                return cachedConfig;
            }
            SysConfig config = sysConfigMapper.selectOne(
                    new LambdaQueryWrapper<SysConfig>()
                            .eq(SysConfig::getConfigKey, "is_cloud_email")
                            .last("LIMIT 1"));
            if (config == null) {
                RequestCodeMessage.ENABLE_CLOUD_EMAIL_CONFIG.throwIt();
                return null;
            }

            cachedConfig = config;
            cacheExpireTime = System.currentTimeMillis() + CACHE_MS;
            return cachedConfig;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取阿里云邮箱的配置
     */
    public SysCloudMail getCloudMail() {
        if (cachedCloudMail != null && System.currentTimeMillis() < cacheExpireTime) {
            return cachedCloudMail;
        }
        lock.lock();
        try {
            if (cachedCloudMail != null && System.currentTimeMillis() < cacheExpireTime) {
                return cachedCloudMail;
            }
            SysCloudMail mail = cloudMailMapper.selectOne(
                    new LambdaQueryWrapper<SysCloudMail>()
                            .eq(SysCloudMail::getState, 1)
                            .last("LIMIT 1"));
            if (mail == null) {
                log.info("============>>>>>>>>>阿里云邮箱未配");
                RequestCodeMessage.EMAIL_NOT_CONFIG.throwIt();
                return null;
            }
            cachedCloudMail = mail;
            cachedCloudMailId = mail.getId();
            cacheExpireTime = System.currentTimeMillis() + CACHE_MS;
            return cachedCloudMail;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取139邮箱的配置
     */
    public SysConfigMail getConfigMail() {
        if (cachedConfigMail != null && System.currentTimeMillis() < cacheExpireTime) {
            return cachedConfigMail;
        }
        lock.lock();
        try {
            if (cachedConfigMail != null && System.currentTimeMillis() < cacheExpireTime) {
                return cachedConfigMail;
            }
            SysConfigMail mail = configMailMapper.selectOne(
                    new LambdaQueryWrapper<SysConfigMail>()
                            .eq(SysConfigMail::getState, 1)
                            .last("LIMIT 1"));
            if (mail == null) {
                log.info("============>>>>>>>>>139邮箱未配");
                RequestCodeMessage.EMAIL_NOT_CONFIG.throwIt();
                return null;
            }
            cachedConfigMail = mail;
            cachedConfigMailId = mail.getId();
            cacheExpireTime = System.currentTimeMillis() + CACHE_MS;
            return cachedConfigMail;
        } finally {
            lock.unlock();
        }
    }

    /**
     * @param loginInfo  登录信息
     * @param prefixCode redis prefixCode
     */
    @Override
    public Result<Map<String, Integer>> emailInfo(EmailVerifyDto loginInfo, String prefixCode) throws Exception {
        //获取是否启用云邮箱配置
        SysConfig cloudEmail = this.getMailConfig();
        // 生成验证码
        String verificationCode = RandomStringUtils.randomAlphanumeric(6);
        boolean bl;
        Integer validTime;
        if (cloudEmail.getConfigValue().equals("yes")) {
            //云邮箱我这里用了阿里云邮箱推送
            SysCloudMail mail = this.getCloudMail();
            //发送邮件
            bl = VerifyCodeUtil.sendCloudEmail(verificationCode, loginInfo.getEmail(), mail);
            validTime = mail.getValidTime();
        } else {
            //获取配置邮箱，这里我用的是139邮箱
            SysConfigMail mail = this.getConfigMail();
            //发送邮件
            bl = VerifyCodeUtil.sendEmail(
                    verificationCode,
                    VerifyCodeUtil.buildEmailVerifyContent(mail, verificationCode),
                    loginInfo.getEmail(),
                    mail);
            validTime = mail.getValidTime();
        }
        String message = RequestCodeMessage.VERIFICATION_CODE_SENT_AGAIN.getMessage();
        if (bl) {
            message = RequestCodeMessage.VERIFICATION_CODE_SENT.getMessage();
            VerifyCodeUtil.markSent(loginInfo.getEmail());
            VerifyCodeUtil.saveCodeAsync(loginInfo.getEmail(), prefixCode, verificationCode, validTime);
        }
        Map<String, Integer> result = new HashMap<>();
        result.put("validTime", validTime);
        return Result.success(result, message);
    }

    /**
     * 邮箱验证
     *
     * @param loginInfo 用户信息
     * @param request   request
     * @return 返回验证码相关信息
     */
    @Override
    public Result<Map<String, Integer>> emailCode(EmailVerifyDto loginInfo, HttpServletRequest request) throws Exception {
        if (StringUtils.isBlank(loginInfo.getEmail()) || !UserVerifyUtil.emailFormat(loginInfo.getEmail())) {//判断邮箱
            RequestCodeMessage.EMAIL_FORMAT_INCORRECT.throwIt();
        }
        String ip = IpRegionUtil.getClientIpAddress(request);

        //查验邮箱发送速率，并标记
        VerifyCodeUtil.checkSendAllowed(loginInfo.getEmail(), ip);
        if (!loginInfo.getAction().equals("register")) {

            //校验邮箱或者账户是否存在
            QueryWrapper<User> query = new QueryWrapper<>();
            query.eq("email", loginInfo.getEmail());
            User user = this.getOne(query);
            if (user == null) {
                RequestCodeMessage.ACCOUNT_EMAIL_INCORRECT.throwIt();
            }
            log.info("user=>>>>>>>>>{}", JSON.toJSONString(user));
        }
        return this.emailInfo(loginInfo, "vc:vf:mini:email:code:");
    }

    /**
     * 用户注册
     *
     * @param user 用户注册信息
     * @return 返回是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> register(RegisterDto user) {
        if (!UserVerifyUtil.phoneFormat(user.getPhone())) {
            RequestCodeMessage.PHONE_NUMBER_INCORRECT.throwIt();//手机号格式不正确
        }
        if (!UserVerifyUtil.passwordFormat(user.getPassword())) {
            RequestCodeMessage.PASSWORD_NUMBER_INCORRECT.throwIt();//密码格式不正确
        }

        VerifyCodeUtil.verifyCode(user.getEmail(), "vc:vf:mini:email:code:", user.getCode(), user.getValidTime());
        List<User> existUsers = this.list(
                new LambdaQueryWrapper<User>()
                        .eq(User::getPhone, user.getPhone())
                        .or()
                        .eq(User::getEmail, user.getEmail())
                        .select(User::getPhone, User::getEmail)
        );
        if (!existUsers.isEmpty()) {
            // 遍历结果精确判断是哪个重复
            for (User u : existUsers) {
                if (user.getPhone().equals(u.getPhone())) {
                    RequestCodeMessage.PHONE_ALREADY_EXISTS.throwIt();
                }
                if (user.getEmail().equals(u.getEmail())) {
                    RequestCodeMessage.EMAIL_ALREADY_EXISTS.throwIt();
                }
            }
        }
        // 构建用户
        User regUser = new User();
        regUser.setPhone(user.getPhone());
        regUser.setEmail(user.getEmail());
        regUser.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        regUser.setNickname("用户" + user.getPhone().substring(7));
        regUser.setSex(3); // 默认未知
        regUser.setState(1); // 正常（因为邮箱验证码已通过，视为已验证）
        regUser.setEmailVerifiedTime(new Date());
        regUser.setCreateTime(new Date());
        regUser.setUpdateTime(new Date());
        this.save(regUser);
        return Result.success();
    }

    /**
     * 用户登录
     *
     * @param loginInfo 用户登录信息
     * @return 返回用户基础信息
     */
    @Override
    public Result<UserInfoDto> login(LoginDto loginInfo) {
        if (StringUtils.isBlank(loginInfo.getPhone()) || StringUtils.isEmpty(loginInfo.getPassword())) {//判断用户名密码不为空
            RequestCodeMessage.USERNAME_PASSWORD_INCORRECT.throwIt();
        }
        if (!UserVerifyUtil.passwordFormat(loginInfo.getPassword())) {
            RequestCodeMessage.PASSWORD_NUMBER_INCORRECT.throwIt();//密码格式不正确
        }
        User user = this.getOne(
                new LambdaQueryWrapper<User>().eq(User::getPhone, loginInfo.getPhone())
        );
        loginVerify(loginInfo, user);

        StpUtil.login(user.getId());
        StpUtil.getSession().set("userInfo", user);

        return Result.success(getUserInfoDto(user));
    }

    private UserInfoDto getUserInfoDto(User user) {//拼装用户信息
        UserInfoDto userInfo = new UserInfoDto();
        userInfo.setToken(StpUtil.getTokenValue());
        userInfo.setId(user.getId());
        userInfo.setNickname(user.getNickname());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setPhone(user.getPhone());
        userInfo.setEmail(user.getEmail());
        userInfo.setSex(user.getSex());
        userInfo.setState(user.getState());
        return userInfo;
    }

    /**
     * 重置密码
     *
     * @param user 用户信息
     * @return 返回是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> resetPassword(ResetPasswordDto user) {
        if (!UserVerifyUtil.passwordFormat(user.getNewPassword())) {
            RequestCodeMessage.PASSWORD_NUMBER_INCORRECT.throwIt();//密码格式不正确
        }
        VerifyCodeUtil.verifyCode(user.getEmail(), "vc:vf:mini:email:code:", user.getCode(), user.getValidTime());
        User existUsers = this.getOne(
                new LambdaQueryWrapper<User>().eq(User::getEmail, user.getEmail())
        );
        if (existUsers == null) {
            RequestCodeMessage.USER_NOT_EXIST.throwIt();
        }
        if (BCrypt.checkpw(user.getNewPassword(), existUsers.getPassword())) {
            RequestCodeMessage.NEW_PASSWORD_CANNOT_OLD_PASSWORD.throwIt();
        }
        existUsers.setPassword(BCrypt.hashpw(user.getNewPassword(), BCrypt.gensalt()));
        existUsers.setUpdateTime(new Date());
        this.updateById(existUsers);
        StpUtil.logout(existUsers.getId());
        return Result.success();
    }

    /**
     * 登录校验
     *
     * @param loginInfo 用户登录信息
     * @param user      用户信息
     */
    private void loginVerify(LoginDto loginInfo, User user) {
        if (user == null || !BCrypt.checkpw(loginInfo.getPassword(), user.getPassword())) {
            RequestCodeMessage.USERNAME_PASSWORD_INCORRECT.throwIt();
            return;
        }
        if (user.getState().equals(2))
            RequestCodeMessage.ACCOUNT_BANNED.throwIt();
    }


    /**
     * 微信一键登录
     *
     * @param req //微信回传code
     * @return 返回是否需要绑定或者用户信息
     */
    @Override
    public Result<UserInfoDto> wxLogin(Map<String, String> req) {
        String code = req.get("code");
        if (code == null || code.isBlank())
            RequestCodeMessage.CODE_CANNOT_EMPTY.throwIt();

        MiniProgramInfo programInfo = programMapper.selectOne(new LambdaQueryWrapper<MiniProgramInfo>().eq(MiniProgramInfo::getType, 1).eq(MiniProgramInfo::getIsDeleted, 0));
        if (programInfo == null)
            RequestCodeMessage.INVALID_OPERATION.throwIt();
        String url = String.format(
                "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                programInfo.getAppId(), programInfo.getAppSecret(), code);
        JSONObject json = JSONUtil.parseObj(HttpUtil.get(url));
        String openid = json.getStr("openid");


        log.info("微信登录结果查看,{}", openid);
        if (openid == null || openid.isBlank()) {
            log.error("微信登录失败: {}", json);
            return Result.fail(HttpStatus.NOT_ACCEPTABLE, "微信登录失败");
        }
        User user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getOpenid, openid));

        if (user != null) {
            // 已绑定，直接生成 Token 登录
            StpUtil.login(user.getId());
            StpUtil.getSession().set("userInfo", user);
            return Result.success(getUserInfoDto(user));
        } else {
            String wxToken = UUID.randomUUID().toString().replace("-", "");
            VerifyCodeUtil.saveCodeAsync(wxToken, "vc:vf:wx:bind:code:", openid, 600);
            UserInfoDto userInfo = new UserInfoDto();
            userInfo.setToken(wxToken);
            userInfo.setNeedBind(true);
            return Result.success(userInfo);
        }
    }

    /**
     * 微信用户绑定
     *
     * @param req 微信用户与用户绑定
     * @return 返回用户信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<UserInfoDto> wxBind(WxBindDto req) {
        if (!UserVerifyUtil.phoneFormat(req.getPhone())) {
            RequestCodeMessage.PHONE_NUMBER_INCORRECT.throwIt();//手机号格式不正确
        }
        String redisKey = "vc:vf:wx:bind:code:" + req.getWxToken();
        String openid = VerifyCodeUtil.getValue(redisKey);
        VerifyCodeUtil.verifyCode(req.getEmail(), "vc:vf:mini:email:code:", req.getCode(), req.getValidTime());
        List<User> list = this.list(new LambdaQueryWrapper<User>().eq(User::getEmail, req.getEmail()).or().eq(User::getPhone, req.getPhone()));
        User user = null;
        if (list.isEmpty()) {
            user = new User();
            user.setPhone(req.getPhone());
            user.setEmail(req.getEmail());
            user.setNickname("用户" + req.getPhone().substring(7));
            user.setSex(3); // 默认未知
            user.setState(1); // 正常（因为邮箱验证码已通过，视为已验证）
            user.setEmailVerifiedTime(new Date());
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            user.setOpenid(openid);
            this.save(user);
        }
        else if (list.size() > 1)
            PHONE_EMAIL_INCORRECT.throwIt();
        else {
            user = list.get(0);
            if(!user.getEmail().equals(req.getEmail()) || !user.getPhone().equals(req.getPhone()))
                RequestCodeMessage.PHONE_EMAIL_BIND.throwIt();
            user.setOpenid(openid);
            this.updateById(user);
        }
        StpUtil.login(user.getId());
        StpUtil.getSession().set("userInfo", user);
        return Result.success(getUserInfoDto(user));
    }

}
