package com.example.api.queue;

import org.I0Itec.zkclient.ExceptionUtil;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author shenzm
 * @date 2019-2-18
 * @description 作用
 */
public class DistributedSimpleQueue<T> {

    private final ZkClient zkClient;

    private final String root;

    private static final String NODE_NAME = "n_";

    public DistributedSimpleQueue(ZkClient zkClient, String root) {
        this.zkClient = zkClient;
        this.root = root;
    }

    public int size() {
        return zkClient.getChildren(root).size();
    }

    public boolean isEmpty() {
        return zkClient.getChildren(root).size() == 0;
    }

    public boolean offer(T element) throws Exception {
        String nodePath = root.concat("/").concat(NODE_NAME);
        try {
            zkClient.createPersistentSequential(nodePath, element);
        } catch (ZkNoNodeException e) {
            zkClient.createPersistent(root);
            offer(element);
        } catch (Exception e) {
            ExceptionUtil.convertToRuntimeException(e);
        }
        return true;
    }

    public T poll() throws Exception {
        List<String> children = zkClient.getChildren(root);
        if (children.size() == 0) {
            return null;
        }
        Collections.sort(children, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return getNodeNumber(lhs, NODE_NAME).compareTo(getNodeNumber(rhs, NODE_NAME));
            }
        });

        for (String nodeName : children) {
            String nodePath = root.concat("/").concat(nodeName);
            try {
                T node = (T) zkClient.readData(nodePath);
                zkClient.delete(nodePath);
                return node;
            } catch (ZkNoNodeException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    private String getNodeNumber(String str, String nodeName) {
        int i = str.lastIndexOf(nodeName);
        if (i >= 0) {
            i += NODE_NAME.length();
            return i < str.length() ? str.substring(i) : "";
        }
        return null;
    }

}
