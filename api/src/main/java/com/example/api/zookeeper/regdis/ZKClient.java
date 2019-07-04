package com.example.api.zookeeper.regdis;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * @author shenzm
 * @date 2019-2-12
 * @description 作用
 */
public class ZKClient {

    static CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) {

        ZooKeeper zooKeeper = null;
        try {
            zooKeeper = new ZooKeeper("10.1.51.96:2181", 10000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    latch.countDown();
                }
            });
            latch.await();
            zooKeeper.addAuthInfo("digest", "username:password".getBytes());
            byte[] data = zooKeeper.getData("/test", null, new Stat());
            System.out.println(data);
        } catch (Exception e) {
            e.printStackTrace();
        }


       /* try {
            zooKeeper = new ZooKeeper("10.1.51.96:2181", 10000, new DefaultWatcher());
            zooKeeper.addAuthInfo("auth", "username:password".getBytes());
            byte[] data = zooKeeper.getData("/test2", null, new Stat());
            System.out.println(data);
        } catch (Exception e) {
            e.printStackTrace();
        }*/


    }

}

