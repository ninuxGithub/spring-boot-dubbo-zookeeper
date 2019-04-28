package com.example.api.mode.adapter;

/**
 * @author shenzm
 * @date 2019-4-25
 * @description 作用
 */
public class VlcPlayer implements AdvancedMediaPlayer {
    @Override
    public void playVlc(String fileName) {
        System.out.println("vlc play : "+ fileName);
    }

    @Override
    public void playMp4(String fileName) {

    }
}
