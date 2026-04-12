package com.joy.dto.utils;

import lombok.Data;

@Data
public class IpSummaryDto {
    private final String ip;
    private final boolean valid;
    private final boolean publicIp;
    private final boolean privateIp;
    private final boolean loopbackIp;
    private final boolean linkLocalIp;
    private final boolean multicastIp;
    private final String ipClass;
    private final String location;
    private final String hostname;

    public IpSummaryDto(String ip, boolean valid, boolean publicIp, boolean privateIp,
                        boolean loopbackIp, boolean linkLocalIp, boolean multicastIp,
                        String ipClass, String location, String hostname) {
        this.ip = ip;
        this.valid = valid;
        this.publicIp = publicIp;
        this.privateIp = privateIp;
        this.loopbackIp = loopbackIp;
        this.linkLocalIp = linkLocalIp;
        this.multicastIp = multicastIp;
        this.ipClass = ipClass;
        this.location = location;
        this.hostname = hostname;
    }

    @Override
    public String toString() {
        return String.format(
                "IP: %s, Valid: %s, Public: %s, Private: %s, Loopback: %s, LinkLocal: %s, Multicast: %s, Class: %s, Location: %s, Hostname: %s",
                ip, valid, publicIp, privateIp, loopbackIp, linkLocalIp, multicastIp, ipClass, location, hostname
        );
    }
}
