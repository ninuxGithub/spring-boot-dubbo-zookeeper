package com.example.api.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.CloseableUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author shenzm
 * @date 2019-2-14
 * @description 作用
 */
public class CuratorTest {

    private static final String PATH = "/master";


    public static void main(String[] args) {

        List<LeaderSelector> selectors = new ArrayList<>();
        List<CuratorFramework> clients = new ArrayList<>();
        try {
            for (int i = 0; i < 5; i++) {
                CuratorFramework client = getClient();
                clients.add(client);

                final String clientName = "client" + i;
                LeaderSelector leaderSelector = new LeaderSelector(client, PATH, new LeaderSelectorListenerAdapter() {
                    @Override
                    public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                        System.out.println(clientName + " is master");
                        TimeUnit.SECONDS.sleep(3);

                    }

                    @Override
                    public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
                    }
                });
                leaderSelector.autoRequeue();
                ;
                leaderSelector.start();
                selectors.add(leaderSelector);

            }

            TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            for (CuratorFramework client : clients) {
                CloseableUtils.closeQuietly(client);
            }

            for (LeaderSelector selector : selectors) {
                CloseableUtils.closeQuietly(selector);
            }

        }

    }

    private static CuratorFramework getClient() {
        RetryPolicy retryPolicy = new RetryNTimes(5, 5000);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("10.1.51.96:2181")
                .connectionTimeoutMs(5000)
                .sessionTimeoutMs(3000)
                .retryPolicy(retryPolicy)
                .namespace("com.example")
                .build();
        client.start();
        return client;
    }

}
