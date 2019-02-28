package com.example.provider.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;
import java.util.Set;

/**
 * @author shenzm
 * @date 2019-2-28
 * @description 作用
 */

@Document(indexName = "departments", type = "department", shards = 1, replicas = 0, refreshInterval = "-1")
public class Department implements Serializable {

    @Id
    private String id;

    @Field
    private String departName;

    @Field
    private Set<Employee> employees;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }
}
