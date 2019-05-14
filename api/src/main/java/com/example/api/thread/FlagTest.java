package com.example.api.thread;

/**
 * @author shenzm
 * @date 2019-5-13
 * @description 作用
 */
public class FlagTest {

    volatile boolean flag = false;

    public static void main(String[] args) throws InterruptedException {
        new FlagTest().flagTest();
    }

    private void flagTest() throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (flag) {
                        System.out.println("退出程序");
                        break;
                    } else {
                        System.out.println("running...");
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


        Thread.sleep(200);

        new Thread(new Runnable() {
            @Override
            public void run() {
                flag = true;
            }
        }).start();
    }

}


