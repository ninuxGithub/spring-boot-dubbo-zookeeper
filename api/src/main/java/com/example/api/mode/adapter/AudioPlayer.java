package com.example.api.mode.adapter;

/**
 * @author shenzm
 * @date 2019-4-25
 * @description 作用
 */
public class AudioPlayer implements MediaPlayer {

    MediaPlayerAdapter mediaPlayerAdapter;

    @Override
    public void play(String type, String fileName) {
        if (type.equals("mp3")){
            System.out.println("play mp3 "+ fileName);
        }else if (type.equals("vlc") || type.equals("mp4")){
            mediaPlayerAdapter = new MediaPlayerAdapter(type);
            mediaPlayerAdapter.play(type,fileName);
        }else{
            System.out.println("暂不支持类型："+ type);
        }
    }
}
