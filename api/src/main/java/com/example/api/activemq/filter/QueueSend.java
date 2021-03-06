package com.example.api.activemq.filter;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageProducer;

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
        //randomize 轮流的连接地址
        String brokerUrl = "failover:(tcp://10.1.51.96:61616)?randomize=true";
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("admin", "admin", brokerUrl);
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("test-queue");
        ActiveMQMessageProducer producer = (ActiveMQMessageProducer) session.createProducer(queue);
        for (int i = 0; i < 10; i++) {
            //创建MapMessage
            MapMessage message = session.createMapMessage();
            message.setString("java", "doca"+i);
            message.setObject("key", new HashMap<String, String>() {{
                put("java", "doc");
            }});
            message.setStringProperty("extra","test-message-type");
            message.setIntProperty("age",i);
            message.setStringProperty("JMSXGroupID","groupA");
            producer.send(message);


            MapMessage message2 = session.createMapMessage();
            message2.setString("java", "docb"+i);
            message2.setObject("key", new HashMap<String, String>() {{
                put("java", "doc");
            }});
            message2.setStringProperty("extra","test-message-type");
            message2.setIntProperty("age",i);
            message2.setStringProperty("JMSXGroupID","groupB");

            producer.send(message2);
        }
        producer.close();
        session.close();
        connection.close();
    }


}
