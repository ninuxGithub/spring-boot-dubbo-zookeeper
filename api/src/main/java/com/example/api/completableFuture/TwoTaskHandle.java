package com.example.api.completableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * 2个任务异步的执行  然后将结果交给 thenCombine 在做一次混合计算再返回
 *
 * @author shenzm
 * @date 2019-5-10
 * @description 作用
 */
public class TwoTaskHandle {

    public static void main(String[] args) throws Exception {
        thenCombine();
    }

    //from https://www.jianshu.com/p/6bac52527ca4
    private static void thenCombine() throws Exception {
        System.out.println("开始执行任务");
        long t1 = System.currentTimeMillis();
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                long t2 = System.currentTimeMillis();
                System.out.println("future1 : " + (t2 - t1));
                return "hello";
            }
        });
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(new Supplier<String>() {
            @Override
            public String get() {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                long t3 = System.currentTimeMillis();
                System.out.println("future2 : " + (t3 - t1));
                return "world";
            }
        });
        CompletableFuture<String> result = future1.thenCombine(future2, new BiFunction<String, String, String>() {
            @Override
            public String apply(String t, String u) {
                long t4 = System.currentTimeMillis();
                System.out.println("result : " + (t4 - t1));
                return t + " " + u;
            }
        });
        System.out.println(result.get());
        long t5 = System.currentTimeMillis();
        System.out.println("get : " + (t5 - t1));
    }
}
