package com.example.provider.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;



/**
 * @author shenzm
 */
@Document(indexName = "company", type = "employee", shards = 1, replicas = 0, refreshInterval = "-1")
public class Employee implements Serializable {
    @Id
    private String id;
    @Field
    private String firstName;
    @Field
    private String lastName;
    @Field
    private Integer age = 0;
    @Field
    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
