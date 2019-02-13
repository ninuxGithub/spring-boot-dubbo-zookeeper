package com.example.provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.example.api.service.TimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author shenzm
 * @date 2019-2-11
 * @description 作用
 */

@Service(
        version = "${spring.application.version}",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class DefaultTimeService implements TimeService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultTimeService.class);

    private ThreadLocal<SimpleDateFormat> localDateFormate = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    @Value("${server.port}")
    private int port;

    @Override
    public String getTime() {
        logger.info("端口{}的服务响应客户端的请求",port);
        return localDateFormate.get().format(new Date());

    }
}
