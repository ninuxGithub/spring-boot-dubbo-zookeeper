package com.example.api.activemq.msg;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.BlobMessage;

import javax.jms.*;
import java.io.File;

/**
 * auto 自动
 * client 客户端签收
 * dups_ok 不必必须签收  会重复发送
 *
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
        String brokerUrl = "failover:(tcp://10.1.51.96:61616)?jms.blobTransferPolicy.uploadUrl=http://10.1.51.96:8161/fileserver/&randomize=true";
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("admin", "admin", brokerUrl);
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        ActiveMQSession session = (ActiveMQSession) connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("my-queue");
        MessageProducer producer = session.createProducer(queue);

//        activeMQConnectionFactory.setUseAsyncSend(true);
//        ((ActiveMQConnection)connection).setUseAsyncSend(true);
        BlobMessage message = session.createBlobMessage(new File("D:\\dev\\workspace-sts-3.9.0.RELEASE\\spring-boot-dubbo-zookeeper\\api\\activemq.md"));
        producer.send(message);
        session.commit();
        producer.close();
        session.close();
        connection.close();
    }


}
