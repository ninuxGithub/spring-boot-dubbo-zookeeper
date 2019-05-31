package com.example.api.spring.activemq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @author shenzm
 * @date 2019-5-31
 * @description 作用
 */
public class MyMessageListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                System.out.println(textMessage.getText());
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
