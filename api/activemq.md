
## activemq 消息确认的模式
    1.Session.AUTO_ACKNOWLEDGE : 当消费端从consumer.receive()  or  consumer.setMessageListener(Listener) 的方式， 客户端是
    自动发送Ack标记的
    
    
    2.Session.CLIENT_ACKNOWLEDGE： 需要消费端通过 message.achknownledge() 方式进行消息的确认， 需要结合
    Session session = connection.createSession(Boolean.TRUE, Session.CLIENT_ACKNOWLEDGE);
    
    3.Session.DUPS_OK_ACKNOWLEDGE : 和Session.AUTO_ACKNOWLEDGE 差不多，会有重复的消息
    
    4.Session.SESSION_TRANSACTED: 事物的方式， 
    需要开启Session session = connection.createSession(Boolean.TRUE, Session.CLIENT_ACKNOWLEDGE);
    
    


## activemq transaction 事物模式

    通过 Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
    第一参数为true 那么开启的事物的模式
    
    消息的生成者需要通过  session.commit 来进行消息的提交； 
    可以认为消息是按照一个整体发送出去的要么全部成功， 要么全部失败
  
  
 
## activemq 消息手动确认
    通过设置acknowledgeMode： Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);  
    
    
    在消费者端 ： 
    consumer.setMessageListener(new MessageListener() {
        @Override
        public void onMessage(Message message) {
            if(message instanceof TextMessage){
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println(textMessage.getText());
                    message.acknowledge(); //消息的手动确认， 确认之后该消息从队列删除
                } catch (JMSException e) {
                    e.printStackTrace();
                    try {
                        session.recover(); //如果消费失败， 那么消息rollback
                    } catch (JMSException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    });
    
    
    
## activemq queue 队列模式
    是通过队列的名称来精确的匹配， 如果客户端有多个相同的队列名称， 消息是分散发送的 但是消息的条数的总和是  生产者生成消息的数量
    
    假如有1个生成者生产了10条消息， 有2个消费者， 那么每个消费者个消费  5条消息； （轮询）
    
## activemq   topic 模式
    消息广播的方式  分发给每个订阅者， 订阅者获取的消息的数量是相同的；
    

## activemq 
    client-broker , broker-borker
    
    
    openwire  tcp 默认的端口61616 数据传输之前需要序列化
    ssl
    stomp
    xmpp
    
    2019-5-31 16:03:00 11c
    12c 容错连接 failover 连接机制
    13c
   
    2019-6-3 08:39:51
    14c  15c  destination  : wildcard  virsual mirrored-queue
    
    cursors的类型
    Store-based
    VM
    file-based
    
    
    jms.useAsyncSend=true
    cf.setUseAsyncSend(true);
    
    consumer优先等级： 0~4 普通  5~9加急
    activemq优先等级：0~127
    c21 : exclusive consumer : 创建队列的时候采用“quname?consumer.exclusive=true&consumer.priority=10”
    
    
    
## 项目的demo
    http://localhost:9091/publish/queue
    消费端发送消息给provier 通过QueueListener进行了消息的消费 并且会通过sendTo回复一个消息给consumer
    
    
    http://localhost:9091/publish/topic    
    consumer发送一个消息个provider 通过TopicListener进行了消息的消费， 分发给a,b 2个topic    
    
    