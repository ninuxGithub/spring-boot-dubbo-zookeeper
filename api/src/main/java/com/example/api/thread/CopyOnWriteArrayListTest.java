package com.example.api.thread;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author shenzm
 * @date 2019-5-13
 * @description 作用
 */
public class CopyOnWriteArrayListTest {

    public static void main(String[] args) {
        CopyOnWriteArrayList list = new CopyOnWriteArrayList();
        list.add(1);
        System.out.println(list.get(0));

        Iterator iterator = list.iterator();
        while (iterator.hasNext()){
            Object next = iterator.next();
            System.out.println(next);
        }


        List subList = list.subList(0, 1);
        System.out.println(subList);
        System.out.println(subList.getClass().getName());
    }
}
