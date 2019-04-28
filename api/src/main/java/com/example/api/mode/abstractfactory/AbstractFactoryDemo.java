package com.example.api.mode.abstractfactory;

/**
 * 通过工厂创建不同的java对象
 *
 * @author shenzm
 * @date 2019-4-25
 * @description 作用
 */
public class AbstractFactoryDemo {

    public static void main(String[] args) {

        AbstractFactory shape = FactoryProducer.getFactory("shape");

        Shape circle = shape.getShape(Shape.CIRCLE);
        circle.draw();


        Shape rectangle = shape.getShape(Shape.RECTANGLE);
        rectangle.draw();

        Shape square = shape.getShape(Shape.SQUARE);
        square.draw();


        AbstractFactory color = FactoryProducer.getFactory("color");
        Color blue = color.getColor(Color.BLUE);
        blue.fill();


        Color green = color.getColor(Color.GREEN);
        green.fill();


        color.getColor(Color.RED).fill();
    }
}
