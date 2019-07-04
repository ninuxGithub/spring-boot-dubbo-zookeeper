package com.example.api.zookeeper.selectmaster;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkInterruptedException;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author shenzm
 * @date 2019-2-14
 * @description 作用
 */
public class WorkerServer {

    private volatile boolean running = false;

    private ZkClient zkClient;

    public static final String MASTER_PATH = "/master";

    private IZkDataListener dataListener;

    private RunningData serverData;

    private RunningData masterData;

    private ScheduledExecutorService delayExecutor = Executors.newScheduledThreadPool(1);

    private int delayTime = 5;

    public WorkerServer(RunningData runningData, ZkClient zkClient) {
        this.serverData = runningData;
        this.zkClient = zkClient;
        this.dataListener = new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {

            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                if (masterData != null && masterData.getName().equals(serverData.getName())) {
                    takeMaster();
                } else {
                    delayExecutor.schedule(new Runnable() {
                        @Override
                        public void run() {
                            takeMaster();
                        }
                    }, delayTime, TimeUnit.SECONDS);
                }
            }
        };
    }

    private void takeMaster() {
        if (!running) {
            return;
        }
        try {
            zkClient.create(MASTER_PATH, serverData, CreateMode.EPHEMERAL);
            masterData = serverData;
            System.out.println(serverData.getName()+" is master");
            delayExecutor.schedule(new Runnable() {
                @Override
                public void run() {
                    if (checkMaster()) {
                        releaseMaster();
                    }
                }
            }, delayTime, TimeUnit.SECONDS);
        } catch (ZkNodeExistsException e) {
            RunningData runningData = zkClient.readData(MASTER_PATH, true);
            if (null == runningData) {
                takeMaster();
            } else {
                masterData = runningData;
            }
        } catch (Exception e) {
            //
        }
    }

    private void releaseMaster() {
        if (checkMaster()) {
            zkClient.delete(MASTER_PATH);
        }
    }

    private boolean checkMaster() {
        try {
            RunningData runningData = zkClient.readData(MASTER_PATH);
            masterData = runningData;
            if (masterData.getName().equals(serverData.getName())) {
                return true;
            }
            return false;
        } catch (ZkNoNodeException e) {
            return false;
        } catch (ZkInterruptedException e) {
            return checkMaster();
        } catch (Exception e) {
            return false;
        }
    }

    public void start() throws Exception {
        if (running) {
            throw new RuntimeException("server is running...");
        }
        running = true;
        zkClient.subscribeDataChanges(MASTER_PATH, dataListener);
        takeMaster();
    }

    public void stop() {
        if (!running) {
            throw new RuntimeException("server is stopped");
        }
        running = false;
        delayExecutor.shutdown();
        zkClient.unsubscribeDataChanges(MASTER_PATH, dataListener);
        releaseMaster();
    }
}
