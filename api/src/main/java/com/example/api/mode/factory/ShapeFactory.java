package com.example.api.mode.factory;

/**
 * @author shenzm
 * @date 2019-4-25
 * @description 作用
 */
public class ShapeFactory {

    public Shape getShape(String shapeType) {
        if (shapeType == null) {
            return null;
        }
        if (shapeType.equals(Shape.CIRCLE)) {
            return new Circle();
        } else if (shapeType.equals(Shape.RECTANGLE)) {
            return new Rectangle();
        } else if (shapeType.equals(Shape.SQUARE)) {
            return new Square();
        }
        return null;
    }
}
