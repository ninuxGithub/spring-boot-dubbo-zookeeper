package com.example.api.lock;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;

/**
 * @author shenzm
 * @date 2019-2-15
 * @description 作用
 */
public class TestZookeeperLock {
    public static void main(String[] args) {

        // 需要手动创建节点 /locker

        ZkClient zkClient1 = new ZkClient("10.1.51.96:2181", 5000, 5000, new BytesPushThroughSerializer());
        LockImpl lock1 = new LockImpl(zkClient1, "/locker","client1");

        ZkClient zkClient2 = new ZkClient("10.1.51.96:2181", 5000, 5000, new BytesPushThroughSerializer());
        final LockImpl lock2 = new LockImpl(zkClient2, "/locker", "client2");

        try {
            for(int i=0; i<100; i++){
                lock1.getLock();
                System.out.println("Client1 is get lock!");
                Thread client2Thd = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            lock2.getLock();
//                        lock2.getLock(500, TimeUnit.SECONDS);
                            System.out.println("Client2 is get lock");
                            lock2.releaseLock();
                            System.out.println("Client2 is released lock");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                client2Thd.start();

                // 5s 后lock1释放锁
                Thread.sleep(5000);
                lock1.releaseLock();
                System.out.println("Client1 is released lock");

                client2Thd.join();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
