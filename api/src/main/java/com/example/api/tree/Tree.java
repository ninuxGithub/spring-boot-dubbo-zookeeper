package com.example.api.tree;

import java.io.File;
import java.util.Random;

/**
 * https://img-bbs.csdn.net/upload/201905/09/1557389576_325837.jpg
 *
 * @author shenzm
 * @date 2019-5-17
 * @description 作用
 */
public class Tree {

    static Node root = null;

    public int fxy(Node x, Node y) {
        return 0;
    }

    public static void main(String[] args) {

//        File rf = new File("D:\\mobileDSNew");
//        new Tree().listFile(rf, 0);

        new Tree().buildTree(root, 0, 3);


    }

    /**
     * 递归文件夹
     *
     * @param rf
     * @param level
     */
    public void listFile(File rf, int level) {

        for (File file : rf.listFiles()) {
            if (file.isDirectory()) {
                System.out.println("\t" + getSplitStr(level) + file.getName());
                level++;
                listFile(file, level);
            } else {
                System.out.println("\t" + getSplitStr(level) + file.getName());
            }

        }
    }

    private String getSplitStr(int h) {
        String str = "";
        for (int i = 0; i < h; i++) {
            str += "---|";
        }
        return str;
    }

    public void buildTree(Node root, int level, int deepth) {
        System.out.println("-->build");
        if (root == null) {
            root = createRootNodes();
            System.out.println(root);
            buildTree(root, level, deepth);
        } else {
            level++;
            if (level >= deepth) {
                return;
            }
            System.out.println(root);
            root.setRightLeaf(createLeafNodes());
            buildTree(root.getLeftLeaf(), level, deepth);

            root.setRightLeaf(createLeafNodes());
            buildTree(root.getRightLeaf(), level, deepth);
        }

    }

    public Node createRootNodes() {
        return new Node(getRandomNum(), new Node(getRandomNum()), new Node(getRandomNum()));
    }

    public Node createLeafNodes() {
        return new Node(getRandomNum());
    }


    private int getRandomNum() {
        Random random = new Random();
        int num = random.nextInt(1000);
        return num;
    }


    class Node {
        int value;

        Node leftLeaf;

        Node rightLeaf;

        public Node(int value) {
            this.value = value;
        }

        public Node(int value, Node leftLeaf, Node rightLeaf) {
            this.value = value;
            this.leftLeaf = leftLeaf;
            this.rightLeaf = rightLeaf;
        }

        /*public Node[] getChildNodes() {
            return new Node[]{leftLeaf, rightLeaf};
        }*/


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

        @Override
        public String toString() {
            return "Node{" +
                    "value=" + value +
                    ", leftLeaf=" + leftLeaf +
                    ", rightLeaf=" + rightLeaf +
                    '}';
        }
    }
}
