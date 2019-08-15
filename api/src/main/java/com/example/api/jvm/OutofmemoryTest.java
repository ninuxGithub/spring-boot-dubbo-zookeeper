package com.example.api.jvm;

import org.apache.xalan.xsltc.trax.TemplatesImpl;

import java.util.concurrent.TimeUnit;

/**
 * vm options :  -Xms128m -Xmx1024m -XX:MaxPermSize=512m
 * vm options :  -Xms32m -Xmx64m -XX:MaxPermSize=64m
 *
 * -Xms32m -Xmx64m -XX:MaxPermSize=64m -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:SurvivorRatio=8 -XX:+HeapDumpOnOutOfMemoryError
 *
 * https://blog.csdn.net/yxc135/article/details/12137663
 * https://www.cnblogs.com/stateis0/p/9062192.html
 *  Parallel Scanvenge : 根据系统的状态给出吞吐量最高GC的配置；
 *      -XX:MaxGCPauseMillis 设置最大垃圾收集停顿时间，他的值是一个大于0的整数。
 *      ParallelGC 工作时，会调整 Java 堆大小或者其他的一些参数，尽可能的把停顿时间控制在 MaxGCPauseMillis 以内。
 *      如果为了将停顿时间设置的很小，将此值也设置的很小，那么 PS 将会把堆设置的也很小，这将会到值频繁 GC ，虽然系统停顿时间小了，但总吞吐量下降了。
 *
 *
 *      -XX:GCTimeRatio 设置吞吐量大小，他的值是一个0 到100之间的整数，假设 GCTimeRatio 的值是 n ，
 *      那么系统将花费不超过 1/(1+n) 的时间用于垃圾收集，默认 n 是99，即不超过1% 的时间用于垃圾收集
 *
 *      -XX:+UseParallelGC 新生代使用 ParallelGC 回收器，老年代使用串行回收器。
 *
 *
 *      -XX:+UseParallelOldGC 新生代使用 ParallelGC 回收器，老年代使用 ParallelOldGC 回收器。
 *
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
public class OutofmemoryTest extends ThreadGroup {

    static Thread thread1 = null;
    static Thread thread2 = null;

    public OutofmemoryTest() {
        super("super");
    }


    //父线程出现oom后， 子线程依然是可以运行的；
    //子线程出现了oom, 父线程还是可以运行的
    public static void main(String[] args) {

        //子线程抛异常  被捕捉
        new Thread(new OutofmemoryTest(), new Runnable() {
            @Override
            public void run() {
                throw new RuntimeException("oom");
            }
        }, "ex-thread").start();

        thread1 = new Thread(new Runnable() {
            @Override
            public void run() {

                //子线程
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                System.out.println(thread1.getName() + "是否存活：" + thread1.isAlive());
                                TimeUnit.SECONDS.sleep(2);
                                Integer[] arr = new Integer[1024 * 1024 * 8];
                                System.out.println("====>" + Thread.currentThread().getName());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, "线程1-son").start();


                while (true) {
                    try {
                        TimeUnit.SECONDS.sleep(2);
                        System.out.println("====>" + Thread.currentThread().getName());
                        Integer[] arr = new Integer[1024 * 1024 * 8];
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "线程1");
        thread1.start();

        thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                System.out.println(thread2.getName() + "是否存活：" + thread2.isAlive());
                                TimeUnit.SECONDS.sleep(2);
                                System.out.println("====>" + Thread.currentThread().getName());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, "线程2-son").start();
                try {
                    while(true){

                        TimeUnit.SECONDS.sleep(2);
                        System.out.println("====>" + Thread.currentThread().getName());
                        Integer[] arr = new Integer[1024 * 1024 * 8];
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "线程2");
        thread2.start();

    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println(t.getName() + " " + t.getId());
        e.printStackTrace();
    }
}
