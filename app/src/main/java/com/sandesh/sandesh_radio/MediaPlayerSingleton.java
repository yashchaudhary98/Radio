package com.sandesh.sandesh_radio;

import android.media.MediaPlayer;

public class MediaPlayerSingleton extends MediaPlayer {

    private static MediaPlayerSingleton instance;

    private MediaPlayerSingleton(){
        super();
    }

    public static MediaPlayerSingleton getInstance(){
        if(instance == null){
            instance = new MediaPlayerSingleton();
        }
        return instance;
    }
}
