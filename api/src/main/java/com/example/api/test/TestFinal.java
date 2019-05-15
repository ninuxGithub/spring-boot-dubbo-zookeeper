package com.example.api.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author shenzm
 * @date 2019-5-15
 * @description 作用
 */
public class TestFinal {

    private static final int i;

    private static final String str;


    private final List<Integer> list;

    final int k = 1;

    static {
        i = 100;
        str = "你好";
        System.out.println("static");


    }

    {
        System.out.println("构造代码块");
        list = new ArrayList<>();
    }

    public TestFinal(int pi, String ps) {
        /*i = pi;
        str = ps;*/
        System.out.println("constractor");
    }


    public int getI() {
        return i;
    }

    public String getStr() {
        return str;
    }

    public void setI(int i) {

    }


    public static void main(String[] args) {
        TestFinal t = new TestFinal(1, "java");
        System.out.println(t.getI());
        System.out.println(t.getStr());

        final int j = 0;
//        j = 10;

        final Integer max = 10;
//        max = 1000;


        //说明数组改变了角标的值后  对象没有发生改变
        final int[] arr = {0, 1, 2};
        // hashcode 1023892928
        System.out.println(arr.hashCode());
        arr[0] = 100;
        // arr = {};
        System.out.println(Arrays.toString(arr));
        // hashcode 1023892928
        System.out.println(arr.hashCode());

        t.addElement(100);

        System.out.println(t.k);
        System.out.println(i);

        while (true){
            byte[] bytes = new byte[1024 * 1024 * 10];
            //System.out.println("程序运行zhong...");
            try {
                TimeUnit.MILLISECONDS.sleep(100);
                //bytes = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void addElement(Integer value) {
        list.add(value);
        System.out.println(list);
    }
}
