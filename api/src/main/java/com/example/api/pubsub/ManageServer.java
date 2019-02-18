package com.example.api.pubsub;

import com.alibaba.fastjson.JSON;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;

import java.util.List;

/**
 * @author shenzm
 * @date 2019-2-15
 * @description 作用
 */
public class ManageServer {
    private String serverPath;
    private String commandPath;
    private String configPaht;
    private ZkClient zkClient;
    private ServerConfig serverConfig;
    private IZkChildListener zkChildListener;
    private IZkDataListener dataListener;
    private List<String> workServerList;

    public ManageServer(String serverPath, String commandPath, String configPaht, ZkClient zkClient, ServerConfig serverConfig) {
        this.serverPath = serverPath;
        this.commandPath = commandPath;
        this.configPaht = configPaht;
        this.zkClient = zkClient;
        this.serverConfig = serverConfig;
        this.zkChildListener = new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                workServerList = currentChilds;
                System.out.println("manager server: work server list ");
                execList();
            }
        };
        this.dataListener = new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                String cmd = new String((byte[]) data);
                exeCmd(cmd);
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {

            }
        };
    }

    private void exeCmd(String cmd) {
        if ("list".equals(cmd)) {
            execList();
        } else if ("create".equals(cmd)) {
            execCreate();
        } else if ("modify".equals(cmd)) {
            execModify();
        } else {
            System.out.println("manager server : ");
        }
    }

    private void execModify() {
        serverConfig.setDbUser(serverConfig.getDbUser() + "_modify");
        try {
            zkClient.writeData(configPaht, JSON.toJSONString(serverConfig).getBytes());
        } catch (ZkNoNodeException e) {
            execCreate();
        }

    }

    private void execCreate() {
        if (!zkClient.exists(configPaht)) {
            try {
                zkClient.createPersistent(configPaht, JSON.toJSONString(serverConfig).getBytes());
            } catch (ZkNodeExistsException e) {
                zkClient.writeData(configPaht, JSON.toJSONString(serverConfig).getBytes());
            } catch (ZkNoNodeException e) {
                String parentDir = configPaht.substring(0, configPaht.lastIndexOf("/"));
                zkClient.createPersistent(parentDir, true);
                execCreate();
            }
        } else {
            System.out.println("manage server : " + configPaht + " is exist");
        }
    }

    private void execList() {
        System.out.println(workServerList.toString() + "\n");
    }

    public void startServer() {
        System.out.println("start manage server");
        zkClient.subscribeChildChanges(serverPath, zkChildListener);
        zkClient.subscribeDataChanges(commandPath, dataListener);
    }

    public void stopServer() {
        System.out.println("stop mange server");
        zkClient.unsubscribeChildChanges(serverPath, zkChildListener);
        zkClient.unsubscribeDataChanges(commandPath, dataListener);
    }


}
