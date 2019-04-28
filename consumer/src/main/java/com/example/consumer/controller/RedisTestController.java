package com.example.consumer.controller;

import com.example.consumer.bean.SecuMain;
import com.example.consumer.bean.UserNew;
import com.example.consumer.mapper.SecuMainMapper;
import com.example.consumer.service.UserA;
import com.example.consumer.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shenzm
 * @date 2019-3-28
 * @description 作用
 */

@RestController
public class RedisTestController {

    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping(value = "/testRedis")
    public String testRedis(@RequestParam("value") String value){
        redisUtil.set("java",value);
        return "ok";
    }
}
