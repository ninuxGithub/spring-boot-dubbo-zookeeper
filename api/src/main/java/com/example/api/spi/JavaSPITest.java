package com.example.api.spi;


import com.alibaba.dubbo.common.extension.ExtensionLoader;

/**
 * @author shenzm
 * @date 2019-2-15
 * @description 作用
 */
public class JavaSPITest {


    public static void main(String[] args) {
        /*ServiceLoader<Robot> serviceLoader = ServiceLoader.load(com.example.api.Robot.class);
        System.out.println("Java SPI");
        serviceLoader.forEach(com.example.api.Robot::sayHello);*/

        ExtensionLoader<Robot> extensionLoader =
                ExtensionLoader.getExtensionLoader(Robot.class);
        Robot optimusPrime = extensionLoader.getExtension("optimusPrime");
        optimusPrime.sayHello();
        Robot bumblebee = extensionLoader.getExtension("bumblebee");
        bumblebee.sayHello();
    }
}
