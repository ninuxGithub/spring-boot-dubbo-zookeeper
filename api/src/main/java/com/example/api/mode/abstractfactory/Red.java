package com.example.api.mode.abstractfactory;

/**
 * @author shenzm
 * @date 2019-4-25
 * @description 作用
 */
public class Red implements Color {
    @Override
    public void fill() {
        System.out.println("fill red color");
    }
}
