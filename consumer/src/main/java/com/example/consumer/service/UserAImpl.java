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
//        userB.testRequiresNew(user);
        userB.testNested(user);
        throw new RuntimeException("service A 异常");
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
