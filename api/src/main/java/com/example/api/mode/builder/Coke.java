package com.example.api.mode.builder;

/**
 * @author shenzm
 * @date 2019-4-25
 * @description 作用
 */
public class Coke extends ColdDrink {
    @Override
    public String name() {
        return "Coke";
    }

    @Override
    public float price() {
        return 20.5f;
    }
}
