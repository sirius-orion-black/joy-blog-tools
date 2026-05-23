package com.joy.config.apiPrefix;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "api.prefix")
public class ApiPrefixProperties {
    //本来想写元注解用的，现在没用上，哪位仁兄想用元注解可以用的上
    private String blog = "/blog";
    private String file = "/infra";
    private String admin = "/admin";
}
