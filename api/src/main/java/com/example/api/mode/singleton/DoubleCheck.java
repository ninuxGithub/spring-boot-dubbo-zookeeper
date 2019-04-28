package com.example.api.mode.singleton;

/**
 * @author shenzm
 * @date 2019-4-25
 * @description 作用
 */
public class DoubleCheck {

    private static volatile DoubleCheck singleton;


    private DoubleCheck() {
    }


    public static DoubleCheck getSingleton() {
        if (singleton ==null){
            synchronized (DoubleCheck.class) {
                if (singleton == null) {
                    singleton = new DoubleCheck();
                }
            }
        }
        return singleton;
    }


    public static void main(String[] args) {
        for(int i=0; i<100; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(DoubleCheck.getSingleton().hashCode());
                }
            }).start();
        }
    }

}
