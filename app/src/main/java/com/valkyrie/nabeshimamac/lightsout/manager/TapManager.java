package com.valkyrie.nabeshimamac.lightsout.manager;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;

import com.valkyrie.nabeshimamac.lightsout.R;

/**
 * tap時の動作部分
 */
public class TapManager {
    private SoundPool soundPool;
    private int soundId;

    public static Intent createIntent(Context context) {
        return new Intent(context, TapManager.class);
    }

    public TapManager(Context context) {
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundId = soundPool.load(context, R.raw.tap01, 1);
    }
    //Tapされた時になる効果音

    public void play() {
        soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
    }

    public void repeatPlay(int repeat) {
        int count = 0;
        while (count < repeat) {
            play();
            count++;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
