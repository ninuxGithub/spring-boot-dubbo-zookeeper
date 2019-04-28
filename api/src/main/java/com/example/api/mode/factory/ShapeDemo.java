package com.example.api.mode.factory;

/**
 * 通过工厂创建不同的java对象
 *
 * @author shenzm
 * @date 2019-4-25
 * @description 作用
 */
public class ShapeDemo {

    public static void main(String[] args) {
        ShapeFactory shapeFactory = new ShapeFactory();
        Shape circle = shapeFactory.getShape(Shape.CIRCLE);
        circle.draw();


        Shape rectangle = shapeFactory.getShape(Shape.RECTANGLE);
        rectangle.draw();

        Shape square = shapeFactory.getShape(Shape.SQUARE);
        square.draw();
    }
}
