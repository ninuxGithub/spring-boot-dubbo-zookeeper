package com.example.api.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @author shenzm
 * @date 2019-2-27
 * @description 作用
 */
public class ActivemqTestQueueReceive {

    public static void main(String[] args) throws InterruptedException {
        ActivemqTestQueueReceive test = new ActivemqTestQueueReceive();
        try {
            test.condum();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }


    public void condum() throws JMSException, InterruptedException {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("admin", "admin", "tcp://10.1.51.96:61616");
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        Queue queue = session.createQueue("test-queue");
        MessageConsumer consumer = session.createConsumer(queue);
        //consumer.receive()
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if(message instanceof TextMessage){
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        System.out.println(textMessage.getText());
                        message.acknowledge();
                    } catch (JMSException e) {
                        e.printStackTrace();
                        try {
                            session.recover();
                        } catch (JMSException e1) {
                            e1.printStackTrace();
                        }
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
