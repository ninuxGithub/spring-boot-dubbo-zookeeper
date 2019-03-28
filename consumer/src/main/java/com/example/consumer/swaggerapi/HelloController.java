package com.example.consumer.swaggerapi;

import com.alibaba.dubbo.config.annotation.Reference;
import com.example.api.service.HelloService;
import com.example.consumer.bean.Message;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author shenzm
 * @date 2019-1-29
 * @description 作用
 */


@RestController
@Api(value = "提供Hello的公共的访问方法")
@RequestMapping(value = "/commonApi")
public class HelloController {


    @Reference(
            version = "${spring.application.version}",
            application = "${dubbo.application.id}",
            registry = "${dubbo.registry.id}"
    )
    private HelloService helloService;


    @ApiOperation(value = "说hello方法", notes = "需要传递参数name(方法需要验签)")
    @ApiImplicitParam(name = "name", value = "用户姓名", required = true, dataType = "String", paramType = "query", defaultValue = "java")
    @ApiResponse(code = 200, message = "消息体")
    @RequestMapping(value = "/sayHello", method = RequestMethod.GET)
    public Message sayHello(@RequestParam(value = "name") String name) {
        return new Message(helloService.sayHello(name));
    }


    @ApiOperation(value = "说goodbye方法", notes = "需要传递参数name, age(方法需要验签)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "用户姓名", required = true, dataType = "String", paramType = "query", defaultValue = "java"),
            @ApiImplicitParam(name = "age", value = "用户年龄", required = true, dataType = "Integer", paramType = "query", defaultValue = "18")
    })
    @ApiResponse(code = 200, message = "消息体")
    @RequestMapping(value = "/sayGoodbye", method = RequestMethod.GET)
    public Message sayGoodbye(@RequestParam(value = "name") String name, @RequestParam(value = "age") Integer age) {
        return new Message(helloService.sayGoodbye(name + " age is : " + age));
    }

    @ApiIgnore
    @RequestMapping(value = "/ignore")
    public Message ignore(@RequestParam(value = "name") String name, @RequestParam(value = "age") Integer age) {
        return new Message(helloService.sayGoodbye(name + " age is : " + age));
    }
}
