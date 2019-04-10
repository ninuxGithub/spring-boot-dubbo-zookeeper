package com.example.api.list;

import java.util.Arrays;

/**
 * @author shenzm
 * @date 2019-4-3
 * @description 作用
 */
public class ArrayCopyTest {

    public static void main(String[] args) {
        Integer[] arr = new Integer[]{1,2,3,4,5};

        Integer[] arrNew = new Integer[arr.length +1];

        System.arraycopy(arr,1,arrNew,2,4);

        System.out.println(Arrays.toString(arrNew));
    }
}
