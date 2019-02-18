package com.example.api.lock;

import org.I0Itec.zkclient.ZkClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author shenzm
 * @date 2019-2-15
 * @description 作用
 */
public class LockImpl extends BaseLock implements ZLock {

    private static final String LOCK_NAME = "lock-";

    private final String basePath;

    private String lockPath;

    public LockImpl(ZkClient zkClient, String basePath) {
        super(zkClient, basePath, LOCK_NAME);
        this.basePath = basePath;
    }

    @Override
    public void getLock() throws Exception {
        lockPath = tryGetLock(-1, null);
        if (lockPath == null) {
            throw new IOException("在" + basePath + "路径不能获取锁");
        }
    }

    @Override
    public boolean getLock(long timeOut, TimeUnit unit) throws Exception {
        lockPath = tryGetLock(timeOut, unit);
        return lockPath != null;
    }

    @Override
    public void releaseLock() throws Exception {
        releaseLock(lockPath);
    }
}
