package com.example.consumer.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Queue;
import javax.jms.Topic;

@RestController
@RequestMapping("/publish")
public class PublishController {

    @Autowired
    private JmsMessagingTemplate jms;

    @Autowired
    private Queue queue;

    @Autowired
    private Topic topic;

    @RequestMapping(value = "/queue",produces ="application/json;charset=UTF-8" )
    public String queue() {

        for (int i = 0; i < 2; i++) {
            jms.convertAndSend(queue, "queue" + i);
        }

        return "queue 发送成功";
    }

    @JmsListener(destination = "${spring.activemq.out-queue-name}")
    public void consumerMsg(String msg) {
        System.out.println(msg);
    }

    @RequestMapping(value = "/topic",produces = "application/json;charset=UTF-8")
    public String topic() {

        for (int i = 0; i < 2; i++) {
            jms.convertAndSend(topic, "topic" + i);
        }

        return "topic 发送成功";
    }
}
