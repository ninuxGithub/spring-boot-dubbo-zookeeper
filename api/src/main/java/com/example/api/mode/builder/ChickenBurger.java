package com.example.api.mode.builder;

/**
 * @author shenzm
 * @date 2019-4-25
 * @description 作用
 */
public class ChickenBurger extends Burger {
    @Override
    public String name() {
        return "ChickenBurger";
    }

    @Override
    public float price() {
        return 23.8f;
    }
}
