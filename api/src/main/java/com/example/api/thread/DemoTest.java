package com.example.api.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author shenzm
 * @date 2019-5-16
 * @description 作用
 */
public class DemoTest {


    public static void main(String[] args) throws Exception {
        int num = 20;
        ExecutorService threadPool = Executors.newFixedThreadPool(num);
        CountDownLatch latch = new CountDownLatch(num);
        long begin_time, end_time;
        begin_time = System.currentTimeMillis();
        for (int i = 1; i <= 20; i++) {
            final Integer number = i;
            threadPool.submit(new Runnable() {
                @Override
                public void run() {
                    num(number);
                    latch.countDown();
                }
            });
        }
        latch.await();
        end_time = System.currentTimeMillis();
        System.out.println("Occupation time-->" + (end_time - begin_time));
        threadPool.shutdown();
    }

    private synchronized static void num(int v) {
        System.out.println(v);
    }


}
