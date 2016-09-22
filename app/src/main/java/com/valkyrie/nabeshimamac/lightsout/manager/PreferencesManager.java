package com.valkyrie.nabeshimamac.lightsout.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferencesのManager
 */
public class PreferencesManager {
    private static PreferencesManager instance;
    private SharedPreferences sharedPreferences;

    //ShardPreferencesの保存部分

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
    //２回目以降チュートリアル画面に行かないように

    public void checkTutorialEnd() {
        sharedPreferences.edit().putBoolean("tutorial", true).apply();
    }

    public int getClearCount(GameClientManager.Ranking ranking) {
        String key = getRankingKey(ranking);
        return sharedPreferences.getInt(key, 0);
    }

    public int addClearCount(GameClientManager.Ranking ranking) {
        int clearCount = getClearCount(ranking);
        clearCount++;
        String key = getRankingKey(ranking);
        sharedPreferences.edit().putInt(key, clearCount).apply();
        return clearCount;
    }

    private String getRankingKey(GameClientManager.Ranking ranking) {
        switch (ranking) {
            case Easy:
                return "easy_count";
            case Normal:
                return "normal_count";
            case Hard:
                return "hard_count";
            case Original:
                return "original_count";
            default:
                return null;
        }
    }
}
