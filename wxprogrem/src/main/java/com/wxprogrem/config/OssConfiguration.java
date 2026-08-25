package com.wxprogrem.config;

import com.wxprogrem.utils.AliOssProperties;
import com.wxprogrem.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration                                        //注解声明配置类并将将其中的 @Bean 方法返回的对象纳入 IoC 容器管理
@Slf4j
public class OssConfiguration {
    @Bean                                             //添加在方法上构造一个实例对象并把对象纳入Ioc容器
    @ConditionalOnMissingBean                         //只有当 Spring 容器中不存在指定类型的 Bean 时，被该注解标记的@Bean方法或配置类才会生效，兜底机制
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) {
        log.info("开始创建阿里云文件上传工具类对象：{}", aliOssProperties);
        return new AliOssUtil(aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getBucketName());
    }
}
