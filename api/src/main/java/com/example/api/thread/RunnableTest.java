package com.example.api.thread;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author shenzm
 * @date 2019-2-20
 * @description 作用
 */
public class RunnableTest {


    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for(int i=0; i<1000_0000; i++){
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("java");
                }
            });
        }
        executorService.shutdown();
    }
}
