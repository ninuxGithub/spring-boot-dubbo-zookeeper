package com.example.provider;

import java.math.BigDecimal;

/**
 * @author shenzm
 * @date 2019-3-14
 * @description 作用
 */
public class Test {

    public static void main(String[] args) {

        BigDecimal dcm = new BigDecimal(1.23434234d);
        BigDecimal scale = dcm.setScale(1, BigDecimal.ROUND_HALF_UP);
        System.out.println(scale+"元");

        System.out.println(scale.toString());


    }
}
