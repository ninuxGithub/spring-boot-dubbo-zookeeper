package com.example.provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.example.api.service.TimeService;

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

    private ThreadLocal<SimpleDateFormat> localDateFormate = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    @Override
    public String getTime() {
        return localDateFormate.get().format(new Date());

    }
}
