package com.example.api.zookeeper.namedservice;

import java.util.concurrent.CountDownLatch;

/**
 * @author shenzm
 * @date 2019-2-18
 * @description 作用
 */
public class IdGeneratorTest {



    public static void main(String[] args) throws Exception {
        int num = 10;
        CountDownLatch latch = new CountDownLatch(num);
        final IdGenerator idGenerator = new IdGenerator("10.1.51.96:2181", "/named-service/idGenerator", "id");

        try {
            for (int i = 0; i < num; i++) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String id = null;
                        try {
                            id = idGenerator.generateId(RemoveMethod.DELAY);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.out.println(id);
                        latch.countDown();
                    }
                }).start();
            }
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //开启多线程的时候  服务判断是否关闭了
            idGenerator.stop();
        }


    }
}
