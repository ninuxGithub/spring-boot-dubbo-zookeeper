package com.example.consumer.config;

import com.example.consumer.bean.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

import java.beans.PropertyDescriptor;

/**
 * postProcessBeforInitialization  --->  afterPropertiesSet --> init --> postProcessAfterInitialization
 * <p>
 * <p>
 * bean加载：
 * 1.ClassPathBeanDefinitionScanner#registerBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry)
 * 2.BeanDefinitionReaderUtils.registerBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry)
 * 3.DefaultListableBeanFactory.registerBeanDefinition(String beanName, BeanDefinition beanDefinition)   将bean保存到beanDefinitionMap
 * <p>
 * 查看 org.springframework.context.support.AbstractApplicationContext#refresh()  bean的加载都在这方法里面
 *
 * @author shenzm
 * @date 2019-2-22
 * @description 作用
 */


/***
 * 实例化BeanFactoryPostProcessor实现类---postProcessBeanFactory()执行
 * 实例化BeanPostProcessor实现类
 * 实例化InstantiationAwareBeanPostProcessorAdaptor实现类
 * 执行InstantiationAwareBeanPostProcessor--->postProcessBeforeInstantiation()-->postProcessPropertyValues()
 * 调用BeanNameAware.setBeanName()
 * 调用BeanFactoryAware.setBeanFactory()
 * 执行BeanPostProcessor.postProcessBeforeInitialization()
 * 调用InitializingBean.afterPropertySet()
 * 调用@Bean的init-method
 * 执行BeanPostProcessor.postProcessAfterInitialization()
 * 执行InstantiationAwareBeanPostProcessor.postProcessAfterInitialization()
 * 执行DisposibleBean.destory()
 * 调用@Bean的destroy-method
 *
 */


//系统启动的时候日志太多了注释掉
//@Configuration
//public class DemoBeanConfig extends InstantiationAwareBeanPostProcessorAdapter implements BeanNameAware,BeanFactoryAware, InitializingBean, BeanPostProcessor, BeanFactoryPostProcessor {
//
//    private static final Logger logger = LoggerFactory.getLogger(DemoBeanConfig.class);
//
//    @Value("${demo.age}")
//    private int age;
//
//    @Value("${demo.name}")
//    private String name;
//
//    private String beanName;
//
//    private BeanFactory beanFactory;
//
//    @Bean(initMethod = "init", destroyMethod = "destory")
//    public Person init() {
//        return new Person(name, age);
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        logger.info("set properties ===>  name: {}  age :{}  ", name, age);
//    }
//
//    @Override
//    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//        logger.info("BeanPostProcessor.postProcessBeforeInitialization  ===>  bean: {}  beanName :{}  ", bean, beanName);
//        return bean;
//    }
//
//    @Override
//    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        logger.info("BeanPostProcessor.postProcessAfterInitialization  ===>  bean: {}  beanName :{}  ", bean, beanName);
//        return bean;
//    }
//
//    @Override
//    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//        logger.info("BeanFactoryPostProcessor.postProcessBeanFactory");
//    }
//
//    @Nullable
//    @Override
//    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
//
//        return super.postProcessBeforeInstantiation(beanClass, beanName);
//    }
//
//    @Override
//    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
//        return super.postProcessPropertyValues(pvs, pds, bean, beanName);
//    }
//
//    @Override
//    public void setBeanName(String name) {
//        this.beanName = name;
//        logger.info("bean name :{}", name);
//    }
//
//    @Override
//    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
//        this.beanFactory = beanFactory;
//        logger.info("bean factory ");
//    }
//}
