package com.example.api.activemq.reject;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;

import javax.jms.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * @author shenzm
 * @date 2019-2-27
 * @description 作用
 */
public class QueueReceive {

    public static void main(String[] args) throws InterruptedException {
        QueueReceive test = new QueueReceive();
        try {
            test.condum();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }


    public void condum() throws JMSException, InterruptedException {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("admin", "admin", "tcp://10.1.51.96:61616");
        Connection connection = activeMQConnectionFactory.createConnection();

        //processExpired = false
        //重新发送策略  超过3次后进入ActiveMQ.DLQ
        RedeliveryPolicy policy = new RedeliveryPolicy();
        policy.setMaximumRedeliveryDelay(3);
        activeMQConnectionFactory.setRedeliveryPolicy(policy);
        connection.start();
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        Queue queue = session.createQueue("test-queue");
        MessageConsumer consumer = session.createConsumer(queue);


        Enumeration jmsxPropertyNames = connection.getMetaData().getJMSXPropertyNames();
        List<String> bindPropertyNames = new ArrayList<>();
        while (jmsxPropertyNames.hasMoreElements()){
            String name = (String) jmsxPropertyNames.nextElement();
            bindPropertyNames.add(name);
        }
        System.out.println(bindPropertyNames);


        //consumer.receive()
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (message instanceof MapMessage){
                    MapMessage mapMessage = (MapMessage) message;
                    try {
                        String javaProperty = mapMessage.getString("java");
                        Map<String,String> map = (Map<String, String>) mapMessage.getObject("key");
                        System.out.println(javaProperty  + "            "+map);

                        //获取消息中的属性
                        String extra = mapMessage.getStringProperty("extra");
                        System.out.println(extra);
                        //message.acknowledge();
                        session.recover();
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
