package com.example.api.thread;

/**
 * @author shenzm
 * @date 2019-5-13
 * @description 作用
 */
public class VolatleTest {

    volatile int a = 0;
    volatile int b = 0;
    volatile int c = 0;
    volatile int result = 0;

    public static void main(String[] args) {
        new VolatleTest().singleVariable();
        new VolatleTest().multiVariable();
    }

    public void singleVariable() {
        for (int t = 0; t < 20; t++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (VolatleTest.class) {
                        for (int i = 0; i < 1000; i++) {
                            synchronized (VolatleTest.class) {
                                a++;
                            }
                        }
                        System.out.println("a  " + a);
                    }
                }
            }).start();
        }
    }

    public void multiVariable() {
        for (int t = 0; t < 4; t++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (VolatleTest.class) {
                        for (int i = 0; i < 10000; i++) {
                            b++;
                            c++;
                            result = b + c;
                        }
                        System.out.println(result);
                    }
                }
            }).start();
        }
    }
}
