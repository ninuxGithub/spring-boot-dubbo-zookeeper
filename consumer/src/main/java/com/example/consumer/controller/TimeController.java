package com.example.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.example.api.service.TimeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shenzm
 * @date 2019-2-11
 * @description 作用
 */

@RestController
public class TimeController {

    @Reference(
            version = "${spring.application.version}",
            application = "${dubbo.application.id}",
            registry = "${dubbo.registry.id}"
    )
    private TimeService timeService;


    @RequestMapping("/getTime")
    public String getTime() {
        return timeService.getTime();
    }
}
