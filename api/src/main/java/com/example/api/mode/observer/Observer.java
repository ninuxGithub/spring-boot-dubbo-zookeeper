package com.example.api.mode.observer;

/**
 * @author shenzm
 * @date 2019-4-28
 * @description 作用
 */
public abstract class Observer {

    protected Subject subject;

    public abstract void update();
}
