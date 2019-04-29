package com.example.api.mode.proxy;

/**
 * @author shenzm
 * @date 2019-4-29
 * @description 作用
 */
public class ProxyImage implements Image {

    private RealImage realImage;

    private String fileName;

    public ProxyImage(String fileName){
        this.fileName = fileName;
    }

    @Override
    public void display() {
        if (null == realImage){
            realImage = new RealImage(fileName);
        }
        realImage.display();
    }
}
