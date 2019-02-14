package com.example.api.selectmaster;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shenzm
 * @date 2019-2-14
 * @description 作用
 */
public class LeaderSelectorZkClient {

    private static final int CLIENT_OPCITY = 10;

    private static final String ZOOKEEPER_SERVER_ADDRESS = "10.1.51.96:2181";

    public static void main(String[] args) {
        List<ZkClient> clients = new ArrayList<>();

        List<WorkerServer> workerServers = new ArrayList<>();

        try {
            for (int i = 0; i < CLIENT_OPCITY; i++) {
                ZkClient zkClient = new ZkClient(ZOOKEEPER_SERVER_ADDRESS, 5000, 5000, new SerializableSerializer());
                clients.add(zkClient);

                RunningData runningData = new RunningData();
                runningData.setCid(Long.valueOf(i));
                runningData.setName("client" + i);

                WorkerServer workerServer = new WorkerServer(runningData, zkClient);
                workerServers.add(workerServer);

                workerServer.start();
            }
            String s = "输入q停止";
            while (s != null && !s.equals("q")){
                System.out.println("输入的字符为：" + s);
                s = new BufferedReader(new InputStreamReader(System.in)).readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            for (WorkerServer workerServer : workerServers) {
                workerServer.stop();
            }

            for (ZkClient zkClient : clients) {
                zkClient.close();
            }
        }
    }
}
