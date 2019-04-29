package com.example.api.mode.proxy;

/**
 * @author shenzm
 * @date 2019-4-29
 * @description 作用
 */
public class RealImage implements Image {
    String fileName;

    public RealImage(String fileName){
        this.fileName = fileName;
        System.out.println("load file : "+ fileName);
    }

    @Override
    public void display() {
        System.out.println("display file :"+ fileName);
    }
}
