package com.example.api.zookeeper.lock;

import com.google.common.base.Charsets;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author shenzm
 * @date 2019-7-4
 * @description 作用
 */
public class LockTest {

    private static final Logger logge = LoggerFactory.getLogger(LockTest.class);

    private static ZkClient client = new ZkClient("10.1.51.96:2181", 1000, 1000, new ZkSerializer() {
        @Override
        public byte[] serialize(Object data) throws ZkMarshallingError {
            return String.valueOf(data).getBytes(Charsets.UTF_8);
        }

        @Override
        public Object deserialize(byte[] bytes) throws ZkMarshallingError {
            return new String(bytes, Charsets.UTF_8);
        }
    });

    private static ZookeeperLock lock = new ZookeeperLock(client);

    static ExecutorService threadPool = Executors.newFixedThreadPool(5);

    public static void main(String[] args) {
        for (int i = 0; i < 50; i++) {
            threadPool.submit(()->{
                try {
                    boolean flag = lock.lock();
                    if (flag) {
                        TimeUnit.SECONDS.sleep(5);
                        logge.info("获取到锁执行业务方法");
                    }
                } catch (Exception e) {
                    //
                } finally {
                    lock.releaseLock();
                }
            });
            new Thread(() -> {
                try {
                    boolean flag = lock.lock();
                    if (flag) {
                        TimeUnit.SECONDS.sleep(2);
                        logge.info("获取到锁执行业务方法");
                    }
                } catch (Exception e) {
                    //
                } finally {
                    lock.releaseLock();
                }
            }, "t" + i).start();
        }
    }


}
