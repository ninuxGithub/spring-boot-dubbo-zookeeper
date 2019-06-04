package com.example.api.thread.csdn.test;

import java.util.Arrays;
import java.util.List;

/**
 * @author shenzm
 * @date 2019-6-3
 * @description 作用
 */
public class TestLambda {

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1,2,3,4,5);
        int sum = list.stream().mapToInt(Integer::intValue).sum();
        System.out.println(sum);
    }
}
