package com.example.api.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author shenzm
 * @date 2019-4-29
 * @description 作用
 */
public class CountDownlatchTest {

    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(2);
        for (int i=0; i<2; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("java");
                }
            }).start();
            try {
                TimeUnit.SECONDS.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
        }
    }
}
