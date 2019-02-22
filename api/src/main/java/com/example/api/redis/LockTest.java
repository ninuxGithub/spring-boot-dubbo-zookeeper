package com.example.api.redis;

import redis.clients.jedis.Jedis;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author shenzm
 * @date 2019-2-22
 * @description 作用
 */
public class LockTest {

    public static void main(String[] args) {
        int num = 50;
        Jedis jedis = new Jedis("10.1.51.96", 6379);
        jedis.auth("redis");

        for (int i = 0; i <num ; i++) {
            final String threadName = "thread" + i;
            Object taskResult = RedisLockUtil.exeTask(jedis, new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    try {
                        //模拟计算消耗的时间
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return Math.random() * 100;
                }
            },"lockKey");
            System.out.println(threadName + "线程任务执行返回的结果： "+taskResult);

        }
        try {
            System.out.println("====>计算完毕");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
