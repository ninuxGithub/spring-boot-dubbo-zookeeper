package com.example.provider.controller;

import com.example.provider.config.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shenzm
 * @date 2019-8-8
 * @description 作用
 */
@RestController
public class KafkaController {
    @Autowired
    private Sender sender;

    @PostMapping("/send/{msg}")
    public String send(@PathVariable("msg") String msg) {
        sender.send(msg);
        return msg;
    }
}
