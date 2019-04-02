package com.example.api.koloboke;




import net.openhft.koloboke.collect.map.hash.HashIntIntMaps;
import net.openhft.koloboke.collect.map.hash.HashObjIntMaps;

import java.util.Map;

/**
 * @author shenzm
 * @date 2019-4-2
 * @description 作用
 */
public class Test {

    public static void main(String[] args) {
        Map<Integer,Integer> map = HashIntIntMaps.newMutableMap();
        map.put(1,1);
        map.put(2,2);
        for (Integer key : map.keySet()){
            System.out.println(key + " ---> "+ map.get(key));
        }
        Map<String, Integer> smap = HashObjIntMaps.newMutableMap();
        smap.put("java", 1);
        smap.put("delph", 2);


        for (String key : smap.keySet()) {
            System.out.println(key + "  " + smap.get(key));
        }
    }
}
