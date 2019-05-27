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
public class UserAImpl implements UserA {


    @Autowired
    private UserB userB;

    @Autowired
    UserPersistService userPersistService;


    @Autowired
    private UserNewRepository userNewRepository;

    //           tx          抛异常    保存情况
    //  save     REQUIRED      no          yes
    //  save2    REQUIRES_NEW  yes         no

    //  save     REQUIRED      yes         no
    //  save2    REQUIRES_NEW  no          no


    //org.springframework.data.jpa.repository.support.CrudMethodMetadataPostProcessor.CrudMethodMetadataPopulatingMethodInterceptor.invoke()
    //ReflectiveMethodInvocation.proceed()
    //TransactionInterceptor.invoke()
    //org.springframework.transaction.interceptor.TransactionAspectSupport


    @Transactional(
            value = "dataSourceTransactionManager",
            rollbackFor = {Exception.class, RuntimeException.class},
            propagation = Propagation.REQUIRED
    )
    @Override
    public void saveUser(UserNew userNew) {
        userPersistService.save(userNew);

        UserNew user = new UserNew();
        user.setName("bbbbbbbbbb");
        user.setAge(222);

        //requires_new 如果内部的事物抛出了异常不try那么都会回滚
        //如果在调用的地方进行了try 那么内部的事物异常后回滚 外部的事物正常提交
        try {
            userB.testRequiresNew(user);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //如果外部抛出了异常 不会对内部的事物造成影响
        //throw new RuntimeException("service A 异常");


//        2.如果采用try nested 那么嵌套事物有异常被捕捉了 内部事物提交失败 导致 roll back to savepoint
//        try {
//            userB.testNested(user);//内部抛出了异常
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        3.如果不采取try语句， 那么抛出的异常会被外层事物捕捉到 导致外层的事物也回滚
//        userB.testNested(user);//内部抛出了异常
//        4.如果外层直接抛出异常，那么所有的提交都会进行回滚
//        throw new RuntimeException("service A 异常");
    }


    @Override
    public UserNew queryByUserId(Long id) {
        return userNewRepository.findUserNewById(id);
    }

    @Override
    @Transactional(
            value = "dataSourceTransactionManager",
            rollbackFor = {Exception.class, RuntimeException.class},
            propagation = Propagation.REQUIRES_NEW,
            isolation = Isolation.DEFAULT
    )
    public void updateUser(int age, String name, Long id) {
        userNewRepository.updateUser(age, name, id);
    }

    @Override
    public void deleteAllInBatch() {
        userNewRepository.deleteAllInBatch();
    }
}
