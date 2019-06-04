package com.example.api.thread.csdn.test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

public class ExchangerTest extends ThreadTest {

    private Exchanger<List<Integer>> exchanger = new Exchanger<>();

    @Test
    public void test() throws InterruptedException {
        super.initThreadPool(2);
        DataConsumer consumer = new DataConsumer(exchanger);
        DataProducer producer = new DataProducer(exchanger);
        consumer.start();
        producer.start();
//        super.getExecutorService().submit(consumer);
//        super.getExecutorService().submit(producer);

    }

    class DataConsumer extends Thread /*implements Runnable*/ {
        private Exchanger exchanger;

        public DataConsumer(Exchanger exchanger) {
            this.exchanger = exchanger;
        }

        private List<Integer> dataIn = new ArrayList<>();
        private List<Integer> dataOut = new ArrayList<>();

        @Override
        public void run() {
            System.out.println("consumer执行！");
            try {
                dataOut = (List<Integer>) this.exchanger.exchange(dataIn);

                //求和能够正常执行
//                int sum = 0;
//                for (int i : dataOut) {
//                    sum += i;
//                }

                System.out.println(dataOut);

                //求和不能正常执行，无返回结果，其后的代码不执行，控制台无异常信息打印
//                int sum = dataOut.stream().mapToInt(Integer::intValue).sum();
                int sum = dataOut.stream().reduce((summary , num) -> summary += num).get();

                System.out.println("总和：" + sum);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //无法正常的实现jsonString的转换
//            String jsonString = JSON.toJSONString(data);
//            System.out.println("获取的数据为：" + dataOut.size());
        }
    }

    class DataProducer extends Thread/* implements Runnable*/{
        private Exchanger exchanger;

        public DataProducer(Exchanger exchanger) {
            this.exchanger = exchanger;
        }

        private List<Integer> dataIn = new ArrayList<>();

        @Override
        public void run() {
            System.out.println("producer执行！");
            for (int i = 1; i <= 10; i++) {
                dataIn.add(i);
            }
            try {
                this.exchanger.exchange(dataIn);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
    }
}