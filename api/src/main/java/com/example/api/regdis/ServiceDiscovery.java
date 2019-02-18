package com.example.api.regdis;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author shenzm
 * @date 2019-2-13
 * @description 作用
 */
public class ServiceDiscovery {

    private static final Logger logger = LoggerFactory.getLogger(ServiceDiscovery.class);

    private CountDownLatch latch = new CountDownLatch(1);

    private volatile List<String> dataList = new ArrayList<>();

    private String registryAddress;

    private int defaultSessionTimeout = 5000;

    private String defalutRootPath = "/zoo";

    private ZooKeeper zookeeper;

    public ServiceDiscovery(String registryAddress) {
        this.registryAddress = registryAddress;
        this.zookeeper = connectZookeeper();
        if (null != zookeeper) {
            loadNode(zookeeper);
        }
    }

    private void loadNode(ZooKeeper zk) {
        try {
            List<String> childNodeList = zk.getChildren(defalutRootPath, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getType() == Event.EventType.NodeChildrenChanged) {
                        loadNode(zk);
                    }
                }
            });

            for (String node : childNodeList) {
                byte[] data = zk.getData(defalutRootPath + "/" + node, false, null);
                dataList.add(new String(data));
            }

            logger.info("load zookeeper child node : {}", dataList);

            //update service info
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
