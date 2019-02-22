package com.example.api.redis;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author shenzm
 * @date 2019-2-22
 * @description 作用
 * <p>
 * sleep 不释放锁对象 , 但是让出cpu的执行权
 * <p>
 * wait 释放锁  进入等待池  等待notify唤醒
 */
public class RedisLockUtil {

    private static final Logger logger = LoggerFactory.getLogger(RedisLockUtil.class);

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final Long RELEASE_SUCCESS = 1L;

    static ExecutorService threadPool = Executors.newFixedThreadPool(5);


    public static boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, int expireTime) {
        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
        if (result == null) {
            logger.info("tryLock 的结果为null");
        }
        if (null != result && LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }


    public static boolean releaseDistributedLock(Jedis jedis, String lockKey, String requestId) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        if (result == null) {
            logger.info("releaseLock 的结果为null");
        }
        if (null != result && RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;

    }

    public static Object exeTask(Jedis jedis, Callable callable, String lockKey) {
        String threadName = Thread.currentThread().getName();
        String requestId = UUID.randomUUID().toString();
        int expireTime = 10_000;
        Object result = null;
        synchronized (RedisLockUtil.class) {
            try {
                boolean tryLock = tryGetDistributedLock(jedis, lockKey, requestId, expireTime);
                while (!tryLock) {
                    try {
                        //如果没有获取到锁 等待别的线程任务执行完毕释放锁
                        Thread.sleep(10);
                    } catch (InterruptedException e) {

                    }
                    tryLock = tryGetDistributedLock(jedis, lockKey, requestId, expireTime);
                }
                if (tryLock) {
                    Future submit = threadPool.submit(callable);
                    try {
                        result = submit.get();
                    } catch (Exception e) {
                        logger.error("{} 线程发生了异常 ： {}", threadName, e);
                    }
                }
            } finally {
                releaseDistributedLock(jedis, lockKey, requestId);
            }
            return result;
        }
    }


}
