package com.example.api.mode.builder;

/**
 * @author shenzm
 * @date 2019-4-25
 * @description 作用
 */
public class VegBurger extends Burger {
    @Override
    public String name() {
        return "vegBurger";
    }

    @Override
    public float price() {
        return 25.0f;
    }
}
