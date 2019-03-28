package com.example.consumer.service;

import com.example.consumer.bean.UserNew;
import com.example.consumer.repository.UserNewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shenzm
 * @date 2019-3-28
 * @description 作用
 */

@Service
public class UserNewServiceImpl implements UserNewService {

    @Autowired
    private UserNewRepository userNewRepository;

    //           tx          抛异常    保存情况
    //  save     REQUIRED      no          no
    //  save2    REQUIRES_NEW  yes         no

    //  save     REQUIRED      yes         no
    //  save2    REQUIRES_NEW  no          no


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

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        UserNew user = new UserNew();
        user.setName("22222");
        user.setAge(222);
        saveUser2(user);
        throw new RuntimeException("异常发生了");

    }

    @Transactional(
            value = "mysqlTransactionManager",
            rollbackFor = {Exception.class, RuntimeException.class},
            propagation = Propagation.REQUIRES_NEW,
            isolation = Isolation.DEFAULT
    )
    @Override
    public void saveUser2(UserNew user) {
        userNewRepository.save(user);
    }
}
