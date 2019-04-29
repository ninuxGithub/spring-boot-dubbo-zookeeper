package com.example.api.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author shenzm
 * @date 2019-2-18
 * @description 作用  测试CyclicBarrier 执行三个线性获取 学生的得分  ， 然后 调用一个线程去计算平均分 （依赖之前的得分）
 */
public class CyclicBarrierTest implements Runnable {


    private ExecutorService executorService = Executors.newFixedThreadPool(6);

    private int num = 6;

    private CyclicBarrier cb = new CyclicBarrier(num, this); //先执行3个线程 到达barrier，然后执行一个线程

    private Map<String, Integer> thread2Score = new HashMap<>();


    @Override
    public void run() {
        System.out.println("开始跑");
        executorService.shutdown();
    }


    public static void main(String[] args) {
        CyclicBarrierTest cyclicBarrierTest = new CyclicBarrierTest();
        cyclicBarrierTest.calcAvg();
    }

    private void calcAvg() {
        for (int i = 0; i < num; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() +"预备开始跑！");
                    try {
                        cb.await();
                        System.out.println(Thread.currentThread().getName() +"running....");
                        TimeUnit.SECONDS.sleep(2);
                        System.out.println(Thread.currentThread().getName() +"结束....");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
