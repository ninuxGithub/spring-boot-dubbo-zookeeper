package com.example.consumer.controller;

import com.example.consumer.bean.UserNew;
import com.example.consumer.repository.UserNewRepository;
import com.example.consumer.service.UserNewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shenzm
 * @date 2019-3-28
 * @description 作用
 */

@RestController
public class TestController {

    @Autowired
    UserNewService userNewService;


    @RequestMapping("/save/user")
    public String save() {
        UserNew userNew = new UserNew();
        userNew.setAge(12);
        userNew.setName("java");
        userNewService.saveUser(userNew);
        return "ok";
    }
}
