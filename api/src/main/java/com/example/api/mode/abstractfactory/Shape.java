package com.example.api.mode.abstractfactory;

/**
 * @author shenzm
 * @date 2019-4-25
 * @description 作用
 */
public interface Shape {
    String CIRCLE = "circle";

    String RECTANGLE = "rectangle";

    String SQUARE = "square";

    void draw();
}
