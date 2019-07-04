package com.example.api.zookeeper.lock;

import com.google.common.base.Charsets;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;


/**
 * @author shenzm
 * @date 2019-7-3
 * @description 作用
 */
public class ZookeeperWatchTest {

    private static ZkClient client = new ZkClient("10.1.51.96:2181", 1000);

    public static void main(String[] args) {
        String path = "/name";
        String ppath = "/pname";

        //rmr 递归删除
        client.deleteRecursive(path);
        client.deleteRecursive(ppath);

        client.setZkSerializer(new ZkSerializer() {
            @Override
            public byte[] serialize(Object data) throws ZkMarshallingError {
                return String.valueOf(data).getBytes(Charsets.UTF_8);
            }

            @Override
            public Object deserialize(byte[] bytes) throws ZkMarshallingError {
                return new String(bytes, Charsets.UTF_8);
            }
        });

       /* if (!client.exists(path)) {
            String nodePath = client.createEphemeralSequential(path, "java");
            client.subscribeDataChanges(nodePath, new IZkDataListener() {
                @Override
                public void handleDataChange(String dataPath, Object data) throws Exception {
                    System.out.println(dataPath + "节点数据改变" + data);
                }

                @Override
                public void handleDataDeleted(String dataPath) throws Exception {
                    System.out.println(dataPath + "节点删除");
                }
            });

            try {
                Thread.currentThread().join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
        if (!client.exists(ppath)) {
            client.createPersistent(ppath, "java");
            client.subscribeDataChanges(ppath, new IZkDataListener() {
                @Override
                public void handleDataChange(String dataPath, Object data) throws Exception {
                    System.out.println(dataPath + "节点数据改变" + data);
                }

                @Override
                public void handleDataDeleted(String dataPath) throws Exception {
                    System.out.println(dataPath + "节点删除");
                }
            });

            try {
                Thread.currentThread().join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
