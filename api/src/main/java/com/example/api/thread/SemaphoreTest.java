package com.example.api.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author shenzm
 * @date 2019-5-14
 * @description 作用
 */
public class SemaphoreTest {

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(10);
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        threadPool.execute(new MyThread(semaphore, 4));
        threadPool.execute(new MyThread(semaphore, 5));
        threadPool.execute(new MyThread(semaphore, 7));
        threadPool.shutdown();

    }
}

class MyThread extends Thread{

    private volatile Semaphore semaphore;

    private int count;

    public MyThread(Semaphore semaphore, int count){
        this.semaphore = semaphore;
        this.count=count;
    }

    @Override
    public void run() {
        try {
            semaphore.acquire(count);
            Thread.sleep(200);
            System.out.println(Thread.currentThread().getName() +" "+count +" 获取到了锁");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release(count);
            System.out.println(Thread.currentThread().getName() +" "+count +" 释放了锁");
        }
    }
}
