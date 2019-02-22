package com.example.api.redis;

import redis.clients.jedis.Jedis;

import java.util.UUID;
import java.util.concurrent.*;

/**
 * @author shenzm
 * @date 2019-2-22
 * @description 作用
 */
public class RedisLockTest {

    static ExecutorService threadPool = Executors.newFixedThreadPool(5);

    public static void main(String[] args) {
        int num = 50;
        CountDownLatch latch = new CountDownLatch(num);
        Jedis jedis = new Jedis("10.1.51.96", 6379);
        jedis.auth("redis");

        for (int i = 0; i <num ; i++) {
            final String threadName = "thread" + i;
            new Thread(new Runnable() {
                @Override
                public void run() {
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
                    try {
                        latch.countDown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, threadName).start();

        }
        try {
            latch.await();
            System.out.println("====>计算完毕");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
