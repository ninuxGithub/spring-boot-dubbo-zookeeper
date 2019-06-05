package com.example.api.spring.activemq;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * @author shenzm
 * @date 2019-5-31
 * @description 作用
 */

@Service
public class QueueSender {

    @Autowired
    JmsTemplate jmsTemplate;


    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        QueueSender bean = context.getBean(QueueSender.class);
        bean.jmsTemplate.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage message = session.createTextMessage();

                CompletableFuture<String> future = CompletableFuture.supplyAsync(new Supplier<String>() {

                    @Override
                    public String get() {
                        try {
                            TimeUnit.SECONDS.sleep(3);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return "hello";
                    }
                }).thenCombine(CompletableFuture.supplyAsync(new Supplier<String>() {
                    @Override
                    public String get() {
                        try {
                            TimeUnit.SECONDS.sleep(3);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return "world";
                    }
                }), new BiFunction<String, String, String>() {

                    @Override
                    public String apply(String s, String s2) {
                        return s + "  -- " + s2;
                    }
                });

                try {
                    message.setText(future.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                return message;
            }
        });
    }
}
