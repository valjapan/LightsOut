package com.valkyrie.nabeshimamac.lightsout;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by NabeshimaMAC on 16/08/21.
 */
public class PreferencesManager {
    private static PreferencesManager instance;

    private SharedPreferences sharedPreferences;

    private PreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences("LightsOuts", Context.MODE_PRIVATE);
    }

    public static PreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new PreferencesManager(context);
        }
        return instance;
    }

    public boolean isTutorialEnd() {
        return sharedPreferences.getBoolean("tutorial", false);
    }

    public void checkTutorialEnd() {
        sharedPreferences.edit().putBoolean("tutorial", true).apply();
    }

    public int getClearCount(GameClientManager.Ranking ranking) {
        String key;
        switch (ranking) {
            case Easy:
                key = "easy_count";
                break;
            case Normal:
                key = "normal_count";
                break;
            case Hard:
                key = "hard_count";
                break;
            default:
                return 0;
        }
        return sharedPreferences.getInt(key, 0);
    }

    public int addClearCount(GameClientManager.Ranking ranking) {
        int clearCount = getClearCount(ranking);
        clearCount++;
        String key;
        switch (ranking) {
            case Easy:
                key = "easy_count";
                break;
            case Normal:
                key = "normal_count";
                break;
            case Hard:
                key = "hard_count";
                break;
            default:
                return 0;
        }
        sharedPreferences.edit().putInt(key, clearCount).apply();
        return clearCount;
    }

}
