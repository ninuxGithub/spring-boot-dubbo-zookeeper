package com.example.api.zookeeper.namedservice;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author shenzm
 * @date 2019-2-18
 * @description 作用  测试CyclicBarrier 执行三个线性获取 学生的得分  ， 然后 调用一个线程去计算平均分 （依赖之前的得分）
 */
public class CyclicBarrierTest implements Runnable {


    private ExecutorService executorService = Executors.newFixedThreadPool(3);

    private int num = 3;

    private CyclicBarrier cb = new CyclicBarrier(num, this); //先执行3个线程 到达barrier，然后执行一个线程

    private Map<String, Integer> thread2Score = new HashMap<>();


    @Override
    public void run() {
        Integer sumUp = thread2Score.values().stream().reduce((sum, n) -> sum += n).get();
        System.out.println(sumUp / 3d);
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
                    int score = (int) (Math.random() * 100);
                    System.out.println(Thread.currentThread().getName() +" 得分："+score);
                    thread2Score.put(Thread.currentThread().getName(), score);
                    try {
                        cb.await();
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
