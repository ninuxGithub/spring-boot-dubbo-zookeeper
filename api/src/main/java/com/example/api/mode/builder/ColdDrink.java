package com.example.api.mode.builder;

/**
 * @author shenzm
 * @date 2019-4-25
 * @description 作用
 */
public abstract class ColdDrink implements Item {

    @Override
    public Packing packing() {
        return new Bottle();
    }

    @Override
    public abstract float price();
}
