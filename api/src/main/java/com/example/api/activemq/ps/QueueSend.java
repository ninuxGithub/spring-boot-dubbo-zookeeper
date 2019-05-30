package com.example.api.activemq.ps;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.HashMap;

/**
 *
 * auto 自动
 * client 客户端签收
 * dups_ok 不必必须签收  会重复发送
 * @author shenzm
 * @date 2019-2-27
 * @description 作用
 */
public class QueueSend {

    public static void main(String[] args) throws InterruptedException {
        QueueSend test = new QueueSend();
        try {
            test.produce();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void produce() throws JMSException {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("admin", "admin", "tcp://10.1.51.96:61616");
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("test-queue");
        MessageProducer producer = session.createProducer(queue);
        for (int i = 0; i < 10; i++) {
            //创建MapMessage
            MapMessage message = session.createMapMessage();
            message.setString("java", "doc");
            message.setObject("key", new HashMap<String, String>() {{
                put("java", "doc");
            }});

            //设置属性
            message.setStringProperty("extra","test-message-type");
            producer.send(message);
        }
        producer.close();
        session.close();
        connection.close();
    }


}
