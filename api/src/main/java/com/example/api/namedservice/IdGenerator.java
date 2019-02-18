package com.example.api.namedservice;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author shenzm
 * @date 2019-2-18
 * @description 作用
 */
public class IdGenerator {

    private  ZkClient zkClient;

    private final String server;

    private final String root;

    private final String nodeName;

    /*private volatile ThreadLocal<Boolean> zkRunning = new ThreadLocal<Boolean>(){
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };*/

    private ExecutorService cleanExecutor = null;

    public IdGenerator(String server, String root, String nodeName) throws Exception {
        this.server = server;
        this.root = root;
        this.nodeName = nodeName;
        start();
    }

    private void start() throws Exception {
        synchronized (IdGenerator.class){
            /*if (zkRunning.get()) {
                throw new RuntimeException("server has started....");
            }
            zkRunning.set(true);*/
            zkClient = new ZkClient(server, 5000, 5000, new BytesPushThroughSerializer());
            cleanExecutor = Executors.newFixedThreadPool(10);
            try {
                zkClient.createPersistent(root, true);
            } catch (ZkNoNodeException e) {
                e.printStackTrace();
            }
        }
    }



    public void stop() throws Exception {
        synchronized (IdGenerator.class){
            /*if (!zkRunning.get()) {
                throw new RuntimeException("server has stopped...");
            }
            zkRunning.set(false);*/
            freeResource();
        }
    }

    private void freeResource() {
        try {
            cleanExecutor.awaitTermination(2, TimeUnit.SECONDS);
            cleanExecutor.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            cleanExecutor = null;
        }
        if (zkClient != null) {
            zkClient.close();
            zkClient = null;
        }
    }

    public String generateId(RemoveMethod removeMethod) throws Exception {
        checkRunning();
        String fullNodePath = root.concat("/").concat(nodeName);
        String psPath = zkClient.createPersistentSequential(fullNodePath, null);
        if (removeMethod.equals(RemoveMethod.IMMEDIATELY)) {
            zkClient.delete(psPath);
        } else if (removeMethod.equals(RemoveMethod.DELAY)) {
            cleanExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    zkClient.delete(psPath);
                }
            });
        }
        return extractId(psPath);
    }

    private String extractId(String str) {
        int i = str.lastIndexOf(nodeName);
        if (i >= 0) {
            i += nodeName.length();
            return i <= str.length() ? str.substring(i) : "";
        }
        return null;
    }

    private void checkRunning() throws Exception {
        synchronized (IdGenerator.class) {
            /*if (!zkRunning.get()) {
                throw new Exception("zk 未连接");
            }*/
        }
    }


}
