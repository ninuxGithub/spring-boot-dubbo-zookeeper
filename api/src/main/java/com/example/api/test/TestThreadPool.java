package com.example.api.test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * 运行结果出现了： test  java42
 *
 * 分析： 说明队列满了， 线程达到了max size  执行了拒绝策略； 通过调用者执行了任务； 此时的调用者的线程名称为“test” 故所以出现结果  test javaxxx
 *
 * 结论： 拒绝策略CallerRunsPolicy 是交给调用者执行任务的；
 *
 * @author shenzm
 * @date 2019-3-19
 * @description 作用
 */
public class TestThreadPool {



    public static void main(String[] args) {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 5, 100, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(20), new ThreadPoolExecutor.CallerRunsPolicy());

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<100; i++){
                    final int counter = i;
                    threadPool.execute(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(Thread.currentThread().getName() + "  java"+ counter);
                        }
                    });
                }

                threadPool.shutdown();
            }
        },"test").start();





    }
}
