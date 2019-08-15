package com.example.provider.config;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author shenzm
 * @date 2019-8-8
 * @description 作用
 */
@Component
public class Receiver {

    @KafkaListener(topics = "probe2")
    public void receiveMessage(ConsumerRecord<String, String> record) {
        System.out.println("【*** 消费者开始接收消息 ***】key = " + record.key() + "、value = " + record.value());
    }
}

