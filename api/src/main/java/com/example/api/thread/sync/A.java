package com.example.api.thread.sync;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author shenzm
 * @date 2019-5-13
 * @description 作用
 */
public class A {

    AtomicInteger counter = new AtomicInteger();

    public void method1() {
        synchronized (A.class) {
            System.out.println(Thread.currentThread().getName() + " A.method1");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void method2() {
        System.out.println(Thread.currentThread().getName() + " A.method2");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void method3() {
        synchronized (counter) {
            counter.getAndIncrement();
            System.out.println(counter.hashCode());
            System.out.println(Thread.currentThread().getName() + " A.method2");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void method4() {
        Integer a = 1;
        synchronized (a) {
            a++;
            System.out.println(a.hashCode());
            System.out.println(Thread.currentThread().getName() + " A.method2");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        //线程的监视器是统一个锁  线程会串行化的执行
       /* for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new A().method1();
                }
            }).start();
        }

        //创建的每个对象是当前的方法的锁 所以 每个方法都有自己的锁 所不同
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new A().method2();
                }
            }).start();
        }*/


        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new A().method3();
                }
            }).start();
        }
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new A().method4();
                }
            }).start();
        }
    }
}
