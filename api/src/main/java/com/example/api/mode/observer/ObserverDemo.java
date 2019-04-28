package com.example.api.mode.observer;

/**
 * @author shenzm
 * @date 2019-4-28
 * @description 作用
 */
public class ObserverDemo {

    public static void main(String[] args) {
        Subject subject = new Subject();

        new HexaObserver(subject);
        new OctalObserver(subject);
        new BinaryObserver(subject);

        subject.setState(15);
        subject.setState(10);
    }
}
