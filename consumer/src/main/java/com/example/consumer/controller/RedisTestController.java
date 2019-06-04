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

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author shenzm
 * @date 2019-3-28
 * @description 作用
 */

@RestController
public class RedisTestController {

    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);


    private static ThreadLocal<SimpleDateFormat> localDateFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        }
    };

    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping(value = "/testRedis")
    public String testRedis(@RequestParam("value") String value){
        redisUtil.set("java",value);
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                redisUtil.rpush("list", localDateFormat.get().format(new Date()));
            }
        }, 2, 3, TimeUnit.SECONDS);


        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                String value = redisUtil.lpop("list");
                System.out.println(value);
            }
        }, 2, 3, TimeUnit.SECONDS);

        return "ok";
    }
}
