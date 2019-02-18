package com.example.api.pubsub;

import com.alibaba.fastjson.JSON;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;

/**
 * @author shenzm
 * @date 2019-2-15
 * @description 作用
 */
public class WorkServer {

    private ZkClient zkClient;
    private String configPath;
    private String serverPath;
    private ServerData serverData;
    private ServerConfig serverConfig;
    private IZkDataListener dataListener;

    public WorkServer(ZkClient zkClient, String configPath, String serverPath, ServerData serverData,
                      ServerConfig serverConfig) {
        this.zkClient = zkClient;
        this.configPath = configPath;
        this.serverPath = serverPath;
        this.serverData = serverData;
        this.serverConfig = serverConfig;
        this.dataListener = new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                String json = new String((byte[]) data);
                ServerConfig serverConfig = JSON.parseObject(json, ServerConfig.class);
                updateConfig(serverConfig);
                System.out.println("worder server : config is " + serverConfig.toString());
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {

            }
        };
    }

    public void startServer() {
        System.out.println("start work server");
        registMe();
        zkClient.subscribeDataChanges(configPath, dataListener);
    }

    public void stopServer() {
        System.out.println("stop work server");
        zkClient.unsubscribeDataChanges(configPath, dataListener);
    }

    private void registMe() {
        System.out.println("word server regist to /server");
        String path = serverPath.concat("/").concat(serverData.getAddress());
        try {
            zkClient.createEphemeral(path, JSON.toJSONString(serverData).getBytes());
        } catch (ZkNoNodeException e) {
            zkClient.createPersistent(serverPath, true);
            registMe();
        }
    }

    private void updateConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }
}
