package com.valkyrie.nabeshimamac.lightsout;

import android.app.Activity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

/**
 * Created by NabeshimaMAC on 16/08/21.
 */

public class GameClientManager {
    public static final int CODE_CONNECT = 100;
    public static final int CODE_RANKING = 200;
    public static final int CODE_MEDAL = 300;

    public enum Ranking {
        Easy("初級ランキング", "CgkIxf6K1KAMEAIQAQ"),
        Normal("中級ランキング", "CgkIxf6K1KAMEAIQAw"),
        Hard("上級ランキング", "CgkIxf6K1KAMEAIQBA");

        public String name;
        public String id;

        Ranking(String name, String id) {
            this.name = name;
            this.id = id;
        }
    }

    public enum Medal {
        FirstTutorial("初めてのLightsOut", "CgkIxf6K1KAMEAIQCA"),
        FirstEazy("初級初クリア", "CgkIxf6K1KAMEAIQBQ"),
        FirstNomal("中級初クリア", "CgkIxf6K1KAMEAIQBg"),
        FirstHard("上級初クリア", "CgkIxf6K1KAMEAIQB"),
        ProEazy("初級パズルのプロ", "CgkIxf6K1KAMEAIQCQ"),
        ProNomal("中級パズルのプロ", "CgkIxf6K1KAMEAIQCg"),
        ProHard("上級パズルのプロ", "CgkIxf6K1KAMEAIQCw");
        public String name;
        public String id;

        Medal(String name, String id) {
            this.name = name;
            this.id = id;
        }

    }

    public static void submitScore(GoogleApiClient apiClient, Ranking ranking, int score) {
        if (apiClient.isConnected()) {
            Games.Leaderboards.submitScore(apiClient, ranking.id, score);
        }
    }

    public static void unlockMedal(GoogleApiClient apiClient, Medal medal) {
        if (apiClient.isConnected()) {
            Games.Achievements.unlock(apiClient, medal.id);
        }
    }

    public static void intentRanking(Activity activity, GoogleApiClient apiClient, Ranking ranking) {
        if (apiClient.isConnected()) {
            activity.startActivityForResult(
                    Games.Leaderboards.getLeaderboardIntent(apiClient, ranking.id),
                    CODE_RANKING
            );
        }
    }

    public static void intentMedal(Activity activity, GoogleApiClient apiClient) {
        if (apiClient.isConnected()) {
            activity.startActivityForResult(Games.Achievements.getAchievementsIntent(apiClient), CODE_MEDAL);
        }
    }

}
