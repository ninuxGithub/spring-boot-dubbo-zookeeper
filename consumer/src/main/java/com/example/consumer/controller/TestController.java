package com.example.consumer.controller;

import com.example.consumer.bean.SecuMain;
import com.example.consumer.bean.UserNew;
import com.example.consumer.mapper.SecuMainMapper;
import com.example.consumer.service.UserNewService;
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
public class TestController {

    @Autowired
    UserNewService userNewService;

    @Autowired
    SecuMainMapper secuMainMapper;


    @RequestMapping("/querydb")
    public List<SecuMain> querydb() {
        List<SecuMain> secuMains = secuMainMapper.loadSecuMainTable();
        return secuMains;
    }

    @RequestMapping("/querydb2")
    public List<SecuMain> querydb2() {
        Map<String, Object> params = new HashMap<>();
        params.put("secuMarket", Arrays.asList(83, 90));
        params.put("secuCategory", Arrays.asList(1, 2, 6));
        List<SecuMain> secuMains = secuMainMapper.loadSecuMainTableByParams(params);
        return secuMains;
    }

    @RequestMapping("/querydb3")
    public List<SecuMain> querydb3(@RequestParam("sm") String sm, @RequestParam("sc") String sc) {
        List<SecuMain> secuMains = secuMainMapper.loadSecuMainTableParam(sm, sc);
        return secuMains;
    }

    @RequestMapping("/save/user")
    public String save() {
        UserNew userNew = new UserNew();
        userNew.setAge(12);
        userNew.setName("java");
        userNewService.saveUser(userNew);
        return "ok";
    }
}
