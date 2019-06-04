package com.example.api.activemq.mirror;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @author shenzm
 * @date 2019-2-27
 * @description 作用
 */
public class TopicReceive {

    public static void main(String[] args) throws InterruptedException {
        TopicReceive test = new TopicReceive();
        try {
            test.consume();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }


    public void consume() throws JMSException, InterruptedException {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("admin", "admin", "tcp://10.1.51.96:61616");
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic("VirtualTopic.Mirror.my-queue");
        MessageConsumer consumer = session.createConsumer(topic);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if(message instanceof TextMessage){
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        System.out.println(textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        consumer.close();
        session.close();
        connection.close();
    }

}
