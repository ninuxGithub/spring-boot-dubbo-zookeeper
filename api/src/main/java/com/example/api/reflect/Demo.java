package com.example.api.reflect;

/**
 * @author shenzm
 * @date 2019-5-10
 * @description 作用
 */


public class Demo {


    @AutoField(value = "name")
    private String name;

    @AutoField(value = "age")
    private int age;

    @AutoField(value = "salary")
    private float salary;

    @AutoMethod(methodName = "getName")
    public String getName() {
        return name;
    }

    @AutoMethod(methodName = "setName")
    public void setName(String name) {
        this.name = name;
    }

    @AutoMethod(methodName = "getAge")
    public int getAge() {
        return age;
    }

    @AutoMethod(methodName = "setAge")
    public void setAge(int age) {
        this.age = age;
    }

    @AutoMethod(methodName = "getSalary")
    public float getSalary() {
        return salary;
    }

    @AutoMethod(methodName = "setSalary")
    public void setSalary(float salary) {
        this.salary = salary;
    }
}
