package com.example.api.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author shenzm
 * @date 2019-2-27
 * @description 作用
 */
public class ActivemqTestQueueTransactionSend {

    public static void main(String[] args) throws InterruptedException {
        ActivemqTestQueueTransactionSend test = new ActivemqTestQueueTransactionSend();
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
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("test-queue");
        MessageProducer producer = session.createProducer(queue);
        for(int i=0; i<10; i++){
            TextMessage message = session.createTextMessage("hello ActiveMQ" + i);
            producer.send(message);
        }

        session.commit();
        producer.close();
        session.close();
        connection.close();
    }


}
