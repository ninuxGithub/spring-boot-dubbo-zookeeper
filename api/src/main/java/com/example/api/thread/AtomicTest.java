package com.example.api.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author shenzm
 * @date 2019-4-1
 * @description 作用
 */
public class AtomicTest {


    private static AtomicInteger value = new AtomicInteger(100);

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                value.compareAndSet(100, 101);
                value.compareAndSet(101,100);
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                boolean b = value.compareAndSet(100, 101);
                System.out.println("线程2 是否修改成功： "+b);

            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();




    }
}
