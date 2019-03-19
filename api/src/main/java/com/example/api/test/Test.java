package com.example.api.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shenzm
 * @date 2019-3-7
 * @description 作用
 */
public class Test {

    public static void main(String[] args) {
        String s = (String) null;
        System.out.println(s);


        long start0 = System.nanoTime();
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < 100_0000; i++) {
            map.put(i, i);
        }
        long start1 = System.nanoTime();


        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100_0000; i++) {
            list.add(i);
        }
        long start2 = System.nanoTime();


        Integer integer = map.get(8888);

        long start3 = System.nanoTime();

        list.get(8888);

        long start4 = System.nanoTime();

        System.out.println("map add 消耗的时间:" + (start1 - start0)/1000);
        System.out.println("list add 消耗的时间： " + (start2 - start1)/1000);

        System.out.println("map get 消耗的时间:" + (start3 - start2));
        System.out.println("list get 消耗的时间： " + (start4 - start3));
    }

}
