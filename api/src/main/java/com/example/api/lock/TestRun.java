package com.example.api.lock;

import sun.misc.Unsafe;

/**
 * @author shenzm
 * @date 2019-2-28
 * @description 作用
 */
public class TestRun implements Runnable {

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "-----------------java");
    }
    public static void main(String[] args) {
        new TestRun().run();

        final Unsafe unsafe = Unsafe.getUnsafe();
    }


}
