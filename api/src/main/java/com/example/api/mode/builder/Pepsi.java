package com.example.api.mode.builder;

/**
 * @author shenzm
 * @date 2019-4-25
 * @description 作用
 */
public class Pepsi extends ColdDrink {
    @Override
    public String name() {
        return "Pepsi";
    }

    @Override
    public float price() {
        return 9.5f;
    }
}
