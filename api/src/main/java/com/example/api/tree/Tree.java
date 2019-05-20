package com.example.api.tree;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * https://img-bbs.csdn.net/upload/201905/09/1557389576_325837.jpg
 *
 * @author shenzm
 * @date 2019-5-17
 * @description 作用
 */

public class Tree {

    static Node treeRoot;

    static Map<Integer,List<Node>> map = new HashMap<>();


    public static int fxy(Node x, Node y) {
        return Node.getSum(x) + Node.getSum(y);
    }

    public static void main(String[] args) {

        int deepth = 5;
        int level=1;
        buildTree(null, level, deepth);
        System.out.println(treeRoot);

        List<Map.Entry<Integer, List<Node>>> entryList = new ArrayList<>(map.entrySet());
        for (int i=0; i<entryList.size(); i++){
            List<Node> nodes = entryList.get(i).getValue();
            for (int j=0; j<nodes.size(); j++){
                System.out.print(getSmbos("**", deepth - i) +nodes.get(j).getValue());
            }
            System.out.println("");

        }
        List<Node> nodeList = entryList.get(deepth - 1).getValue();
        /*for (int i=0; i<nodeList.size(); i++){
            System.out.println("第"+deepth+"排的第"+(i+1)+"列的元素到root的和为："+Node.getSum(nodeList.get(i)));
        }*/

        List<Node> max2List = nodeList.stream().sorted(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return Node.getSum(o1) - Node.getSum(o2);
            }
        }.reversed()).limit(2).collect(Collectors.toList());


        for (int i=0; i<nodeList.size(); i++){
            if (nodeList.get(i) == max2List.get(0) || nodeList.get(i) ==max2List.get(1)){
                System.out.println("第"+deepth+"排的第"+(i+1)+"列的元素到root的和为："+Node.getSum(nodeList.get(i)));
            }

        }

    }


    /**
     * @param root root 节点
     * @param level 深度从第几层开始
     * @param deepth 深度
     */
    public static void buildTree(Node root, int level, int deepth) {
        if(level ==1 && root == null){
            root = createLeafNodes(null);
            root.setDeepth(deepth);
            treeRoot = root;
            initMap(root, level);
        }
        level++;
        if (level > deepth) {
            return;
        }
        root.setLeftLeaf(createLeafNodes(root));
        initMap(root.getLeftLeaf(), level);
        buildTree(root.getLeftLeaf(), level, deepth);

        root.setRightLeaf(createLeafNodes(root));
        initMap(root.getRightLeaf(), level);
        buildTree(root.getRightLeaf(), level, deepth);
    }

    private static void initMap(Node root, int level) {
        List<Node> list = map.get(level);
        if (null == list){
            list = new ArrayList<>();
        }
        list.add(root);
        map.put(level, list);
    }

    private static String getSmbos(String smbo , int deepth){
        String s = "";
        for (int i=0; i<deepth; i++){
            s += smbo;
        }
        return s;
    }


    public static Node createLeafNodes(Node parent) {
        return new Node(getRandomNum(), parent);
    }


    private static int getRandomNum() {
        Random random = new Random();
        int num = random.nextInt(20);
        return num;
    }


    static class Node {
        int value;

        int deepth;

        Node leftLeaf;

        Node rightLeaf;

        Node parent;

        public Node(int value, Node parent) {
            this.value = value;
            this.parent = parent;
        }

        public Node(int value, Node leftLeaf, Node rightLeaf, Node parent) {
            this.value = value;
            this.leftLeaf = leftLeaf;
            this.rightLeaf = rightLeaf;
            this.parent = parent;
        }

        public static int getSum(Node node){
            int sum = node.getValue();
            Node p = node.getParent();
            while (p !=null){
                sum += p.getValue();
                p = p.getParent();
            }

            return sum;
        }
        public int getDeepth() {
            return deepth;
        }

        public void setDeepth(int deepth) {
            this.deepth = deepth;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public Node getLeftLeaf() {
            return leftLeaf;
        }

        public void setLeftLeaf(Node leftLeaf) {
            this.leftLeaf = leftLeaf;
        }

        public Node getRightLeaf() {
            return rightLeaf;
        }

        public void setRightLeaf(Node rightLeaf) {
            this.rightLeaf = rightLeaf;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "value=" + value +
                    ", parent=" + (parent == null ? "root" : parent.getValue()) +
                    '}';
        }
    }
}
