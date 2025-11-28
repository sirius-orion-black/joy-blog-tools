package com.joy.configurer;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "api.prefix")
public class ApiPrefixProperties {
    private String blog = "/blog";
    private String file = "/file";
}
