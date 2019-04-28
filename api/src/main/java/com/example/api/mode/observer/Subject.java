package com.example.api.mode.observer;


import java.util.ArrayList;
import java.util.List;

/**
 * @author shenzm
 * @date 2019-4-28
 * @description 作用
 */
public class Subject {

    private List<Observer> observerList = new ArrayList<Observer>();

    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        notifyAllObservers();
    }

    public void attach(Observer observer){
        observerList.add(observer);
    }

    public void notifyAllObservers(){
        for (Observer observer : observerList){
            observer.update();
        }
    }
}
