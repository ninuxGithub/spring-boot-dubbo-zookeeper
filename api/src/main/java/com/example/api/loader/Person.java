package com.example.api.loader;

/**
 * @author shenzm
 * @date 2019-6-4
 * @description 作用
 */
public class Person {

    private int age;
    private String address;
    private String name;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
