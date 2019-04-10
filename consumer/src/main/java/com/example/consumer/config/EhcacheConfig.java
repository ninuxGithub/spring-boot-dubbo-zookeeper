package com.example.consumer.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * @author shenzm
 * @date 2019-4-10
 * @description 作用
 */

@Configuration
@EnableCaching
public class EhcacheConfig {

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean managerFactoryBean = new EhCacheManagerFactoryBean();
        managerFactoryBean.setConfigLocation(new ClassPathResource("config/ehcache.xml"));
        managerFactoryBean.setShared(true);
        return managerFactoryBean;
    }

    @Bean
    public EhCacheCacheManager ehCacheCacheManager() {
        return new EhCacheCacheManager(ehCacheManagerFactoryBean().getObject());
    }
}
