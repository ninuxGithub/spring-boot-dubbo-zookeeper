package com.example.api.mode.observer;

/**
 * @author shenzm
 * @date 2019-4-28
 * @description 作用
 */
public class OctalObserver extends Observer {

    public OctalObserver(Subject subject) {
        this.subject = subject;
        this.subject.attach(this);
    }

    @Override
    public void update() {

        System.out.println("Octal string :" + Integer.toOctalString(subject.getState()));
    }
}
