package com.example.api.mode.prototype;

import java.io.Serializable;

/**
 * @author shenzm
 * @date 2019-4-25
 * @description 作用
 */
public class Square extends Shape implements Serializable {

    public Square() {
        type = "Square";
    }

    @Override
    void draw() {
        System.out.println("Square");
    }
}
