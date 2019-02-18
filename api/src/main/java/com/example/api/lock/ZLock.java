package com.example.api.lock;

import java.util.concurrent.TimeUnit;

/**
 * @author shenzm
 * @date 2019-2-15
 * @description 作用
 */
public interface ZLock {


    void getLock() throws Exception;

    boolean getLock(long timeOut, TimeUnit unit) throws Exception;

    void releaseLock() throws Exception;


}
