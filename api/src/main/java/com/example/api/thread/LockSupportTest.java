package com.example.api.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author shenzm
 * @date 2019-5-7
 * @description 作用
 */
public class LockSupportTest {

    public static void main(String[] args) {
        test2();


    }

    /*before park
    before unpark
    after unpark
    after park*/
    public static void test1() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("before park");
                LockSupport.park();
                System.out.println("after park");

            }
        });
        thread.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("before unpark");
        LockSupport.unpark(thread);
        System.out.println("after unpark");
    }





    /*before unpark
    after unpark
    before park*/
    public static void test2() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("before unpark");
                LockSupport.unpark(Thread.currentThread());
                System.out.println("after unpark");

            }
        });
        thread.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("before park");
        LockSupport.park(thread);//<=============会被阻塞在这里
        System.out.println("after park");
    }
}
