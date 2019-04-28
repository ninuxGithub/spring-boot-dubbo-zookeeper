package com.example.api.mode.adapter;

/**
 * @author shenzm
 * @date 2019-4-25
 * @description 作用
 */
public class Mp4Player implements AdvancedMediaPlayer {
    @Override
    public void playVlc(String fileName) {

    }

    @Override
    public void playMp4(String fileName) {
        System.out.println("mp4 play : "+ fileName);
    }
}
