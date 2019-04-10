package com.example.api.list;

import java.util.Arrays;

/**
 * @author shenzm
 * @date 2019-4-3
 * @description 作用
 */
public class List<T> {

    private Object[] tab;

    private int threshold;

    private int size;

    private static int DEFAULT_SIZE = 12;

    public void add(T t) {
        //如果tab 没有初始化，初始化tab
        if (tab == null) {
            tab = new Object[DEFAULT_SIZE];
            threshold = DEFAULT_SIZE;
        }

        //如果size 达到了阈值，扩容数组
        if (size == threshold) {
            resize();
        }
        tab[size++] = t;
    }

    public void add(T t, int index) {
        if (index > size) {
            throw new RuntimeException("角标越界");
        }

        if (size + 1 == threshold) {
            resize();
        }
        threshold += 1;
        Object[] newTab = new Object[threshold];
        if (index == 0) {
            newTab[0] = t;
            System.arraycopy(tab, 0, newTab, 1, size);
        } else {
            System.arraycopy(tab, 0, newTab, 0, index);
            newTab[index] = t;
            System.arraycopy(tab, index, newTab, index + 1, size - index);
        }
        tab = newTab;
        size++;
    }


    /**
     * 获取所有的元素
     *
     * @return
     */
    public T[] getAll() {
        Object[] arr = new Object[size];
        for (int i = 0; i < size; i++) {
            arr[i] = tab[i];
        }
        return (T[]) arr;
    }

    private void resize() {
        threshold = new Double(threshold * 1.5).intValue();
        Object[] newTab = new Object[threshold];
        System.arraycopy(tab, 0, newTab, 0, size);
        tab = newTab;
        System.out.println("数组扩容 threshold " + threshold);

    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(tab[i]);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        List<Integer> list = new List();

        for (int i = 0; i < 3; i++) {
            list.add(i);
        }
        System.out.println(list);

        list.add(1000, 3);
        list.add(1000, 4);
        list.add(1000, 5);
        System.out.println(list);

        System.out.println(Arrays.toString(list.getAll()));
    }
}
