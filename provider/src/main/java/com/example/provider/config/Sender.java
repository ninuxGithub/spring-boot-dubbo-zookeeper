package com.example.provider.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * @author shenzm
 * @date 2019-8-8
 * @description 作用
 */
@Component
public class Sender {
    private static final Logger LOG = LoggerFactory.getLogger(Sender.class);

    @Autowired
    private KafkaTemplate<String, String> template;

    public void send(String message) {
        ListenableFuture<SendResult<String, String>> future = this.template.sendDefault(message);
        future.addCallback(success -> LOG.info("KafkaMessageProducer 发送消息成功！消息内容是:"+message),
                fail -> LOG.error("KafkaMessageProducer 发送消息失败！消息内容是:"+message));
    }
}

