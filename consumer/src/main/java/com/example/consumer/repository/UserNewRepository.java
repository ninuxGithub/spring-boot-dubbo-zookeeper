package com.example.consumer.repository;

import com.example.consumer.bean.UserNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//@Transactional(value = "mysqlTransactionManager", rollbackFor = {Exception.class})
public interface UserNewRepository extends JpaRepository<UserNew, Long> {

    /**
     * 根据名称获取用户
     *
     * @param name
     * @return
     */
    public UserNew findUserNewByName(String name);

}
