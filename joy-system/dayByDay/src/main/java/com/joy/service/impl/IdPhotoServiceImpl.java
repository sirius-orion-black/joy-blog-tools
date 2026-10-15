package com.joy.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joy.common.Result;
import com.joy.dto.dayByDay.IdPhotoGenerateDto;
import com.joy.dto.dayByDay.IdPhotoHumanMattingDto;
import com.joy.entity.dayByDay.DayPhotoRecord;
import com.joy.entity.dayByDay.DayPhotoSize;
import com.joy.entity.dayByDay.DayPhotoSuit;
import com.joy.entity.infra.FilesRecord;
import com.joy.enums.http.RequestCodeMessage;
import com.joy.mapper.dayByDay.DayPhotoRecordMapper;
import com.joy.mapper.dayByDay.DayPhotoSizeMapper;
import com.joy.mapper.dayByDay.DaySuitConfigMapper;
import com.joy.mapper.infra.FilesRecordMapper;
import com.joy.service.FileService;
import com.joy.service.IdPhotoService;
import com.joy.utils.Base64MultipartFileUtil;
import com.joy.utils.FileUtil;
import com.joy.utils.RedisUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.indexer.DoubleIndexer;
import org.bytedeco.javacpp.indexer.Indexer;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class IdPhotoServiceImpl extends ServiceImpl<DayPhotoRecordMapper, DayPhotoRecord> implements IdPhotoService {

    private final RedisUtil redisUtil;

    private final DayPhotoSizeMapper sizeMapper;
    private final DaySuitConfigMapper suitMapper;
    private final FilesRecordMapper filesRecordMapper;

    private final FileService fileService;

    private CascadeClassifier faceDetector;

    @PostConstruct
    public void init() throws Exception {
        // 加载OpenCV内置Haar人脸检测器
        InputStream is = new ClassPathResource("haarcascade_frontalface_alt2.xml").getInputStream();
        File tempModel = File.createTempFile("haarcascade", ".xml");
        Files.copy(is, tempModel.toPath(), StandardCopyOption.REPLACE_EXISTING);
        faceDetector = new CascadeClassifier(tempModel.getAbsolutePath());
        tempModel.deleteOnExit();
    }
    /**
     * 获取证件照尺寸配置
     *
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

        redisUtil.sets(key, list, 3600);
        return Result.success(list);
    }

    /**
     * 获取证件服装
     *
     * @param gender 性别
     * @return 返回服装列表
     */
    @Override
    public Result<List<DayPhotoSuit>> photoSuits(Integer gender) {
        LambdaQueryWrapper<DayPhotoSuit> suits = new LambdaQueryWrapper<DayPhotoSuit>()
                .eq(DayPhotoSuit::getIsEnabled, 1)
                .orderByAsc(DayPhotoSuit::getSortOrder);
        if (gender != null) {
            suits.and(q -> q.eq(DayPhotoSuit::getGender, gender).or().eq(DayPhotoSuit::getGender, 0));
        }
        List<DayPhotoSuit> list = suitMapper.selectList(suits);
        list.forEach(s -> {
            if (s.getImagePath() != null) {
                s.setImagePath(null);
            }
        });
        return Result.success(list);
    }

    /**
     * 构建 RestClient（60s 读超时，适配 Python 证件照处理耗时）
     *
     * @return 返回服装列表 RestClient
     */
    private RestClient getRestClient() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(5));
        factory.setReadTimeout(Duration.ofSeconds(60));
        return RestClient.builder()
                .baseUrl("http://192.168.10.129:8080")
                .requestFactory(factory)
                .build();
    }

    /**
     *
     * @param file 证件照原始文件
     * @param fileType 类型
     * @param platform 平台
     * @return 返回预览图
     */
    @Override
    public Result<Map<String, Object>> photoPreview(MultipartFile file, String fileType, String platform) throws IOException {
        Map<String, String> result = fileService.uploadFile(file, fileType, platform).getData();
        Long recordId =Long.valueOf(result.get("id"));
        Map<String, Object> res = new HashMap<>();

        DayPhotoRecord existing = this.getOne(new LambdaQueryWrapper<DayPhotoRecord>().eq(DayPhotoRecord::getOriginId, recordId));
        if (existing == null) {
            RestClient restClient = this.getRestClient();

            // 构建 Multipart 表单（严格对应 curl 的所有 -F 参数）
            MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
            //动态获取media type
            MediaType mediaType = Optional.ofNullable(file.getContentType())
                    .map(MediaType::parseMediaType)
                    .orElse(MediaType.APPLICATION_OCTET_STREAM);
            // 👇 直接使用 MultipartFile 的资源流，无需转存临时文件
            bodyBuilder.part("input_image", new InputStreamResource(file.getInputStream()))
                    .filename(Objects.requireNonNull(file.getOriginalFilename()))
                    .contentType(mediaType);
            IdPhotoHumanMattingDto idPhotoResult = restClient.post()
                    .uri("/human_matting")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(bodyBuilder.build())
                    .retrieve()
                    .body(IdPhotoHumanMattingDto.class);

            if (idPhotoResult == null || !idPhotoResult.status() || idPhotoResult.imageBase64() == null) {
                //AI 证件照生成失败
                RequestCodeMessage.INVALID_OPERATION.throwIt();
                return null;
            }
            String timestamp = String.valueOf(System.currentTimeMillis());
            MultipartFile hdFile = new Base64MultipartFileUtil(idPhotoResult.imageBase64(),timestamp + "_standard.png");
            Map<String, String> resultHd = fileService.uploadFile(hdFile, fileType, platform).getData();
            DayPhotoRecord record = new DayPhotoRecord();
            record.setUserId(StpUtil.getLoginIdAsLong());
            record.setOriginUrl(result.get("url"));
            record.setOriginId(recordId);
            record.setFgUrl(resultHd.get("url"));
            record.setFgId(Long.valueOf(resultHd.get("id")));
            this.save(record);
            res.put("url",record.getFgUrl());
            res.put("id",record.getId());
        }else {
            res.put("url",existing.getFgUrl());
            res.put("id",existing.getId());
        }
        return Result.success(res);
    }


    /**
     * 【核心修复】基于人脸检测推算颈部中心点
     * 证件照场景下，人脸位置极其稳定，远比Alpha轮廓可靠
     */
    private Point detectNeckByFace(Mat rgba) {
        try (Mat gray = new Mat();
             RectVector faces = new RectVector()) {

            opencv_imgproc.cvtColor(rgba, gray, opencv_imgproc.COLOR_BGRA2GRAY);
            faceDetector.detectMultiScale(gray, faces, 1.1, 5, 0,
                    new Size(50, 50), new Size());

            if (faces.size() > 0) {
                Rect face = faces.get(0);
                // 颈部中心 = 人脸底部中心 + 脸高*20%（下巴到领口的距离）
                int neckX = face.x() + face.width() / 2;
                int neckY = face.y() + face.height() + (int)(face.height() * 0.2);
                return new Point(neckX, neckY);
            }
        }

        // 人脸检测失败时降级使用图像比例
        return new Point(rgba.cols() / 2, (int)(rgba.rows() * 0.62));
    }

    /**
     * 基于颈部中心点估算肩宽
     * 【修复】彻底解决 1274 原生异常：增加严格的边界钳制与空值防御
     */
    private double estimateShoulderWidth(Mat rgba, Point neckCenter) {
        try (Mat alpha = new Mat()) {
            opencv_core.extractChannel(rgba, alpha, 3);

            // 【核心修复1】严格钳制Y坐标，防止 row() 越界触发 1274
            int y = neckCenter.y() + 30;
            y = Math.max(0, Math.min(y, alpha.rows() - 1));

            // 【核心修复2】防御性检查，避免对空矩阵创建 Indexer
            if (alpha.empty() || alpha.cols() == 0) {
                return rgba.cols() * 0.65;
            }

            try (Mat rowMat = alpha.row(y);
                 org.bytedeco.javacpp.indexer.UByteIndexer indexer = rowMat.createIndexer()) {

                // 【核心修复3】缓存 cols 避免循环内重复调用 JNI
                int cols = alpha.cols();
                int leftBound = 0;
                int rightBound = cols - 1;
                boolean foundLeft = false;
                boolean foundRight = false;

                for (int x = 0; x < cols; x++) {
                    if (indexer.get(x) > 128) {
                        leftBound = x;
                        foundLeft = true;
                        break;
                    }
                }
                for (int x = cols - 1; x >= 0; x--) {
                    if (indexer.get(x) > 128) {
                        rightBound = x;
                        foundRight = true;
                        break;
                    }
                }

                // 只有左右边界都找到了有效像素，才认为采样成功
                if (foundLeft && foundRight) {
                    double width = rightBound - leftBound;
                    if (width > rgba.cols() * 0.2 && width < rgba.cols() * 0.9) {
                        return width;
                    }
                }
            }
        } catch (Exception e) {
            // 捕获所有原生异常，记录日志并优雅降级，绝不让 1274 向上抛出
            System.err.println("肩宽估算异常，已降级使用默认值: " + e.getMessage());
        }

        // 终极降级兜底
        return rgba.cols() * 0.65;
    }

    /**
     * 缩放并平移服装素材
     * 【修复】增加 dstSize 参数，确保变换后的西装与人物图尺寸一致
     */
    private void resizeAndTranslate(Mat src, Mat dst, double scale,
                                    Point targetCenter, Point srcAnchor, Size dstSize) {
        try (Mat transformMat = new Mat(2, 3, opencv_core.CV_64F)) {
            double tx = targetCenter.x() - (srcAnchor.x() * scale);
            double ty = targetCenter.y() - (srcAnchor.y() * scale);

            try (Indexer indexer = transformMat.createIndexer()) {
                DoubleIndexer di = (DoubleIndexer) indexer;
                di.put(0, 0, scale); di.put(0, 1, 0.0); di.put(0, 2, tx);
                di.put(1, 0, 0.0);   di.put(1, 1, scale); di.put(1, 2, ty);
            }

            // 【关键】输出尺寸必须使用 dstSize（人物图尺寸），而非 src.size()
            opencv_imgproc.warpAffine(src, dst, transformMat, dstSize,
                    opencv_imgproc.INTER_LINEAR,
                    opencv_core.BORDER_CONSTANT,
                    new Scalar(0.0, 0.0, 0.0, 0.0));
        }
    }

    /**
     * Alpha 通道混合 (将衣服贴到人像上)
     */
    private Mat alphaBlend(Mat base, Mat overlay) {
        Mat result = base.clone();
        Mat finalOverlay = overlay;
        boolean resized = false;
        if (overlay.rows() != base.rows() || overlay.cols() != base.cols()) {
            finalOverlay = new Mat();
            opencv_imgproc.resize(overlay, finalOverlay, base.size());
            resized = true;
        }

        try (MatVector baseCh = new MatVector();
             MatVector overCh = new MatVector();
             Mat alphaF = new Mat();
             Mat invAlpha = new Mat()) {

            opencv_core.split(base, baseCh);
            opencv_core.split(finalOverlay, overCh);

            overCh.get(3).convertTo(alphaF, opencv_core.CV_32F, 1.0 / 255.0, 0.0);

            try (MatExpr expr = opencv_core.subtract(new Scalar(1.0, 1.0, 1.0, 1.0), alphaF);
                 Mat temp = expr.asMat()) {
                temp.copyTo(invAlpha);
            }

            for (int i = 0; i < 3; i++) {
                try (Mat bf = new Mat(); Mat of = new Mat();
                     Mat bp = new Mat(); Mat op = new Mat();
                     Mat rc = new Mat(); Mat ru = new Mat()) {

                    baseCh.get(i).convertTo(bf, opencv_core.CV_32F, 1.0, 0.0);
                    overCh.get(i).convertTo(of, opencv_core.CV_32F, 1.0, 0.0);
                    opencv_core.multiply(bf, invAlpha, bp);
                    opencv_core.multiply(of, alphaF, op);
                    opencv_core.add(bp, op, rc);
                    rc.convertTo(ru, opencv_core.CV_8U, 1.0, 0.0);
                    ru.copyTo(baseCh.get(i));
                }
            }
            opencv_core.merge(baseCh, result);
        } finally {
            if (resized) finalOverlay.release();
        }
        return result;
    }

    /**
     * 将Mat编码为Base64字符串
     */
    private String matToBase64(Mat mat) throws IOException {
        Path tempFile = Files.createTempFile("compose_", ".png");
        try {
            opencv_imgcodecs.imwrite(tempFile.toString(), mat);
            byte[] imageBytes = Files.readAllBytes(tempFile);
            return Base64.getEncoder().encodeToString(imageBytes);
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    /**
     * 核心换装接口
     * @param photo 证件照处理参数
     * @return 返回输出换装结果
     */
    @Override
    public Result<Map<String, Object>> photoChangeSuit(IdPhotoGenerateDto photo) {
        if(Objects.nonNull(photo.getSuitId()) && Objects.nonNull(photo.getId())){
            DayPhotoRecord record = this.getById(photo.getId());
            if(record == null){
                RequestCodeMessage.INFORMATION_INCOMPLETE.throwIt();
                return null;
            }
            FilesRecord fgFile = filesRecordMapper.selectById(record.getFgId());
            DayPhotoSuit suit = suitMapper.selectById(photo.getSuitId());
            if(fgFile == null || suit == null){
                RequestCodeMessage.INFORMATION_INCOMPLETE.throwIt();
                return null;
            }
            String personImagePath =  fgFile.getPath();
            String suitImagePath = suit.getImagePath();

            try (Mat personRgba = opencv_imgcodecs.imread(personImagePath, opencv_imgcodecs.IMREAD_UNCHANGED);
                 Mat suitRgba = opencv_imgcodecs.imread(suitImagePath, opencv_imgcodecs.IMREAD_UNCHANGED)) {

                if (personRgba.empty() || suitRgba.empty()) {
                    throw new RuntimeException("图像读取失败");
                }
                if (personRgba.channels() != 4 || suitRgba.channels() != 4) {
                    throw new RuntimeException("图像必须是4通道BGRA透明底PNG");
                }

                Point neckCenter = detectNeckByFace(personRgba);
                double personShoulderWidth = estimateShoulderWidth(personRgba, neckCenter);

                // 【核心修复1】缩放比安全钳制，防止西装缩放到不可见或无限大
                double scale = personShoulderWidth / suit.getReferenceShoulderWidth();
                scale = Math.max(0.3, Math.min(scale, 3.0));

                Point targetPoint = new Point(neckCenter.x(), neckCenter.y() + suit.getOffsetY());

                try (Mat transformedSuit = new Mat()) {
                    // 【核心修复2】传入人物图尺寸作为输出画布，而非素材原始尺寸
                    resizeAndTranslate(suitRgba, transformedSuit, scale,
                            targetPoint,
                            new Point(suit.getAnchorX(), suit.getAnchorY()),
                            personRgba.size()); // 👈 新增参数

                    try (Mat result = alphaBlend(personRgba, transformedSuit)) {
                        Map<String, Object> res = new HashMap<>();
                        res.put("url","data:image/jpeg;base64,"+matToBase64(result));
                        return Result.success(res);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("换装合成失败: " + e.getMessage(), e);
            }
        }
        return null;
    }

    @Override
    public Result<DayPhotoRecord> photoProcess(IdPhotoGenerateDto photo) {
        return null;
    }


}
