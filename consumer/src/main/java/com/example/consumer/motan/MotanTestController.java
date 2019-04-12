package com.example.consumer.motan;

import com.example.api.service.MotanBizService;
import com.weibo.api.motan.config.springsupport.annotation.MotanReferer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shenzm
 * @date 2019-4-11
 * @description 作用
 */


@RestController
public class MotanTestController {

    @MotanReferer(basicReferer = "basicRefererConfigBean")
    private MotanBizService motanService;

    @RequestMapping(value = "/motan/test")
    public String hello(@RequestParam("msg") String msg) {
        return motanService.sayHello(msg);
    }
}
