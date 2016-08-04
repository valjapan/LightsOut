package com.valkyrie.nabeshimamac.lightsout;

import android.app.Activity;
import android.content.Context;

/**
 * Created by NabeshimaMAC on 16/08/03.
 */
public class MediaPlayer extends Activity{


    private android.media.MediaPlayer mp;

    public MediaPlayer(Context context){

        //BGMファイルを読み込む
        this.mp = android.media.MediaPlayer.create(context, R.raw.bgm01);
        this.mp.setLooping(true);
        this.mp.setVolume(1.0f, 1.0f);
    }

    //BGMを再生する
    public void start(){
        if (!mp.isPlaying()){
            mp.seekTo(0);
            mp.start();
        }
    }

    //BGMを停止する
    public void stop(){
        if (mp.isPlaying()) {
            mp.stop();
            mp.prepareAsync();
        }

        //TODO BGMのループ追加を行うこと
    }

//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//
//        }
}

