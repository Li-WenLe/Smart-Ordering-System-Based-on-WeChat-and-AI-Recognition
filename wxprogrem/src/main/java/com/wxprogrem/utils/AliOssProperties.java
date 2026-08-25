package com.wxprogrem.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component                      //@Component 是一个注解，它允许 Spring 自动检测自定义 Bean
@ConfigurationProperties(prefix = "sky.alioss")
//@ConfigurationProperties 是 Spring Boot 提供的强大注解，用于将配置文件（如 application.yml 或 application.properties）中的属性绑定到 Java 对象上
@Data
public class AliOssProperties {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

}

