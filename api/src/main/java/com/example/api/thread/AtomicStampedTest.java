package com.example.api.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author shenzm
 * @date 2019-4-1
 * @description 作用
 */
public class AtomicStampedTest {

    private static AtomicStampedReference<Integer> stampedReference = new AtomicStampedReference<>(100, 0);

    public static void main(String[] args) {

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                stampedReference.compareAndSet(100, 101, stampedReference.getStamp(), stampedReference.getStamp() + 1);
                stampedReference.compareAndSet(101, 100, stampedReference.getStamp(), stampedReference.getStamp() + 1);
            }
        });


        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                //false
                //int stamp = stampedReference.getStamp();
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //true
                int stamp = stampedReference.getStamp();
                boolean b = stampedReference.compareAndSet(100, 101, stamp, stamp + 1);
                System.out.println(b);
            }
        });
        t1.start();
        t2.start();
    }
}
