package com.example.api.thread.csdn.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

public class ExchangerTest2 {
    protected static final Logger log = Logger.getLogger(ExchangerTest.class);
    private static volatile boolean isDone = false;

    static class ExchangerProducer implements Runnable {
        private Exchanger<List<Integer>> exchanger;
        ExchangerProducer(Exchanger<List<Integer>> exchanger) {
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            while (!Thread.interrupted() && !isDone) {
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                    List<Integer> list = new ArrayList<>();
                    for (int index =0; index <10; index ++){
                        list.add(index);
                    }
                    exchanger.exchange(list);
                    Integer summary = list.stream().reduce((sum, e) -> sum += e).get();
                    System.out.println(summary);
                } catch (InterruptedException e) {
                    log.error(e, e);
                }
                isDone = true;
                System.out.println("ExchangerProducer : "+isDone);
            }
        }
    }

    static class ExchangerConsumer implements Runnable {
        private Exchanger<List<Integer>> exchanger;
        List<Integer> list = new ArrayList<>();
        ExchangerConsumer(Exchanger<List<Integer>> exchanger) {
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            while (!Thread.interrupted() && !isDone) {
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                    list = exchanger.exchange(list);
                } catch (InterruptedException e) {
                    log.error(e, e);
                }
                System.out.println("consumer after : " + list);
                if (list.size()>0){
                    isDone = true;
                    System.out.println("ExchangerConsumer : "+isDone);
                }

            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        ExecutorService exec = Executors.newFixedThreadPool(5);
        try {
            Exchanger<List<Integer>> exchanger = new Exchanger<List<Integer>>();
            ExchangerProducer producer = new ExchangerProducer(exchanger);
            ExchangerConsumer consumer = new ExchangerConsumer(exchanger);
            exec.execute(producer);
            exec.execute(consumer);
            exec.shutdown();
        } catch (Exception e) {
            log.error(e, e);
        }finally {
            //exec.shutdownNow();
        }
    }
}
