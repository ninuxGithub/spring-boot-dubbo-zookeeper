package com.example.consumer.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author shenzm
 * @date 2019-2-22
 * @description 作用
 */
public class Person implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(Person.class);

    private String name;


    private int age;


    public Person() {
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void init(){
        logger.info("====person init run....");
    }

    public void destory(){
        logger.info("====person destory run....");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
