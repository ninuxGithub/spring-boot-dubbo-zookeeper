package com.example.consumer.config;

import com.weibo.api.motan.config.springsupport.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shenzm
 * @date 2019-4-11
 * @description 作用
 */

@Configuration
public class MotanClient {

    @Bean
    @ConfigurationProperties(prefix = "motan.annotation")
    public AnnotationBean motanAnnotation() {
        AnnotationBean bean = new AnnotationBean();
        return bean;
    }

    @Bean(name = "motan")
    @ConfigurationProperties(prefix = "motan.protocol")
    public ProtocolConfigBean protocolConfigBean() {
        ProtocolConfigBean config = new ProtocolConfigBean();
        return config;
    }

    @Bean(name = "registry")
    @ConfigurationProperties(prefix = "motan.registry")
    public RegistryConfigBean registryConfigBean() {
        RegistryConfigBean registryConfigBean = new RegistryConfigBean();
        return registryConfigBean;
    }

    @Bean
    @ConfigurationProperties(prefix = "motan.client")
    public BasicRefererConfigBean basicRefererConfigBean() {
        BasicRefererConfigBean configBean = new BasicRefererConfigBean();
        return configBean;
    }
}
