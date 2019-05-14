package com.example.api.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author shenzm
 * @date 2019-5-13
 * @description 作用
 */
public class WRLockTest {

    Map<String, String> data;

    volatile boolean cacheValid;

    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();


    public static void main(String[] args) {
        new WRLockTest().testLock();
    }


    public void testLock() {
        lock.readLock().lock();
        if (!cacheValid) {
            lock.readLock().unlock();
            lock.writeLock().lock();

            if (!cacheValid) {
                data = new HashMap<>();
                data.put("java", "doc");
                cacheValid = true;
            }

            lock.readLock().lock();
            lock.writeLock().unlock();

        }

        //use data
        useData(data);
        lock.readLock().unlock();


    }

    private void useData(Object data) {
        for (int i = 0; i < 10; i++) {
            System.out.println(data);
        }
    }
}
