package com.example.api.thread.csdn.test;

import static java.util.concurrent.Executors.newFixedThreadPool;


import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;

public class ThreadTest {

    /**
     * 测试的线程数
     */
    private int threadNum = 3;

    private ExecutorService executorService = newFixedThreadPool(this.threadNum);

    protected void initThreadPool(int threadNum) {
        if (threadNum == this.threadNum) {
            return;
        }
        this.threadNum = threadNum;
        this.executorService = newFixedThreadPool(threadNum);
    }

    ;

    public int getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }
}

