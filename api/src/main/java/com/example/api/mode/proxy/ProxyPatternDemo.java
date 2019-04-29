package com.example.api.mode.proxy;

/**
 * @author shenzm
 * @date 2019-4-29
 * @description 作用
 */
public class ProxyPatternDemo {
    public static void main(String[] args) {
        Image image = new ProxyImage("a.jpg");

        image.display();
        image.display();
    }
}
