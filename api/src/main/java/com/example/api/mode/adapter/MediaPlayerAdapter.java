package com.example.api.mode.adapter;

/**
 * @author shenzm
 * @date 2019-4-25
 * @description 作用
 */
public class MediaPlayerAdapter implements MediaPlayer {

    AdvancedMediaPlayer advancedMediaPlayer;

    public MediaPlayerAdapter(String type){
        if (type.equals("vlc")){
            advancedMediaPlayer = new VlcPlayer();
        }else if (type.equals("mp4")){
            advancedMediaPlayer = new Mp4Player();
        }
    }

    @Override
    public void play(String type, String fileName) {
        if (type.equals("vlc")){
            advancedMediaPlayer.playVlc(fileName);
        }else if (type.equals("mp4")){
            advancedMediaPlayer.playMp4(fileName);
        }
    }
}
