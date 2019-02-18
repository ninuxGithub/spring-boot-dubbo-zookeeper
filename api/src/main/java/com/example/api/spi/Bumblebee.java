package com.example.api.spi;

/**
 * @author shenzm
 * @date 2019-2-15
 * @description 作用
 */
public class Bumblebee implements Robot {
    @Override
    public void sayHello() {
        System.out.println("Hello, I am Bumblebee.");
    }
}
