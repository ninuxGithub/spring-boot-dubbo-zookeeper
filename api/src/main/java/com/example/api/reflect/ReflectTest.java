package com.example.api.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author shenzm
 * @date 2019-5-10
 * @description 作用
 */
public class ReflectTest {

    public static void main(String[] args) {
        ExecutorService threadpool = Executors.newFixedThreadPool(5);
        while(true){
            for(int i=0; i<3; i++){
                threadpool.execute(new Runnable() {
                    @Override
                    public void run() {
                        combine();
                    }
                });
            }
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static void reflect() {
        Class clazz = Demo.class;
        try {
            Object o = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            System.out.println("属性的获取");
            for (Field field : fields) {
                int modifiers = field.getModifiers();
                String typeName = field.getType().getSimpleName();
                Annotation[] annotations = field.getDeclaredAnnotations();
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < annotations.length; i++) {
                    if (i > 0) {
                        sb.append(",");
                    }
                    Annotation annotation = annotations[i];
                    Method[] anoMethods = annotation.annotationType().getDeclaredMethods();
                    StringBuffer msb = new StringBuffer();
                    for (int k = 0; k < anoMethods.length; k++) {
                        if (k > 0) {
                            msb.append(" ,");
                        }
                        Method method = anoMethods[k];
                        msb.append(Modifier.toString(method.getModifiers()) + " " + method.getName() + " " + method.getDefaultValue());
                    }
                    sb.append(annotation.annotationType().getSimpleName() + "(" + msb.toString() + ")");
                }
                System.out.println(Modifier.toString(modifiers) + " " + typeName + " " + field.getName() + "  @" + sb.toString());
            }

            System.out.println("方法声明");
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                Parameter[] parameters = method.getParameters();
                StringBuffer param = new StringBuffer();
                for (int i = 0; i < parameters.length; i++) {
                    Parameter parameter = parameters[i];
                    if (i > 0) {
                        param.append(",");
                    }
                    param.append(parameter.getType().getSimpleName()).append(" ").append(parameter.getName());
                }
                System.out.println(Modifier.toString(method.getModifiers()) + " " + method.getReturnType().getSimpleName() + " " + method.getName() + "(" + param.toString() + ")");
            }


        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     *4 *3 * 2 = 24;
     * c4/3  c3/2   c2/1
     */
    public static void combine() {
        int[] arr = {1, 2, 3, 4};
        List<Integer[]> list = new ArrayList<>();
        Integer[] tempArr = new Integer[3];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                for (int k = 0; k < arr.length; k++) {
                    if (arr[i] != arr[j] && arr[i] != arr[k] && arr[j] != arr[k]) {
                        tempArr[0] = arr[i];
                        tempArr[1] = arr[j];
                        tempArr[2] = arr[k];
                        Integer sum = Arrays.stream(tempArr).reduce((s, item) -> s+=item).get();
                        System.out.println(Thread.currentThread().getName()+"  "+ arr[i] + arr[j] + arr[k] + "  sum :" + sum);
                        list.add(tempArr);
                    }
                }
            }
        }
        System.out.println("list.size  " + list.size());
    }
}
