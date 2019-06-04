package com.example.api.activemq.msg;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.BlobMessage;

import javax.jms.*;
import java.io.*;
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
                if(message instanceof BlobMessage){
                    BlobMessage blobMessage = (BlobMessage) message;
                    try {
                        InputStream inputStream = blobMessage.getInputStream();
                        BufferedInputStream bis = new BufferedInputStream(inputStream);
                        byte[] bytes = new byte[inputStream.available()];
                        bis.read(bytes);
                        FileOutputStream fout = new FileOutputStream(new File("a.md"));
                        fout.write(bytes);
                    } catch (IOException e) {
                        e.printStackTrace();
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
