package com.example.api.test;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LinkedHashMap 实现lru算法
 * accessOrder 元素访问的时候 改变链表里面元素的属性 将当前访问的元素放到tail  查看afterNodeAccess
 * 在LinkedHashMap调用get方法的时候， 会判断accessOrder是否为true 如果为true 那么调用afterNodeAccess
 * 改变当前元素的位置：-----> javadoc : move node to last 将Entry放到链表的最后去（链表的靠前的位置是旧的entry, 靠后的时候新增的）
 * 所以当调用access访问元素的时候是改变了元素的位置， 放到最新的位置， 就是设置元素为更“新” ；
 * put->putVal 最后会调用afterNodeInsertion -->removeEldestEntry(first) 可能会删除first 返回boolean
 * 如果返回true 调用removeNode进行节点的删除
 *
 * @author shenzm
 * @date 2019-5-15
 * @description 作用
 */
public class LRUHashMap<K, V> extends LinkedHashMap<K, V> implements Map<K, V> {

    private int maxCount;

    public LRUHashMap(int initCapacity, float loadFactor, boolean accessOrder, int maxCount) {
        super(initCapacity, loadFactor, accessOrder);
        this.maxCount = maxCount;
    }

    @Override
    protected boolean removeEldestEntry(Entry<K, V> eldest) {
        if (size() > maxCount) {
            return true;
        }
        return false;
    }

    /**
     * map里面的最大的容量为 20 如果超过20个的时候  删除最近最少使用的那个元素
     * @param args
     */
    public static void main(String[] args) {
        Map<Integer, String> map = new LRUHashMap(16, 0.75f, true, 20);
        for (int i = 10; i < 50; i++) {
            map.put(i, "java" + i);

            //如果加入这样代码  ， 使用一下这个元素 ，那么就不会被淘汰 否则  旧的值被淘汰
            //map.get(5);
            System.out.println(map);
        }

        System.out.println(map.size());
        System.out.println(map.get(5));
        System.out.println(map.get(30));//将该元素调整到最后去
        System.out.println(map);

    }
}
