package com.example.spart.config;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shenzm
 * @date 2019-5-16
 * @description 作用
 */
@Configuration
public class SparkConfig {

    @Value("${spark.spark-home}")
    private String sparkHome;

    @Value("${spark.app-name}")
    private String appName;

    @Value("${spark.master}")
    private String master;

    @Bean
    @ConditionalOnMissingBean(SparkConf.class)
    public SparkConf sparkConf() {
        SparkConf sparkConf = new SparkConf().setAppName(appName).setMaster(master);
        return sparkConf;
    }

    @Bean
    @ConditionalOnMissingBean(JavaSparkContext.class)
    public JavaSparkContext javaSparkContext() {
        return new JavaSparkContext(sparkConf());
    }

}
