package com.example.consumer.service;

import com.example.consumer.bean.UserNew;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * @author shenzm
 * @date 2019-4-15
 * @description 作用
 */


@Service
public class UserPersistService {

    private static final Logger logger = LoggerFactory.getLogger(UserPersistService.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void save(UserNew user){
        String sql = "insert into usernew(name,age) VALUES('"+user.getName()+"',"+user.getAge()+")";
        jdbcTemplate.execute(sql);
        logger.info(sql);
    }
}
