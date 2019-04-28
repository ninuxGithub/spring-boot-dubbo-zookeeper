//package com.example.consumer.config;
//
//import com.fasterxml.jackson.annotation.JsonAutoDetect;
//import com.fasterxml.jackson.annotation.PropertyAccessor;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cache.annotation.CachingConfigurerSupport;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisNode;
//import org.springframework.data.redis.connection.RedisSentinelConfiguration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import redis.clients.jedis.JedisPoolConfig;
//
//@Configuration
//public class RedisSentinelConfig extends CachingConfigurerSupport {
//
//    @Value("${spring.redis.host}")
//    private String host;
//
//    @Value("${spring.redis.port}")
//    private int port;
//
//    @Value("${spring.redis.timeout}")
//    private int timeout;
//
//    @Value("${spring.redis.database}")
//    private int database;
//
//    @Value("${spring.redis.password}")
//    private String password;
//
//    @Value("${spring.redis.sentinel.nodes}")
//    private String redisNodes;
//
//    @Value("${spring.redis.sentinel.master}")
//    private String master;
//
//    //redis哨兵配置
//    @Bean
//    public RedisSentinelConfiguration redisSentinelConfiguration() {
//        RedisSentinelConfiguration configuration = new RedisSentinelConfiguration();
//        String[] host = redisNodes.split(",");
//        for (String redisHost : host) {
//            String[] item = redisHost.split(":");
//            String ip = item[0];
//            String port = item[1];
//            configuration.addSentinel(new RedisNode(ip, Integer.parseInt(port)));
//        }
//        configuration.setMaster(master);
//        return configuration;
//    }
//
////    //连接redis的工厂类
////    @Autowired
////    private JedisPoolConfig jedisPoolConfig;
////
////        @Bean
////    public JedisPool redisPoolFactory() {
////        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
////        jedisPoolConfig.setMaxIdle(maxIdle);
////        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
////        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);
////        return jedisPool;
////    }
//
//    @Autowired
//    private RedisSentinelConfiguration redisSentinelConfiguration;
//
//    @Bean
//    public JedisConnectionFactory jedisConnectionFactory() {
//        JedisConnectionFactory jedisConnectionFactory =
//                new JedisConnectionFactory(redisSentinelConfiguration, new JedisPoolConfig());
//        return jedisConnectionFactory;
//    }
//
//    //配置RedisTemplate,设置添加序列化器,key 使用string序列化器,value 使用Json序列化器,还有一种简答的设置方式，改变defaultSerializer对象的实现。
//    //@Autowired
//    //private JedisConnectionFactory jedisConnectionFactory;
//
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate() {
//        /*//StringRedisTemplate的构造方法中默认设置了stringSerializer
//        RedisTemplate<Object, Object> template = new RedisTemplate<>();
//        //设置开启事务
//        template.setEnableTransactionSupport(true);
//        //set key serializer
//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//        template.setKeySerializer(stringRedisSerializer);
//        template.setHashKeySerializer(stringRedisSerializer);
//
//        template.setConnectionFactory(jedisConnectionFactory());
//        template.afterPropertiesSet();
//        return template;*/
//
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(jedisConnectionFactory());
//
//        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
//        redisTemplate.setKeySerializer(jackson2JsonRedisSerializer);
//        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
//        redisTemplate.setHashKeySerializer(jackson2JsonRedisSerializer);
//        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//    }
//
//}
