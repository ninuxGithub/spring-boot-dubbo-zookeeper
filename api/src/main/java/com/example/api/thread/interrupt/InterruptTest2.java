package com.example.api.thread.interrupt;

import java.util.concurrent.TimeUnit;

/**
 * @author shenzm
 * @date 2019-5-13
 * @description 作用
 */
public class InterruptTest2 {

    public static void main(String[] args) {


        Thread t = new Thread(){
            @Override
            public void run() {
                while (true){
                    if (this.isInterrupted()){
                        System.out.println("线程被中断");
                        break;
                    }
                }
            }
        };

        t.start();

        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t.interrupt();

        System.out.println(t.isInterrupted());
        System.out.println(t.isInterrupted());
        System.out.println(t.interrupted());
    }
}
