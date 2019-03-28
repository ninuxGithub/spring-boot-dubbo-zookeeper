package com.example.consumer.service;

import com.example.consumer.bean.UserNew;
import com.example.consumer.repository.UserNewRepository;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * @author shenzm
 * @date 2019-3-28
 * @description 作用
 */

@Service
public class UserNewServiceImpl implements UserNewService {

    @Autowired
    private UserNewRepository userNewRepository;

    @Transactional(
            value = "mysqlTransactionManager",
            rollbackFor = {Exception.class, RuntimeException.class},
            propagation = Propagation.REQUIRED,
            isolation = Isolation.DEFAULT
    )
    @Override
    public void saveUser(UserNew userNew) {
        //org.springframework.data.jpa.repository.support.CrudMethodMetadataPostProcessor.CrudMethodMetadataPopulatingMethodInterceptor.invoke()
        //ReflectiveMethodInvocation.proceed()
        //TransactionInterceptor.invoke()
        //org.springframework.transaction.interceptor.TransactionAspectSupport
        userNewRepository.save(userNew);
        int a = 1, b = 0;
        System.out.println(a / b);
    }
}
