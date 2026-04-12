package com.joy.dto.utils;

import lombok.Data;

/**
 * IP地理位置信息DTO
 */
@Data
public class IpLocationDto {
    private String country;   // 国家
    private String region;    // 区域
    private String province;  // 省份
    private String city;      // 城市
    private String isp;       // ISP

    public IpLocationDto() {}

    public IpLocationDto(String country, String region, String province, String city, String isp) {
        this.country = country;
        this.region = region;
        this.province = province;
        this.city = city;
        this.isp = isp;
    }

    /**
     * 获取完整的地理位置描述
     */
    public String getFullLocation() {
        StringBuilder sb = new StringBuilder();
        if (!"未知".equals(country) && !"0".equals(country)) {
            sb.append(country);
        }
        if (!"未知".equals(province) && !"0".equals(province) && !"内网IP".equals(province)) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(province);
        }
        if (!"未知".equals(city) && !"0".equals(city) && !"内网IP".equals(city)) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(city);
        }
        if (!"未知".equals(isp) && !"0".equals(isp)) {
            if (sb.length() > 0) sb.append(" ");
            sb.append("[").append(isp).append("]");
        }
        return sb.length() > 0 ? sb.toString() : "未知位置";
    }

    @Override
    public String toString() {
        return String.format("%s|%s|%s|%s|%s", country, region, province, city, isp);
    }
}
