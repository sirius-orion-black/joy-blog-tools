package com.joy.utils;

public class DeviceInfoUtil {
    /**
     * 判断是否为移动端
     */
    public static boolean isMobile(String ua) {
        if (ua == null) return false;
        String lowerUa = ua.toLowerCase();
        // 常见移动设备标识
        return lowerUa.contains("mobile") ||
                (lowerUa.contains("android") && !lowerUa.contains("pad")) ||
                lowerUa.contains("iphone") ||
                lowerUa.contains("ipod");
    }

    /**
     * 获取操作系统名称
     */
    public static String getOsName(String ua) {
        if (ua == null) return "Unknown";
        if (ua.contains("Windows NT")) return "Windows";
        if (ua.contains("Macintosh") || ua.contains("Mac OS X")) return "macOS";
        if (ua.contains("Android")) return "Android";
        if (ua.contains("iPhone") || ua.contains("iPad")) return "iOS";
        if (ua.contains("Linux")) return "Linux";
        return "Other";
    }

    /**
     * 获取浏览器名称
     */
    public static String getBrowserName(String ua) {
        if (ua == null) return "Unknown";
        // 注意判断顺序，Edge 包含 Chrome 和 Safari，Chrome 包含 Safari
        if (ua.contains("Edg")) return "Edge";
        if (ua.contains("Chrome") && !ua.contains("Edg")) return "Chrome";
        if (ua.contains("Firefox")) return "Firefox";
        if (ua.contains("Safari") && !ua.contains("Chrome")) return "Safari";
        if (ua.contains("MicroMessenger")) return "WeChat"; // 微信
        return "Other";
    }
}
