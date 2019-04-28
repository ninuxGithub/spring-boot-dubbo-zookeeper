package com.example.api.mode.observer;


/**
 * @author shenzm
 * @date 2019-4-28
 * @description 作用
 */
public class BinaryObserver extends Observer {

    public BinaryObserver(Subject subject) {
        this.subject = subject;
        this.subject.attach(this);

    }


    @Override
    public void update() {
        System.out.println(" binary string : " + Integer.toBinaryString(subject.getState()));
    }
}
