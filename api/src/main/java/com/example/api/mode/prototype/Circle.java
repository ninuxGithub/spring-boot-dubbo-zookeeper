package com.example.api.mode.prototype;

import java.io.Serializable;

/**
 * @author shenzm
 * @date 2019-4-25
 * @description 作用
 */
public class Circle extends Shape implements Serializable{

    public Circle() {
        type = "Circle";
    }

    @Override
    void draw() {
        System.out.println("Circle");
    }
}
