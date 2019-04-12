package com.example.provider.config;

import com.weibo.api.motan.config.springsupport.AnnotationBean;
import com.weibo.api.motan.config.springsupport.BasicServiceConfigBean;
import com.weibo.api.motan.config.springsupport.ProtocolConfigBean;
import com.weibo.api.motan.config.springsupport.RegistryConfigBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shenzm
 * @date 2019-4-11
 * @description 作用
 */

@Configuration
public class MotanServer {

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
    @ConfigurationProperties(prefix = "motan.server")
    public BasicServiceConfigBean basicServiceConfigBean() {
        BasicServiceConfigBean configBean = new BasicServiceConfigBean();
        return configBean;
    }
}
