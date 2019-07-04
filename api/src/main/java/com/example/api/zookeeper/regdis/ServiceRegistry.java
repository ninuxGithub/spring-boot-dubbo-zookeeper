package com.example.api.zookeeper.regdis;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author shenzm
 * @date 2019-2-13
 * @description 作用
 */
public class ServiceRegistry {

    private static final Logger logger = LoggerFactory.getLogger(ServiceRegistry.class);

    private CountDownLatch latch = new CountDownLatch(1);

    private String registryAddress;

    private int defaultSessionTimeout = 5000;

    private String defalutRootPath = "/zoo";

    private String defaultChildPath = defalutRootPath + "/data";

    public ServiceRegistry(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    /**
     * 将数据注册到zookeeper
     *
     * @param data
     */
    public void registry(String data) {
        if (null != data) {
            ZooKeeper zk = connectZookeeper();
            if (null != zk) {
                createRootNode(zk);
                createChildNode(zk, data);
            }

        }
    }

    /**
     * 创建一个子节点
     *
     * @param zk
     */
    private void createChildNode(ZooKeeper zk, String data) {
        try {
            byte[] bytes = data.getBytes();
            String path = zk.create(defaultChildPath, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            logger.info("create zookeeper path : {}  data :{}", path, data);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建root 父节点
     *
     * @param zk
     */
    private void createRootNode(ZooKeeper zk) {
        try {
            Stat exists = zk.exists(defalutRootPath, false);
            if (exists == null) {
                zk.create(defalutRootPath, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接zookeeper
     *
     * @return
     */
    private ZooKeeper connectZookeeper() {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(registryAddress, defaultSessionTimeout, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    latch.countDown();
                }
            });
            latch.await();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return zk;
    }


}
