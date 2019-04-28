package com.example.api.mode.abstractfactory;

/**
 * @author shenzm
 * @date 2019-4-25
 * @description 作用
 */
public class FactoryProducer {

    public static AbstractFactory getFactory(String factoryType) {
        if (factoryType.equals("shape")) {
            return new ShapeFactory();
        } else if (factoryType.equals("color")) {
            return new ColorFactory();
        }
        return null;
    }
}
