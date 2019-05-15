package com.example.api.test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LinkedHashMap 保证顺序的原因
 * 继承了HashMap ； 添加元素的时候调用put ->putVal 如果发现tab[x] 位置没有插入元素
 * 则创建一个新的节点插入到tab , 与此同时， linkedHashmap 创建节点的方法为LinkedHashMap.newNode
 * 该方法会将新创建的节点添加到LinkedHashMap维护的tail节点后面
 * <p>
 * 如果tab[x] 位置是一个tree 那么就会调用  LinkedHashMap.newTreeNode , 将创建的节点添加到tail后面
 * <p>
 * 总结： LinkedHashMap 内部元素的添加除了会添加到父类的tab数组中， 还会在自己的内部维护一个链表
 * head  , tail 会形成一个有序的链表 ， 元素在添加的时候会创建节点添加到链表的尾部
 * <p>
 * <p>
 * 对比就可知  LinkedHashMap 比 HashMap 多一个 链表 这就是LinkedHashMap 有序的原因
 *
 * @author shenzm
 * @date 2019-5-15
 * @description 作用
 */
public class TestLinkedHashMap {

    public static void main(String[] args) {
        LinkedHashMap<Integer, String> map = new LinkedHashMap<>();
        map.put(1, "java1");
        map.put(2, "java2");
        map.put(3, "java3");
        map.put(4, "java4");


        HashMap<Integer, String> hashmap = new HashMap<>();
        hashmap.put(1, "java1");
        hashmap.put(2, "java2");
        hashmap.put(3, "java3");
        hashmap.put(4, "java4");


        System.out.println(hashmap);
        System.out.println(map);


        for (Map.Entry entry : hashmap.entrySet()) {
            System.out.println(entry.getKey() + "  " + entry.getValue());
        }
        for (Map.Entry entry : map.entrySet()) {
            System.out.println(entry.getKey() + "  " + entry.getValue());
        }


    }
}
