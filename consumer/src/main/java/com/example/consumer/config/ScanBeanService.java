package com.example.consumer.config;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 扫描@RestController or @Controller的注解  获取方法上有@RequestMapping的方法
 *
 * 获取方法名称 ， 获取参数列表 封装到map
 * @author shenzm
 * @date 2019-3-18
 * @description 作用
 */

@Component
public class ScanBeanService implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(ScanBeanService.class);


    private Map<String, List<String>> methodName2ParamsTable = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        scan(applicationContext);
    }


    /**
     * 扫描工程里面所有对外的接口
     *
     * @param applicationContext
     */
    private void scan(ApplicationContext applicationContext) {
        //TODO @Controller 未添加
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(RestController.class);
        if (MapUtils.isNotEmpty(beansWithAnnotation)) {
            for (Object bean : beansWithAnnotation.values()) {
                Method[] declaredMethods = bean.getClass().getDeclaredMethods();
                for (Method method : declaredMethods) {
                    RequestMapping declaredAnnotation = method.getDeclaredAnnotation(RequestMapping.class);
                    String methodName = method.getName();
                    if (null != declaredAnnotation) {
                        List<String> paramsTable = methodName2ParamsTable.get(methodName);
                        if (null == paramsTable) {
                            paramsTable = new ArrayList<>();
                        }
                        Parameter[] parameters = method.getParameters();
                        for (Parameter parameter : parameters) {
                            paramsTable.add(parameter.getName());
                        }
                        methodName2ParamsTable.put(methodName, paramsTable);
                    }
                }
            }
            logger.info("扫描系统的对外接口： {}",methodName2ParamsTable);
        }
    }


    public Map<String, List<String>> getMethodName2ParamsTable() {
        return methodName2ParamsTable;
    }
}
