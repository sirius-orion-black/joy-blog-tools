package com.joy.utils;

import com.joy.dto.utils.IpLocationDto;
import com.joy.dto.utils.IpSummaryDto;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * IP地理位置查询工具类
 * ip2region.xdb 数据库进行IP地址定位
 * 支持代理服务器多IP地址处理
 */
@Component
public class IpRegionUtil {

    private static Searcher searcher;

    /**
     * IP地址正则表达式
     */
    private static final Pattern IP_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
    );

    /**
     * IPv4 CIDR 正则表达式 (例如: 192.168.1.1/24)
     */
    private static final Pattern CIDR_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)/([0-9]|[1-2][0-9]|3[0-2])$"
    );

    /**
     * 初始化
     */
    @PostConstruct
    public void init() throws IOException {
        // 从 classpath 加载 ip2region.xdb 文件
        ClassPathResource resource = new ClassPathResource("ip2region.xdb");
        byte[] cBuff = Searcher.loadContentFromFile(resource.getFile().getAbsolutePath());
        searcher = Searcher.newWithBuffer(cBuff);
    }

    /**
     * 根据IP查询位置信息
     *
     * @param ip (如: 192.168.1.1)
     * @return 格式: 国家|区域|省份|城市|ISP
     * @throws Exception 查询失败时抛出异常
     */
    public static String getRegion(String ip) throws Exception {
        if (searcher == null || Objects.isNull(ip) || ip.trim().isEmpty()) {
            return "未知";
        }

        String cleanIp = extractValidIp(ip.trim());
        if (cleanIp == null) {
            return "未知";
        }

        try {
            return searcher.search(cleanIp);
        } catch (Exception e) {
            throw new Exception("IP地址查询失败: " + e.getMessage(), e);
        }
    }

    /**
     * 解析IP为详细位置对象
     *
     * @param ip IP地址
     * @return IP位置对象
     * @throws Exception 查询失败时抛出异常
     */
    public static IpLocationDto getLocationInfo(String ip) throws Exception {
        String region = getRegion(ip);
        if ("未知".equals(region)) {
            return new IpLocationDto("未知", "未知", "未知", "未知", "未知");
        }

        String[] parts = region.split("\\|");
        if (parts.length >= 5) {
            return new IpLocationDto(
                    parts[0], // 国家
                    parts[1], // 区域
                    parts[2], // 省份
                    parts[3], // 城市
                    parts[4]  // ISP
            );
        }

        return new IpLocationDto("未知", "未知", "未知", "未知", "未知");
    }

    /**
     * 获取IP简要位置（省份+城市）
     *
     * @param ip IP地址
     * @return 简要位置
     * @throws Exception 查询失败时抛出异常
     */
    public static String getSimpleLocation(String ip) throws Exception {
        IpLocationDto info = getLocationInfo(ip);
        StringBuilder sb = new StringBuilder();

        String province = info.getProvince();
        String city = info.getCity();

        if (!"未知".equals(province) && !"0".equals(province) && !"内网IP".equals(province)) {
            sb.append(province);
        }
        if (!"未知".equals(city) && !"0".equals(city) && !"内网IP".equals(city)) {
            sb.append(city);
        }

        return sb.length() > 0 ? sb.toString() : "未知位置";
    }

    /**
     * 检查IP地址是否有效
     *
     * @param ip IP地址
     * @return 是否为有效IP
     */
    public static boolean isValidIp(String ip) {
        if (ip == null || ip.trim().isEmpty()) {
            return false;
        }

        return IP_PATTERN.matcher(ip.trim()).matches();
    }

    /**
     * 检查CIDR是否有效 (例如: 192.168.1.0/24)
     *
     * @param cidr CIDR字符串
     * @return 是否为有效CIDR
     */
    public static boolean isValidCidr(String cidr) {
        if (cidr == null || cidr.trim().isEmpty()) {
            return false;
        }

        return CIDR_PATTERN.matcher(cidr.trim()).matches();
    }

    /**
     * 提取有效的IP地址（多个IP）
     *
     * @param ipString 多个IP的字符串
     * @return 有效的IP地址，找不到返回null
     */
    public static String extractValidIp(String ipString) {
        if (ipString == null || ipString.trim().isEmpty()) {
            return null;
        }

        // 分割可能含多个IP的字符串
        String[] ips = ipString.split("[,;\\s]+");

        for (String ip : ips) {
            ip = ip.trim();
            if (isValidIp(ip) && !isPublicIp(ip)) {
                return ip;
            }
        }

        // 没有找到公网IP，返回第一个有效的IP
        for (String ip : ips) {
            ip = ip.trim();
            if (isValidIp(ip)) {
                return ip;
            }
        }

        return null;
    }

    /**
     * 是否为公网IP地址（非私有IP）
     *
     * @param ip IP地址
     * @return 是否为公网IP
     */
    public static boolean isPublicIp(String ip) {
        return !isPrivateIp(ip);
    }

    /**
     * 是否为私有IP地址
     *
     * @param ip IP地址
     * @return 是否为私有IP
     */
    public static boolean isPrivateIp(String ip) {
        if (!isValidIp(ip)) {
            return false;
        }

        String[] parts = ip.split("\\.");
        int firstOctet = Integer.parseInt(parts[0]);
        int secondOctet = Integer.parseInt(parts[1]);

        // 10.x.x.x
        if (firstOctet == 10) return true;
        // 172.16.x.x - 172.31.x.x
        if (firstOctet == 172 && secondOctet >= 16 && secondOctet <= 31) return true;
        // 192.168.x.x
        if (firstOctet == 192 && secondOctet == 168) return true;
        // 127.x.x.x (localhost)
        if (firstOctet == 127) return true;
        // 0.x.x.x
        return firstOctet == 0;
    }

    /**
     * IP是否在指定的CIDR范围内
     *
     * @param ip IP地址
     * @param cidr CIDR范围 (例如: 192.168.1.0/24)
     * @return 是否在范围内
     */
    public static boolean isIpInRange(String ip, String cidr) {
        if (!isValidIp(ip) || !isValidCidr(cidr)) {
            return false;
        }

        try {
            String[] parts = cidr.split("/");
            String networkAddress = parts[0];
            int prefixLength = Integer.parseInt(parts[1]);

            long ipLong = ipToLong(ip);
            long networkLong = ipToLong(networkAddress);
            long subnetMask = -(1L << (32 - prefixLength));

            return (ipLong & subnetMask) == (networkLong & subnetMask);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * IP地址转换为长整型
     *
     * @param ip IP地址
     * @return 长整型表示
     */
    public static long ipToLong(String ip) {
        if (!isValidIp(ip)) {
            return 0;
        }

        String[] octets = ip.split("\\.");
        long result = 0;
        for (int i = 0; i < 4; i++) {
            result = (result << 8) + Integer.parseInt(octets[i]);
        }
        return result;
    }

    /**
     * 客户端真实IP地址（多种代理头）
     *
     * @param request HttpServletRequest
     * @return 客户端真实IP地址
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        String ip = null;

        // 按优先级检查各种HTTP头
        List<String> headers = Arrays.asList(
                "X-Forwarded-For",
                "X-Real-IP",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_X_FORWARDED_FOR",
                "HTTP_X_FORWARDED",
                "HTTP_X_CLUSTER_CLIENT_IP",
                "HTTP_CLIENT_IP",
                "HTTP_FORWARDED_FOR",
                "HTTP_FORWARDED",
                "X-Cluster-Client-IP",
                "X-Forwarded-Proto",
                "X-Originating-IP",
                "CF-Connecting-IP",
                "True-Client-IP"
        );

        for (String header : headers) {
            ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                // 提取第一个有效IP
                String validIp = extractValidIp(ip);
                if (validIp != null) {
                    return validIp;
                }
            }
        }

        // 如果HTTP头都没有找到有效IP，则使用远程地址
        ip = request.getRemoteAddr();
        if (isValidIp(ip) && isPublicIp(ip)) {
            return ip;
        }

        // 最后使用本地回环地址
        return "127.0.0.1";
    }

    /**
     * IP是否为本地回环地址
     *
     * @param ip IP地址
     * @return 是否为回环地址
     */
    public static boolean isLoopbackIp(String ip) {
        if (!isValidIp(ip)) {
            return false;
        }

        return ip.startsWith("127.");
    }

    /**
     * IP是否为链路本地地址
     *
     * @param ip IP地址
     * @return 是否为链路本地地址
     */
    public static boolean isLinkLocalIp(String ip) {
        if (!isValidIp(ip)) {
            return false;
        }

        return ip.startsWith("169.254.");
    }

    /**
     * IP是否为组播地址
     *
     * @param ip IP地址
     * @return 是否为组播地址
     */
    public static boolean isMulticastIp(String ip) {
        if (!isValidIp(ip)) {
            return false;
        }

        int firstOctet = Integer.parseInt(ip.split("\\.")[0]);
        return firstOctet >= 224 && firstOctet <= 239;
    }

    /**
     * IP地址的类别 (A, B, C, D, E)
     *
     * @param ip IP地址
     * @return IP类别
     */
    public static String getIpClass(String ip) {
        if (!isValidIp(ip)) {
            return "Invalid";
        }

        int firstOctet = Integer.parseInt(ip.split("\\.")[0]);

        if (firstOctet >= 1 && firstOctet <= 126) {
            return "A";
        } else if (firstOctet >= 128 && firstOctet <= 191) {
            return "B";
        } else if (firstOctet >= 192 && firstOctet <= 223) {
            return "C";
        } else if (firstOctet >= 224 && firstOctet <= 239) {
            return "D";
        } else if (firstOctet >= 240 && firstOctet <= 255) {
            return "E";
        }

        return "Unknown";
    }

    /**
     * IP地址的主机名
     *
     * @param ip IP地址
     * @return 主机名，无法解析则返回原IP
     */
    public static String getHostname(String ip) {
        if (!isValidIp(ip)) {
            return ip;
        }

        try {
            InetAddress address = InetAddress.getByName(ip);
            return address.getHostName();
        } catch (UnknownHostException e) {
            return ip;
        }
    }

    /**
     * 所有可能的IP地址列表
     *
     * @param request HttpServletRequest
     * @return 所有可能的IP地址列表
     */
    public static List<String> getAllPossibleIps(HttpServletRequest request) {
        Set<String> ipSet = new LinkedHashSet<>();

        // 检查各种HTTP头
        List<String> headers = Arrays.asList(
                "X-Forwarded-For",
                "X-Real-IP",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP"
        );

        for (String header : headers) {
            String headerValue = request.getHeader(header);
            if (headerValue != null && !headerValue.isEmpty() && !"unknown".equalsIgnoreCase(headerValue)) {
                String[] ips = headerValue.split("[,;\\s]+");
                for (String ip : ips) {
                    ip = ip.trim();
                    if (isValidIp(ip)) {
                        ipSet.add(ip);
                    }
                }
            }
        }

        // 添加远程地址
        String remoteAddr = request.getRemoteAddr();
        if (isValidIp(remoteAddr)) {
            ipSet.add(remoteAddr);
        }

        return new ArrayList<>(ipSet);
    }

    /**
     * 最可信的IP地址（通常是最后一个非私有IP）
     *
     * @param request HttpServletRequest
     * @return 最可信的IP地址
     */
    public static String getMostTrustworthyIp(HttpServletRequest request) {
        List<String> allIps = getAllPossibleIps(request);

        // 从后往前找，通常是最后一个非私有IP最可信
        for (int i = allIps.size() - 1; i >= 0; i--) {
            String ip = allIps.get(i);
            if (isPublicIp(ip)) {
                return ip;
            }
        }

        // 如果都是私有IP，返回最后一个
        if (!allIps.isEmpty()) {
            return allIps.get(allIps.size() - 1);
        }

        return request.getRemoteAddr();
    }

    /**
     * IP位置信息的完整方法（包含代理IP处理）
     *
     * @param request HttpServletRequest
     * @return IP地理位置信息
     */
    public static IpLocationDto getLocationFromRequest(HttpServletRequest request) {
        try {
            String clientIp = getClientIpAddress(request);
            return getLocationInfo(clientIp);
        } catch (Exception e) {
            // 返回默认的未知位置
            return new IpLocationDto("未知", "未知", "未知", "Unknown", "Unknown");
        }
    }

    /**
     * IP地址的完整信息摘要
     *
     * @param ip IP地址
     * @return IP地址的完整信息
     * @throws Exception 查询失败时抛出异常
     */
    public static IpSummaryDto getIpSummary(String ip) throws Exception {
        IpLocationDto location = getLocationInfo(ip);
        return new IpSummaryDto(
                ip,
                isValidIp(ip),
                isPublicIp(ip),
                isPrivateIp(ip),
                isLoopbackIp(ip),
                isLinkLocalIp(ip),
                isMulticastIp(ip),
                getIpClass(ip),
                location.getFullLocation(),
                getHostname(ip)
        );
    }
}