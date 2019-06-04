package com.example.api.activemq.mirror;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.transport.amqp.protocol.AmqpConnection;

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
        String brokerUrl = "failover:(tcp://10.1.51.96:61616,tcp://10.1.51.240:61616)?randomize=true";
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("admin", "admin", brokerUrl);
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("my-queue");
        MessageProducer producer = session.createProducer(queue);

//        activeMQConnectionFactory.setUseAsyncSend(true);
//        ((ActiveMQConnection)connection).setUseAsyncSend(true);
        for (int i = 0; i < 10; i++) {
            //创建MapMessage
            TextMessage message = session.createTextMessage("java doc");
            producer.send(message);
        }
        producer.close();
        session.close();
        connection.close();
    }


}
