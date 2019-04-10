package com.example.api.list;

/**
 * @author shenzm
 * @date 2019-4-2
 * @description 作用
 */
public class LinkedList<T> {

    /**
     * 头结点
     */
    private Node head;

    /**
     * 尾节点
     */
    private Node tail;

    /**
     * 保存的节点的数量
     */
    private int size;


    /**
     * 内部类声明一个节点对象
     */
    class Node {
        //当前节点的前一个节点
        private Node prev;

        //保存的值
        private T t;

        //后一个节点
        private Node next;

        public Node getPrev() {
            return prev;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }

        public T getT() {
            return t;
        }

        public void setT(T t) {
            this.t = t;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
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

            if (null != prev && !prev.equals(node.prev)) {
                return false;
            }
            if (null != t && !t.equals(node.t)) {
                return false;
            }
            return next.equals(node.next);
        }

        @Override
        public int hashCode() {
            int result = prev.hashCode();
            result = 31 * result + t.hashCode();
            result = 31 * result + next.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "prev=" + prev +
                    ", t=" + t +
                    ", next=" + next +
                    '}';
        }
    }

    public void setHead(Node head) {
        this.head = head;
    }

    public void setTail(Node tail) {
        this.tail = tail;
    }

    public int getSize() {
        return size;
    }

    /**
     * 首先判断插入的角标是否越界
     * 如果头为空 插入到头的节点
     *
     * 如果头结点不为空
     *         index 为0 插入头部 更新head
     *         index >0  && index <size  获取插入的节点
     *         index =size  直接插入到尾部  更新tail
     *
     * @param t
     * @param index
     */
    public void add(T t, int index) {
        if (index > size) {
            throw new RuntimeException("角标越界");
        }

        if (null != head) {
            Node node = new Node();
            node.setT(t);
            if (index == 0) {
                Node insert = head;
                Node prev = null;

                insert.setPrev(node);
                node.setPrev(prev);
                node.setNext(insert);
                setHead(node);
            } else if (index == size) {
                node.setPrev(tail);
                tail.setNext(node);
                setTail(node);
            } else {
                Node insert = head;
                Node prev = null;
                for (int i = 0; i < index; i++) {
                    insert = insert.getNext();
                    prev = insert.getPrev();
                }

                prev.setNext(node);
                insert.setPrev(node);

                node.setPrev(prev);
                node.setNext(insert);
            }
        } else {
            head = new Node();
            head.setT(t);
            setTail(head);
        }

        size++;

    }

    /**
     * 如果头结点为空， 插入头部， 如果头部的节点不为空， 直接添加到尾部， 更新尾部的节点为当前的node
     *
     * @param t
     */
    public void add(T t) {
        if (head == null) {
            head = new Node();
            head.setT(t);
            setTail(head);
        } else {
            Node node = new Node();
            node.setT(t);
            tail.setNext(node);
            node.setPrev(tail);
            setTail(node);
        }
        size++;
    }


    @Override
    public String toString() {
        if (head == null) {
            return null;
        } else {
            StringBuffer sb = new StringBuffer();
            if (null != head) {
                sb.append(head.getT()).append(",");
                Node next = head.getNext();
                while (!next.equals(tail)) {
                    sb.append(next.getT()).append(",");
                    next = next.getNext();
                }
                sb.append(next.getT());
            }
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        LinkedList list = new LinkedList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(null);
        list.add(null);

        System.out.println(list);


        LinkedList<String> list2 = new LinkedList<String>();
        list2.add("java");
        list2.add("php");
        list2.add("delph");
        list2.add("c++");

        //测试 根据索引插入元素
        list2.add("aaa", 4);
        list2.add("bbb", 3);

        System.out.println(list2);
    }


}
