package com.example.consumer.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Topic;

/**
 * @author shenzm
 * @date 2019-2-27
 * @description 作用
 */

@Configuration
@EnableJms
public class ActivemqConfig {

    @Value("${spring.activemq.queue-name}")
    private String queueName;

    @Value("${spring.activemq.topic-name}")
    private String topicName;

    @Value("${spring.activemq.user}")
    private String userName;

    @Value("${spring.activemq.password}")
    private String password;

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Bean
    public Queue queue(){
        return new ActiveMQQueue(queueName);
    }

    @Bean
    public Topic topic(){
        return new ActiveMQTopic(topicName);
    }

    @Bean
    public RedeliveryPolicy redeliveryPolicy(){
        RedeliveryPolicy policy = new RedeliveryPolicy();
        //是否在每次尝试重新发送失败后，增长这个等待时间
        policy.setUseExponentialBackOff(true);
        //重发的次数（6次）
        policy.setMaximumRedeliveries(10);
        //重发的默认的间隔
        policy.setMaximumRedeliveryDelay(1);
        //再次失败后等待的时间 * 2
        policy.setBackOffMultiplier(2);
        //是否避免消息碰撞
        policy.setUseCollisionAvoidance(false);
        //重发最大的推延时间-1， 标示没有延迟
        policy.setMaximumRedeliveryDelay(-1);
        return policy;
    }

    @Bean
    public PooledConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(userName, password, brokerUrl);
        activeMQConnectionFactory.setRedeliveryPolicy(redeliveryPolicy());
        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory(activeMQConnectionFactory);
        pooledConnectionFactory.setMaxConnections(100);
        return pooledConnectionFactory;
    }

    public JmsTemplate jmsTemplate(PooledConnectionFactory connectionFactory, Queue queue){
        JmsTemplate jmsTemplate = new JmsTemplate();
        //1 非持久化； 2 持久化
        jmsTemplate.setDeliveryMode(2);
        jmsTemplate.setConnectionFactory(connectionFactory);
        jmsTemplate.setDefaultDestination(queue);
        //客户端签收
        jmsTemplate.setSessionAcknowledgeMode(4);
        return jmsTemplate;
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerQueue(PooledConnectionFactory connectionFactory){
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        bean.setConnectionFactory(connectionFactory);
        bean.setSessionAcknowledgeMode(4);
        bean.setConcurrency("1-10");
        bean.setRecoveryInterval(1000L);
        return bean;
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerTopic(PooledConnectionFactory connectionFactory){
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        //设置为发布订阅方式, 默认情况下使用的生产消费者方式
        bean.setPubSubDomain(true);
        bean.setConnectionFactory(connectionFactory);
        bean.setRecoveryInterval(1000L);
        return bean;
    }
}
