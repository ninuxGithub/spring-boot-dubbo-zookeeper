package com.example.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.example.api.service.HelloService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shenzm
 * @date 2019-1-29
 * @description 作用
 */

@RestController
public class HelloController {


    @Reference(
            version = "${spring.application.version}",
            application = "${dubbo.application.id}",
            registry = "${dubbo.registry.id}"
    )
    private HelloService helloService;

    @RequestMapping(value ="/hello/{name}")
    public String sayHello(@PathVariable String name){
        return helloService.sayHello(name);
    }


    @RequestMapping(value="/sayGoodbye/{name}")
    public String sayGoodbye(@PathVariable String name){
        return helloService.sayGoodbye(name);
    }
}
