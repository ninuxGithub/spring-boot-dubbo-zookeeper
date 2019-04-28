package com.example.api.mode.abstractfactory;

/**
 * @author shenzm
 * @date 2019-4-25
 * @description 作用
 */
public class Rectangle implements Shape {
    @Override
    public void draw() {
        System.out.println("Rectangle");
    }
}
