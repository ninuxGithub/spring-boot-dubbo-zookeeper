package com.example.api.regdis;

import java.io.IOException;

/**
 * @author shenzm
 * @date 2019-2-13
 * @description 作用
 */
public class TestZookeeper {

    public static void main(String[] args) {
        String registryAddress = "10.1.51.96:2181";
        ServiceRegistry serviceRegistry = new ServiceRegistry(registryAddress);
        serviceRegistry.registry("10.1.51.96:3000");


        ServiceDiscovery serviceDiscovery = new ServiceDiscovery(registryAddress);
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
