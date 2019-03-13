package com.example.api.condition;

import java.util.Arrays;

/**
 * @author shenzm
 * @date 2019-3-6
 * @description 作用
 */
public class ConditionTest {

    public static void main(String[] args) {
        effectiveJuedge();

        bubbleSort();

        selectSort();


        sort();

        insertSort();


    }

    private static void insertSort() {
        int[] arr = {3, 8, 2, 90, 78, 23, 22, 1, 89};
        int prevIndex,current;
        for(int i=1 ; i<arr.length; i++){
            prevIndex = i-1;
            current = arr[i];
            while(prevIndex>=0 && arr[prevIndex] > current){
                arr[prevIndex +1] = arr[prevIndex];
                prevIndex --;
            }
            arr[prevIndex +1] = current;
        }
        System.out.println(Arrays.toString(arr));
    }

    private static void sort() {
        int[] arr = {3, 8, 2, 90, 78, 23, 22, 1, 89};

        int count =0;
        for (int i=0; i<arr.length; i++){
            if(i >=1){
                for(int j=i; j>=0; j--){
                    if(j>=1 && arr[j]<arr[j-1]){
                        System.out.println(Arrays.toString(arr));
                        count++;
                        int temp = arr[j];
                        arr[j]=arr[j-1];
                        arr[j-1]=temp;
                    }
                }
            }

        }

        System.out.println(Arrays.toString(arr) + " count :"+ count);
    }

    /**
     * 0 角标与其他的位置进行比较 ，选择最新的数据放到0角标
     * 然后选择第二小的放到1角标
     * ....
     */
    public static void selectSort() {
        System.out.println("selectSort 开始");
        int count =0;
        int[] arr = {3, 8, 2, 90, 78, 23, 22, 1, 89};
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[i]) {
                    System.out.println(Arrays.toString(arr));
                    count++;
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
        System.out.println(Arrays.toString(arr) + " count :"+ count);
    }


    /**
     * 冒泡排序： 数组中间的2个位置的数据进行对比 如果前一个比后一个大，交换位置  这样通过内循环慢慢的将最大的数据放到最后
     */
    private static void bubbleSort() {
        System.out.println("bubbleSort 开始");
        int count =0;
        int[] arr = {3, 8, 2, 90, 78, 23, 22, 1, 89};
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    System.out.println(Arrays.toString(arr));
                    count++;
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        System.out.println(Arrays.toString(arr) + " count :"+ count);
    }

    public static void effectiveJuedge() {
        Integer[] rangeArr = {0, 7, 14, 30, 60, 90, 120, 180, 360, 620};
        Double[] valueArr = {1d, 0.9, 0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1};
        int range = 0;
        Double value = 0d;
        int targetRange = 300;

        for (int i = rangeArr.length - 1; i >= 0; i--) {
            range = rangeArr[i];
            value = valueArr[i];
            if (targetRange >= range) {
                System.out.println("range : " + range + " value : " + value);
                break;
            }
        }
    }
}
