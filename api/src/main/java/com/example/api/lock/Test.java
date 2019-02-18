package com.example.api.lock;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author shenzm
 * @date 2019-2-15
 * @description 作用
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {

        // 需要手动创建节点 /locker
        int num = 100;
        CountDownLatch latch = new CountDownLatch(num);

        for (int i = 0; i < 100; i++) {
            final int n = i;

            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        latch.await();
                        ZkClient zkClient = new ZkClient("10.1.51.96:2181", 5000, 50000, new BytesPushThroughSerializer());
                        LockImpl lock = new LockImpl(zkClient, "/locker");
                        lock.getLock();
                        System.out.println("Client" + n + " is get lock");
                        TimeUnit.SECONDS.sleep(2);
                        lock.releaseLock();
                        System.out.println("Client" + n + " is released lock");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            latch.countDown();

        }


    }
}
