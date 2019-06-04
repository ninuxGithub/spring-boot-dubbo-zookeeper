package com.example.api.activemq.dtopic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author shenzm
 * @date 2019-2-27
 * @description 作用
 */
public class TopicSend {

    public static void main(String[] args) throws InterruptedException {
        TopicSend test = new TopicSend();
        try {
            test.produce();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void produce() throws JMSException {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("admin", "admin", "tcp://10.1.51.96:61616");
        Connection connection = activeMQConnectionFactory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic("test.topic");
        MessageProducer producer = session.createProducer(topic);


        //持久化的topic 启动的需要挪动到后面去
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        connection.start();

        for (int i = 0; i < 10; i++) {
            TextMessage message = session.createTextMessage("hello ActiveMQ topic " + i);
            producer.send(message);
        }
        producer.close();
        session.close();
        connection.close();
    }


}
