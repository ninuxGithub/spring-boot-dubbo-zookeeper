package com.example.api.mode.prototype;

/**
 * @author shenzm
 * @date 2019-4-25
 * @description 作用
 */
public class PrototypeDemo {
    public static void main(String[] args) {
        ShapeCache.loadCache();

        Shape circle = ShapeCache.getShape("1");
        System.out.println(circle.getType());
        System.out.println("PrototypeDemo color hash " + circle.hashCode());
        System.out.println("PrototypeDemo color type hash " + circle.type.hashCode());

        System.out.println(ShapeCache.getShape("2").getType());
        System.out.println(ShapeCache.getShape("3").getType());
    }
}
