package com.example.api.lock;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author shenzm
 * @date 2019-2-15
 * @description 作用
 */
public class BaseLock {

    private final ZkClient zkClient;
    private final String path;
    private final String basePath;
    private final String lockName;

    private static final Integer MAX_RETRY_COUNT = 10;

    public BaseLock(ZkClient zkClient, String basePath, String lockName) {
        this.zkClient = zkClient;
        this.basePath = basePath;
        this.path = basePath.concat("/").concat(lockName);
        if (!zkClient.exists(basePath)) {
            zkClient.createEphemeral(basePath);
        }
        this.lockName = lockName;
    }


    private boolean waitForLock(long startMillis, Long millisToWait, String lockPath) throws Exception {
        boolean holdLock = false;
        boolean deleteLockPath = false;

        try {
            while (!holdLock) {
                List<String> children = getSortedChildren();
                String nodeSequenceNum = lockPath.substring(basePath.length() + 1);
                int num = children.indexOf(nodeSequenceNum);
                if (num < 0) {
                    throw new ZkNoNodeException("没有找到节点" + nodeSequenceNum);
                }
                boolean isGetTheLock = num == 0;
                if (isGetTheLock) {
                    holdLock = true;
                } else {
                    String pathToWatch = children.get(num - 1);
                    String prevPath = basePath.concat("/").concat(pathToWatch);
                    final CountDownLatch latch = new CountDownLatch(1);
                    final IZkDataListener prevDataListener = new IZkDataListener() {
                        @Override
                        public void handleDataChange(String dataPath, Object data) throws Exception {

                        }

                        @Override
                        public void handleDataDeleted(String dataPath) throws Exception {
                            latch.countDown();
                        }
                    };
                    try {
                        zkClient.subscribeDataChanges(prevPath, prevDataListener);

                        if (millisToWait != null) {
                            millisToWait -= (System.currentTimeMillis() - startMillis);
                            if (millisToWait <= 0) {
                                deleteLockPath = true;
                                break;
                            }
                            latch.await(millisToWait, TimeUnit.MICROSECONDS);
                        } else {
                            latch.await();
                        }
                    } catch (ZkNoNodeException e) {
                        e.printStackTrace();
                    } finally {
                        zkClient.unsubscribeDataChanges(prevPath, prevDataListener);
                    }
                }
            }
        } catch (Exception e) {
            deleteLockPath = true;
            throw e;
        } finally {
            if (deleteLockPath) {
                deleteLockPath(lockPath);
            }
        }
        return holdLock;
    }

    private void deleteLockPath(String lockPath) throws Exception {
        zkClient.delete(lockPath);
    }

    protected void releaseLock(String lockPath) throws Exception {
        deleteLockPath(lockPath);
    }

    protected String tryGetLock(long timeOut, TimeUnit unit) throws Exception {
        long startMillis = System.currentTimeMillis();
        Long millisToWait = unit != null ? unit.toMillis(timeOut) : null;

        String lockPath = null;
        boolean holdLock = false;
        boolean isDone = false;
        int retryCount = 0;

        while (!isDone) {
            isDone = true;
            try {
                lockPath = createLockNode(zkClient, path);
                holdLock = waitForLock(startMillis, millisToWait, lockPath);
            } catch (ZkNoNodeException e) {

                if (retryCount++ < MAX_RETRY_COUNT) {
                    isDone = false;
                } else {
                    throw e;
                }
            }
        }

        if (holdLock) {
            return lockPath;
        }
        return null;
    }

    private String createLockNode(ZkClient zkClient, String path) throws Exception {
        return zkClient.createEphemeralSequential(path, new byte[0]);
    }


    private List<String> getSortedChildren() {
        try {
            List<String> children = zkClient.getChildren(basePath);
            Collections.sort(children, new Comparator<String>() {
                @Override
                public int compare(String lhs, String rhs) {
                    return getLockNodeNumber(lhs, lockName).compareTo(getLockNodeNumber(rhs, lockName));
                }
            });
            return children;
        } catch (ZkNoNodeException e) {
            zkClient.createPersistent(basePath, true);
            return getSortedChildren();
        }
    }

    private String getLockNodeNumber(String str, String lockName) {
        int index = str.lastIndexOf(lockName);
        if (index >= 0) {
            index += lockName.length();
            return index <= str.length() ? str.substring(index) : "";
        }
        return str;
    }


}
