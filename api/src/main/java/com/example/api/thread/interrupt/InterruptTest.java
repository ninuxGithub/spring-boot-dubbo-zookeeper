package com.example.api.thread.interrupt;


/**
 * 总结  ： 线程调用interrupt() 会对线性发起一个中断的标记， 至于线程是否中断是由线程自己控制是否进行程序的停止中断
 * <p>
 * 如何中断线程  在run里面 通过判断 isIntterupted方法 ， 并且做退出的逻辑控制
 * <p>
 * 在线程里面如有sleep , wait , join 会抛出中断异常 擦除中断的标记
 * <p>
 * interrupted() 方法会擦除中断的标记
 *
 * @author shenzm
 * @date 2019-5-13
 * @description 作用
 */
public class InterruptTest {

    public static void main(String[] args) {
        MyThread t = new MyThread();
        t.start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t.interrupt();


        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println("线程调用中断");
        System.out.println("isInterrupted " + t.isInterrupted());
        System.out.println("isInterrupted2 " + t.isInterrupted());

        System.out.println("interrupted " + MyThread.interrupted());
        System.out.println("interrupted2 " + MyThread.interrupted());
        System.out.println(t.getState());
    }
}

class MyThread extends Thread {
    @Override
    public void run() {
        while (true) {
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("MyThread 被 interrupt 方法中断了");
                break;
            }


            //sleep 擦除中断标记
            /*try {
                //调用 sleep , join , wait 的时候 在调用interrupt()方法的时候会抛出异常  InterruptException,
                //这个异常会清除线程的中断的状态  导致的结果就是  Thread.currentThread().isInterrupted() 一直为false
                Thread.sleep(100);
                System.out.println("sleep end .....");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    }
}
