package com.valkyrie.nabeshimamac.lightsout;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * Created by NabeshimaMAC on 16/08/04.
 */
public class Tap {
    private SoundPool soundPool;
    private  int soundId;
    public Tap(Context context){
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        soundId = soundPool.load(context,R.raw.tap01,1);
    }

    public void play() {
        soundPool.play(soundId,1.0f,1.0f,0,0,1.0f);
    }
    public void repeatPlay(int repeat){
        int count =0;
        while (count < repeat) {
            play();
            count ++;
            try {
                Thread.sleep(500);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

    }
}
