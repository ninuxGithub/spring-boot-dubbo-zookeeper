package com.example.api.zookeeper.lock;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * https://www.cnblogs.com/zwgblog/p/6972531.html  zookeeper 权限
 *
 * @author shenzm
 * @date 2019-7-3
 * @description 作用
 */
public class ZookeeperLock {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperLock.class);

    static final String parent = "/lock";

    static final String path = "/lock/node-";

    volatile ThreadLocal<String> lockPath = new ThreadLocal<>();

    private ZkClient zkClient;

    public ZookeeperLock(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

    /**
     * 分布式锁： 没有超时时间
     * @return
     */
    public boolean lock() {
        return timeLock(null, TimeUnit.MICROSECONDS);
    }


    /**
     * 设定了超时时间
     * @param waitTime
     * @param timeUnit
     * @return
     */
    public synchronized boolean timeLock(Long waitTime, TimeUnit timeUnit) {
        if (!zkClient.exists(parent)) {
            zkClient.createPersistent(parent);
        }
        boolean deleteLockKey = false;
        String currentNode = lockPath.get();
        try {
            if (null == currentNode) {
                currentNode = zkClient.createEphemeralSequential(path, "java");
                lockPath.set(currentNode);

                String[] arr = currentNode.split("-");
                String sequence = arr[1];

                //获取到/lock节点下创建了多少临时子节点 [node-0000000051, node-0000000050]
                List<String> nodeList = zkClient.getChildren(parent);
                logger.info(nodeList.toString());

                //获取临时节点的最小的一个序列号 0000000050
                String minSequence = nodeList.stream().map(r -> r.split("-")[1]).sorted(Comparator.comparing(String::valueOf)).findFirst().orElse(null);

                //如果当前的节点是最新的那个子节点，说明获取到了锁对象
                if (sequence.equals(minSequence)) {
                    logger.info("所节点的名称为： " + lockPath.get());
                    return true;
                }

                //获取前一个节点
                String preNode = getPrevousNode(currentNode, nodeList);

                if (null != preNode) {
                    logger.info(preNode + "添加监听");
                    CountDownLatch countDownLatch = new CountDownLatch(1);
                    try {
                        IZkDataListener dataListener = new IZkDataListener() {
                            @Override
                            public void handleDataChange(String dataPath, Object data) throws Exception {
                                logger.info(dataPath + "节点数据改变" + data);
                            }

                            @Override
                            public void handleDataDeleted(String dataPath) throws Exception {
                                logger.info(dataPath + "节点删除");
                                countDownLatch.countDown();
                            }
                        };
                        zkClient.subscribeDataChanges(preNode, dataListener);
                    } catch (Exception e) {
                        return false;
                    }

                    //根据是否设置等待时间进行调节
                    if (null == waitTime) {
                        countDownLatch.await();
                    } else {
                        countDownLatch.await(waitTime, timeUnit);
                    }
                    logger.info("等待唤醒 ： " + lockPath.get());
                    return true;
                }
            }

        } catch (Exception e) {
            deleteLockKey = true;
        } finally {
            if (null != lockPath.get() && deleteLockKey) {
                zkClient.delete(lockPath.get());
                lockPath.remove();
            }
        }
        return false;
    }

    private String getPrevousNode(String currentNode, List<String> nodeList) {
        //排序节点
        List<String> sortedNodeList = nodeList.stream()
                .map(r -> r.split("-")[1])
                .sorted(Comparator.comparing(String::valueOf))
                .map(r -> path + r)
                .collect(Collectors.toList());

        //获取到当前节点的前一个node
        String preNode = null;
        for (int i = 0; i < sortedNodeList.size(); i++) {
            if (currentNode.equals(sortedNodeList.get(i)) && i > 0) {
                preNode = sortedNodeList.get(i - 1);
            }
        }
        return preNode;
    }

    public void releaseLock() {
        if (zkClient != null && null != lockPath.get()) {
            zkClient.delete(lockPath.get());
            lockPath.remove();
        }
    }

}
