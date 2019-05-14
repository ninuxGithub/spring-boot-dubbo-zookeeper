package com.example.api.test;

import java.util.Properties;
import java.util.Set;

/**
 * @author shenzm
 * @date 2019-5-13
 * @description 作用
 */
public class DirTest {

    public static void main(String[] args) {
        Properties properties = System.getProperties();
        for (Object key : properties.keySet()){
            String property = properties.getProperty(key.toString());
            System.out.println(key  + "\t" + property);
        }

    }
}
