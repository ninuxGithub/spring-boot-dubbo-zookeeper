package com.example.api.mode.abstractfactory;

/**
 * @author shenzm
 * @date 2019-4-25
 * @description 作用
 */
public class ColorFactory extends AbstractFactory {
    @Override
    public Color getColor(String color) {
        if (null == color) {
            return null;
        }
        if (color.equals(Color.RED)) {
            return new Red();
        } else if (color.equals(Color.BLUE)) {
            return new Blue();
        } else if (color.equals(Color.GREEN)) {
            return new Green();
        }
        return null;
    }

    @Override
    public Shape getShape(String shape) {
        return null;
    }
}
