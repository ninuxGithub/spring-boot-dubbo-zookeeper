package com.example.consumer.service;

import com.example.consumer.bean.UserNew;
import com.example.consumer.repository.UserNewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shenzm
 * @date 2019-3-28
 * @description 作用
 */

@Service
public class UserBImpl implements UserB {


    @Autowired
    private UserNewRepository userNewRepository;

    @Autowired
    UserPersistService userPersistService;

    @Transactional(
            value = "dataSourceTransactionManager",
            rollbackFor = {Exception.class, RuntimeException.class},
            propagation = Propagation.REQUIRES_NEW
    )
    @Override
    public void testRequiresNew(UserNew user) {
        userPersistService.save(user);
        throw new RuntimeException("service B exception");
    }

    @Transactional(
            value = "dataSourceTransactionManager",
            rollbackFor = {Exception.class, RuntimeException.class},
            propagation = Propagation.NESTED
    )
    @Override
    public void testNested(UserNew user) {
        userPersistService.save(user);
        throw new RuntimeException("service B exception");
    }

}
