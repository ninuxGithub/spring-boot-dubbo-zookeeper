package com.example.api.jvm;

import org.apache.xalan.xsltc.trax.TemplatesImpl;

import java.util.concurrent.TimeUnit;

/**
 * vm options :  -Xms128m -Xmx1024m -XX:MaxPermSize=512m
 * vm options :  -Xms32m -Xmx64m -XX:MaxPermSize=64m
 *
 *
 *
 *  线程1 ： OutofMemoryError 异常可以别try catuch ，oom 之后会到值推的内存被回收， 但是不是所有的内存都被回收
 *
 *  线程2 ： 有约s是可达的，所以不会被回收 然后线程 内部创建对象 当内存达到上限也会出现异常
 *
 *  所有的线程结束后  程序退出
 *
 * @author shenzm
 * @date 2019-4-30
 * @description 作用
 */
//java.lang.OutOfMemoryError: Java heap space
//java.lang.OutOfMemoryError: unable to create new native thread
//java.lang.OutOfMemoryError: PermGen space
//java.lang.OutOfMemoryError: Requested array size exceeds VM limit
public class OutofmemoryTest {

    public static void main(String[] args) {
        String s = new String("java");

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){

                    System.out.println(s);
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        Integer[] arr = new Integer[1024 * 1024 * 5];
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        },"线程1").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(true){

                        Integer[] arr = new Integer[1024 * 1024 * 10];
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },"线程2").start();

    }
}
