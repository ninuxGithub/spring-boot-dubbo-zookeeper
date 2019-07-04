package com.example.api.zookeeper.queue;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import java.util.concurrent.TimeUnit;

/**
 * @author shenzm
 * @date 2019-2-18
 * @description 作用
 */
public class TestQueue {

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient("10.1.51.96:2181", 5000, 5000, new SerializableSerializer());
        DistributedSimpleQueue<User> queue = new DistributedSimpleQueue<>(zkClient, "/queue");

        User user1 = new User();
        user1.setId("1");
        user1.setName("tomcat");

        User user2 = new User();
        user2.setId("2");
        user2.setName("jetty");

        try {
            queue.offer(user1);
            queue.offer(user2);
            TimeUnit.SECONDS.sleep(8);
            User u1 = queue.poll();
            User u2 = queue.poll();
            System.out.println(u1.getName() + " " + u1.getId());
            System.out.println(u2.getName() + " " + u2.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
