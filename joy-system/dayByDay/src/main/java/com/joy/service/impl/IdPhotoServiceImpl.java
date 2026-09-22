package com.joy.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.viapi20230117.Client;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joy.common.Result;
import com.joy.dto.dayByDay.IdPhotoGenerateDto;
import com.joy.entity.admin.sysConfig.SysCloudCredential;
import com.joy.entity.dayByDay.DayPhotoRecord;
import com.joy.entity.dayByDay.DayPhotoSize;
import com.joy.entity.dayByDay.DayPhotoSuit;
import com.joy.mapper.dayByDay.DayPhotoRecordMapper;
import com.joy.mapper.dayByDay.DayPhotoSizeMapper;
import com.joy.mapper.dayByDay.DaySuitConfigMapper;
import com.joy.service.AuthService;
import com.joy.service.FileService;
import com.joy.service.IdPhotoService;
import com.joy.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class IdPhotoServiceImpl extends ServiceImpl<DayPhotoRecordMapper, DayPhotoRecord> implements IdPhotoService {

    private final RedisUtil redisUtil;

    private final DayPhotoSizeMapper sizeMapper;
    private final DaySuitConfigMapper suitMapper;

    private final AuthService authService;
    private final FileService fileService;
    private volatile Client viapiClient;
    private volatile com.aliyun.imageseg20191230.Client imageSegClient;

    /**
     * 获取证件照尺寸配置
     * @return 返回尺寸列表
     */
    @Override
    public Result<List<DayPhotoSize>> photoSizes() {
        String key = "vc:vf:id:photo:sizes";
        List<DayPhotoSize> cached = redisUtil.getList(key);
        if (cached != null) {
            return Result.success(cached);
        }
        List<DayPhotoSize> list = sizeMapper.selectList(
                new LambdaQueryWrapper<DayPhotoSize>()
                        .eq(DayPhotoSize::getIsEnabled, 1)
                        .orderByAsc(DayPhotoSize::getSortOrder));

        redisUtil.sets(key, list,3600);
        return Result.success(list);
    }

    /**
     * 初始化 证件照 Client
     */
    private Client getViapiClient() throws Exception {
        if (viapiClient == null) {
            synchronized (this) {
                if (viapiClient == null) {
                    SysCloudCredential cred = authService.getCredential("ID_PHOTO","ALIYUN");
                    Config config = new Config()
                            .setAccessKeyId(cred.getAccessKeyId())
                            .setAccessKeySecret(cred.getAccessKeySecret());
                    // 从 JSON 扩展配置中读取 endpoint，有默认值兜底

                    String endpoint = "viapi.cn-shanghai.aliyuncs.com";
                    if(cred.getExtraConfig() != null){
                        JSONObject extra = StringUtils.hasText(cred.getExtraConfig())
                                ? JSON.parseObject(cred.getExtraConfig())
                                : new JSONObject();
                        endpoint = org.apache.commons.lang3.StringUtils.isNotBlank(extra.getString("viapi_endpoint")) ? extra.getString("viapi_endpoint") : endpoint;
                    }
                    viapiClient = new Client(config);
                }
            }
        }
        return viapiClient;
    }

    /**
     * 初始化 证件照 服装 Client
     */
    public com.aliyun.imageseg20191230.Client getImageSegClient() {
        if (imageSegClient == null) {
            synchronized (this) {
                if (imageSegClient == null) {
                    try {
                        SysCloudCredential cred = authService.getCredential("ID_PHOTO","ALIYUN");
                        Config config = new Config()
                                .setAccessKeyId(cred.getAccessKeyId())
                                .setAccessKeySecret(cred.getAccessKeySecret());
                        config.endpoint = "imageseg.cn-shanghai.aliyuncs.com";
                        imageSegClient = new com.aliyun.imageseg20191230.Client(config);
                        log.info("ImageSeg Client 初始化成功");
                    } catch (Exception e) {
                        throw new RuntimeException("初始化ImageSeg客户端失败", e);
                    }
                }
            }
        }
        return imageSegClient;
    }


    /**
     * 获取证件服装
     * @param gender 性别
     * @return 返回服装列表
     */
    @Override
    public Result<List<DayPhotoSuit>> photoSuits(Integer gender) {
        LambdaQueryWrapper<DayPhotoSuit> suits= new LambdaQueryWrapper<DayPhotoSuit>()
                .eq(DayPhotoSuit::getIsEnabled, 1)
                .orderByAsc(DayPhotoSuit::getSortOrder);
        if (gender != null) {
            suits.and(q -> q.eq(DayPhotoSuit::getGender, gender).or().eq(DayPhotoSuit::getGender, 0));
        }
        List<DayPhotoSuit> list = suitMapper.selectList(suits);
        list.forEach(s -> {
            if (s.getThumbPath() != null) {
                s.setThumbUrl("https://joyimg.lexujia.com/" + s.getThumbPath());
            }
        });
        return Result.success(list);
    }

    @Override
    public void syncSuits() {
        log.info("开始同步阿里云服装列表...");
    }


    public Result<Map<String, Object>> preview(MultipartFile file) throws IOException {
        return null;
    }

    @Override
    public DayPhotoRecord generate(IdPhotoGenerateDto req) {
        return null;
    }
}
