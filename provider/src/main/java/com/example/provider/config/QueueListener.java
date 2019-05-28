package com.example.provider.config;

/**
 * @author shenzm
 * @date 2019-2-27
 * @description 作用
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

@Component
public class QueueListener{
    private static final Logger logger = LoggerFactory.getLogger(QueueListener.class);


    @JmsListener(destination = "${spring.activemq.queue-name}", containerFactory = "jmsListenerContainerQueue")
    @SendTo("${spring.activemq.out-queue-name}")
    public String receive(TextMessage message, Session session) throws JMSException {
        String msg = "消费成功了";
        try {
            //发送了一个异常， 那么无法调用message.acknowledge()会导致消息被recover返回到mq
            int a=1,b=0,c=a/b;
            logger.info("QueueListener: consumer-a 收到一条信息: " + message.getText());
            message.acknowledge();
        } catch (Exception e) {
            session.recover();
            msg = "消费失败 消息被退回";
        }
        return "consumer-a received : " + msg;
    }
}
