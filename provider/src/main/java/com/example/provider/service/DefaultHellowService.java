package com.example.provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.example.api.service.HelloService;

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
public class DefaultHellowService implements HelloService {
    @Override
    public String sayHello(String name) {
        return "hello " + name + "  welcome!";
    }

    @Override
    public String sayGoodbye(String name) {
        return "byebye " + name + " ,see u !";
    }
}
