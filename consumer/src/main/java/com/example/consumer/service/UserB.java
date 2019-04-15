package com.example.consumer.service;

import com.example.consumer.bean.UserNew;

/**
 * @author shenzm
 * @date 2019-3-28
 * @description 作用
 */
public interface UserB {


    /**
     * Propagation.REQUIRES_NEW 测试
     * @param userNew
     */
    public void testRequiresNew(UserNew userNew);


    public void testNested(UserNew userNew);


}
