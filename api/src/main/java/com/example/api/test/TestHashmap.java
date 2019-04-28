package com.example.api.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author shenzm
 * @date 2019-4-15
 * @description 作用
 */
public class TestHashmap {

    public static void main(String[] args) {
        HashMap<Node, Integer> map = new HashMap<Node, Integer>();
        List<String> list = Arrays.asList("语文", "数学", "英语", "历史", "政治", "地理", "生物", "化学");
        int size = list.size();

        for (int i = 0; i < 200; i++) {
            if (i ==11){
                System.out.println("i");
            }
            int index = i % size;
            Node node = new Node(list.get(index) + i, i);
            map.put(node, i);
        }

        /*map.put("语文", 1);
        map.put("数学", 2);
        map.put("英语", 3);
        map.put("历史", 4);
        map.put("政治", 5);
        map.put("地理", 6);
        map.put("生物", 7);
        map.put("化学", 8);*/

        //http://www.cnblogs.com/finite/p/8251587.html
        System.out.println(map);

        System.out.println(Integer.toBinaryString(100));
        //return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
        int a = 100 >>> 16;
        System.out.println(a);
        System.out.println(100 ^ a);
    }


    static class Node {
        private String key;
        private Integer value;

        public Node(String key, Integer value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Node node = (Node) o;

            if (!key.equals(node.key)) {
                return false;
            }
            return value.equals(node.value);
        }

        @Override
        public int hashCode() {
            /*int result = key.hashCode();
            result = 31 * result + value.hashCode();
            return result;*/
            return 100;
        }
    }
}
