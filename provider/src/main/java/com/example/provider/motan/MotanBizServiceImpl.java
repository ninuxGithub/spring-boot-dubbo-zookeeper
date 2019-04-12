package com.example.provider.motan;

import com.example.api.service.MotanBizService;
import com.weibo.api.motan.config.springsupport.annotation.MotanService;

/**
 * @author shenzm
 * @date 2019-4-11
 * @description 作用
 */

@MotanService(basicService = "basicServiceConfigBean")
public class MotanBizServiceImpl implements MotanBizService {
    @Override
    public String sayHello(String msg) {
        return "hello : "+msg;
    }
}
