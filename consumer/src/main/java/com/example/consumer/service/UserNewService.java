package com.example.consumer.service;

import com.example.consumer.bean.UserNew;
import org.apache.ibatis.annotations.Param;

/**
 * @author shenzm
 * @date 2019-3-28
 * @description 作用
 */
public interface UserNewService {

    public void saveUser(UserNew userNew);


    public void saveUser2(UserNew userNew);

    public UserNew queryByUserId(Long id);

    public void updateUser(int age, String name, Long id);

    public void deleteAllInBatch();


}
