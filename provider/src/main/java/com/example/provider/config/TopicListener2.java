package com.example.provider.config;

/**
 * @author shenzm
 * @date 2019-2-27
 * @description 作用
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class TopicListener2 {

    private static final Logger logger = LoggerFactory.getLogger(QueueListener.class);

    @JmsListener(destination = "${spring.activemq.topic-name}", containerFactory = "jmsListenerContainerTopic")
    public void receive(String text){
        logger.info("QueueListener: consumer-b 收到一条信息: " + text);
    }
}
