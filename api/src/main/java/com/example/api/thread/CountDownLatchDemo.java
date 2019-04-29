package com.example.api.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class CountDownLatchDemo {

    //用来表示裁判员需要维护的是6个运动员
    private static CountDownLatch endSignal = new CountDownLatch(6);

    static CyclicBarrier cb = new CyclicBarrier(1, new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < 6; i++) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println(Thread.currentThread().getName() + "正在全力冲刺");
                            endSignal.countDown();
                            TimeUnit.SECONDS.sleep(2);
                            System.out.println(Thread.currentThread().getName() + "  到达终点");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        }
    });

    public static void main(String[] args) throws InterruptedException {

        System.out.println("裁判员发号施令啦！！！");
        try {
            cb.await();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
