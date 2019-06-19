package com.example.api.guava;

import com.google.common.collect.ArrayListMultimap;

import java.util.Collection;

/**
 * @author shenzm
 * @date 2019-6-18
 * @description 作用
 */
public class MultiMap {

    public static void main(String[] args) {
        ArrayListMultimap<String,Integer> map = ArrayListMultimap.create();

        map.put("java",1);
        map.put("java",2);
        map.put("java",3);

        map.put("c++",4);
        map.put("c++",5);
        map.put("c++",6);

        map.put("php",7);
        map.put("php",8);
        map.put("php",9);


        //AbstractMapBaseedMultimap : 如果 Maps.safeGet(submap, key) 检查了submap引用； 如果引用为null抛异常
        System.out.println(map.asMap().get("delph"));

        //AbstractMapBaseedMultimap 里面的map直接获取对应的key
        System.out.println(map.get("delph"));


        System.out.println(map.get("java"));



    }

}
