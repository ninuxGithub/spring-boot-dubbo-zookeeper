package com.example.api.loader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author shenzm
 * @date 2019-6-4
 * @description 作用
 */
public class TestClassLoader {
    private static String classPath ="D:\\dev\\workspace-sts-3.9.0.RELEASE\\spring-boot-dubbo-zookeeper\\api\\target\\classes\\";

    public static void main(String[] args) {
        while (true){
            test1();
            test2();
            try {
                TimeUnit.SECONDS.sleep(8);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void test1() {
        SclassLoader sclassLoader = new SclassLoader(Thread.currentThread().getContextClassLoader(), classPath);
        try {
            Class<?> clazz = sclassLoader.loadClass("com.example.api.loader.Person");
            String classLoaderName = clazz.getClassLoader().getClass().getName();
            //sun.misc.Launcher$AppClassLoader
            System.out.println(classLoaderName);
            //类加载器的不同  Person 不可以转换
            //Person person = (Person) clazz.newInstance();
            //System.out.println(person);

            Method method = clazz.getMethod("toString", new Class[]{});
            System.out.println(method.invoke(clazz.newInstance()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    private static void test2() {
        SclassLoader sclassLoader = new SclassLoader(null, classPath);
        try {
            Class<?> clazz = sclassLoader.loadClass("com.example.api.loader.Person");
            //com.example.api.loader.SclassLoader
            String classLoaderName = clazz.getClassLoader().getClass().getName();
            System.out.println(classLoaderName);
            //类加载器的不同  Person 不可以转换
            //Person person = (Person) clazz.newInstance();
            //System.out.println(person);

            Method method = clazz.getMethod("toString", new Class[]{});
            System.out.println(method.invoke(clazz.newInstance()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
