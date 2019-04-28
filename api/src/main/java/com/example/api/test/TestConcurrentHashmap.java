package com.example.api.test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * byte short int long  boolean char float double
 * byte short int long boolean char float double
 * 1 2 4 8 1 2 4 8
 * 8 16 32 64 8 16 32 64
 * @author shenzm
 * @date 2019-4-16
 * @description 作用
 */
public class TestConcurrentHashmap {

    public static void main(String[] args) {
        ConcurrentHashMap<Node,Integer> map = new ConcurrentHashMap<>();

        List<String> list = Arrays.asList("语文", "数学", "英语", "历史", "政治", "地理", "生物", "化学");
        for (int i=0; i<list.size(); i++){
            Node node = new Node(list.get(i), i);
            map.put(node, i);
        }
        int size = list.size();
        for (int i = 0; i < 2000; i++) {
            int index = i % size;
            Node node = new Node(list.get(index) + i, i);
            map.put(node, i);
        }

        map.put(new Node("null", 0),0);
        System.out.println(map);
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
            int result = key.hashCode();
            result = 31 * result + value.hashCode();
            System.out.println(result);
            return result;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "key='" + key + '\'' +
                    ", value=" + value +
                    '}';
        }
    }
}
