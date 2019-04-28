package com.example.api.mode.abstractfactory;

/**
 * @author shenzm
 * @date 2019-4-25
 * @description 作用
 */
public abstract class AbstractFactory {

    public abstract Color getColor(String color);

    public abstract Shape getShape(String shape);


}
