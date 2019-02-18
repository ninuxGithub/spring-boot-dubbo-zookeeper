package com.example.api.pubsub;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shenzm
 * @date 2019-2-15
 * @description 作用
 */
public class SubscribZkClient {

    private static final int CLIENT_CAPCITY = 5;

    private static final String SERVER_ADDRESS = "10.1.51.96:2181";

    private static final String CONFIG_PATH = "/configs";

    private static final String COMMAND_PATH = "/commands";

    private static final String SERVERS_PATH = "/servers";

    public static void main(String[] args) {
        List<ZkClient> clients = new ArrayList<>();
        List<WorkServer> workServers = new ArrayList<>();
        ManageServer manageServer = null;

        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setDbUser("root");
        serverConfig.setDbPwd("123456");
        serverConfig.setDbUrl("jdbc://mysql://localhost:3306/mydb");

        ZkClient clientMange = new ZkClient(SERVER_ADDRESS, 5000, 5000, new BytesPushThroughSerializer());
        manageServer = new ManageServer(SERVERS_PATH, COMMAND_PATH, CONFIG_PATH, clientMange, serverConfig);
        manageServer.startServer();

        try {
            for (int i = 0; i < CLIENT_CAPCITY; i++) {
                ZkClient client = new ZkClient(SERVER_ADDRESS, 5000, 5000, new BytesPushThroughSerializer());
                clients.add(client);

                ServerData serverData = new ServerData();
                serverData.setId(i);
                serverData.setName("WorkServer#" + i);
                serverData.setAddress("10.1.51." + i);

                WorkServer workServer = new WorkServer(client, CONFIG_PATH, SERVERS_PATH, serverData, serverConfig);
                workServers.add(workServer);
                workServer.startServer();
            }

            Thread.sleep(500);
            System.out.println("敲回车键退出！\n");
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Shutting down...");

            for (WorkServer workServer : workServers) {
                try {
                    workServer.stopServer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (ZkClient client : clients) {
                try {
                    client.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }

}
