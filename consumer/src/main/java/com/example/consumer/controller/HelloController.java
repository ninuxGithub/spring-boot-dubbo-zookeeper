package com.example.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.example.api.service.HelloService;
import com.example.consumer.bean.Message;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value ="/commonApi/hello", method = RequestMethod.GET)
    public Message sayHello(@RequestParam(value = "name") String name){
        return new Message(helloService.sayHello(name));
    }


    @RequestMapping(value="/sayGoodbye/{name}")
    public String sayGoodbye(@PathVariable String name){
        return helloService.sayGoodbye(name);
    }
}
