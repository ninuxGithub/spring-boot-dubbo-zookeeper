package com.example.api.lock;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;

/**
 * @author shenzm
 * @date 2019-2-28
 * @description 作用
 */
public class SpinLockTest {
    static int count = 0;

    public static void testLock(Lock lock) {
        try {
            lock.lock();
            for (int i = 0; i < 10000000; i++) {
                ++count;
            }
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        final ClhSpinLock clh = new ClhSpinLock();
        final CyclicBarrier cb = new CyclicBarrier(10, new Runnable() {
            @Override
            public void run() {
                System.out.println(count);
            }
        });

        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    testLock(clh);
                    try {
                        cb.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}


