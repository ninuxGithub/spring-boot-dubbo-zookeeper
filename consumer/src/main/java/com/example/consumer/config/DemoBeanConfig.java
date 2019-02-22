package com.example.consumer.config;

import com.example.consumer.bean.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * postProcessBeforInitialization  --->  afterPropertiesSet --> init --> postProcessAfterInitialization
 *
 *
 * bean加载：
 * 1.ClassPathBeanDefinitionScanner#registerBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry)
 * 2.BeanDefinitionReaderUtils.registerBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry)
 * 3.DefaultListableBeanFactory.registerBeanDefinition(String beanName, BeanDefinition beanDefinition)   将bean保存到beanDefinitionMap
 *
 * 查看 org.springframework.context.support.AbstractApplicationContext#refresh()  bean的加载都在这方法里面
 *
 *
 * @author shenzm
 * @date 2019-2-22
 * @description 作用
 */


@Configuration
public class DemoBeanConfig implements InitializingBean, BeanPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(DemoBeanConfig.class);

    @Value("${demo.age}")
    private int age;

    @Value("${demo.name}")
    private String name;

    @Bean(initMethod = "init", destroyMethod = "destory")
    public Person init() {
        return new Person(name, age);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("set properties ===>  name: {}  age :{}  ", name, age);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        //logger.info("postProcessBeforeInitialization  ===>  bean: {}  beanName :{}  " , bean, beanName );
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //logger.info("postProcessAfterInitialization  ===>  bean: {}  beanName :{}  " , bean, beanName );
        return bean;
    }
}
