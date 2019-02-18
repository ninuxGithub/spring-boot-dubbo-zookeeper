package com.example.api.completableFuture;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author shenzm
 * @date 2019-2-18
 * @description 作用
 */
public class AsyncTest {
    static CountDownLatch latch = new CountDownLatch(2);

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(4, 5, 100, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());
        testThenAcceptAsync(threadPoolExecutor);

        testThenApply(threadPoolExecutor);

        testTheCompose(threadPoolExecutor);


    }


    /**
     * thenCompose 返回的是一个 CompletableFuture
     * @param threadPoolExecutor
     */
    private static void testTheCompose(ThreadPoolExecutor threadPoolExecutor) {
        Map<String, Integer> map = new HashMap<>();
        CompletableFuture.supplyAsync(() -> {
            int v = (int) (Math.random() * 100);
            map.put(Thread.currentThread().getName(), v);
            System.out.println(v);
            return map;
        }, threadPoolExecutor).thenCompose(m -> {

            return CompletableFuture.supplyAsync(() -> {
                m.put("bb", 2);
                return m;
            });
        }).whenCompleteAsync((r, e) -> {
            System.out.println("testTheCompose: "+r);
            threadPoolExecutor.shutdown();
        });
    }


    /**
     * thenApply  调用  supplyAsync  的结果  继续做一些事情   有返回结果
     * @param threadPoolExecutor
     */
    private static void testThenApply(ThreadPoolExecutor threadPoolExecutor) {
        Map<String, Integer> map = new HashMap<>();
        CompletableFuture<Map<String, Integer>> aa = CompletableFuture.supplyAsync(() -> {
            int v = (int) (Math.random() * 100);
            map.put(Thread.currentThread().getName(), v);
            System.out.println(v);

            return map;
        }, threadPoolExecutor).thenApply((Map<String, Integer> m) -> {
            for (String key : m.keySet()) {
                m.put("aa", 1);
            }
            System.out.println("thenApply: " + m);
            return m;
        }).whenCompleteAsync((r, e) -> {
            System.out.println("whenCompleteAsync" + r);
            //threadPoolExecutor.shutdown();
            latch.countDown();
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean done = aa.isDone();
        System.out.println("任务是否执行完毕： "+done);
        try {
            System.out.println("结果是："+aa.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * thenAcceptAsync  接收    supplyAsync返回的参数  然后没有返回结果
     * @param threadPoolExecutor
     */
    private static void testThenAcceptAsync(ThreadPoolExecutor threadPoolExecutor) {
        Map<String, Integer> map = new HashMap<>();
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            int v = (int) (Math.random() * 100);
            map.put(Thread.currentThread().getName(), v);
            System.out.println(v);
            return true;
        }, threadPoolExecutor).thenAcceptAsync(flag -> {
            if (flag) {
                for (String name : map.keySet()) {
                    Integer value = map.get(name);
                    map.put(name, value + 10);
                    System.out.println(name + "   " + map.get(name));
                }
            }
        }, threadPoolExecutor).whenCompleteAsync((r, e) -> {
            System.out.println(r);
            System.out.println(e);
            System.out.println("任务执行完毕...");
            //如果后续有任务执行  不可以shutdown
            //threadPoolExecutor.shutdown();
            latch.countDown();
        });

        future.join();
    }

}
