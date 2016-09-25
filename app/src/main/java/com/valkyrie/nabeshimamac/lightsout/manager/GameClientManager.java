package com.valkyrie.nabeshimamac.lightsout.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

/**
 * GooglePlayGameServiceの実績・ランキング等のコード
 * 送信・データの要求する時の処理
 */

public class GameClientManager {
    public static final int CODE_CONNECT = 100;
    public static final int CODE_RANKING = 200;
    public static final int CODE_MEDAL = 300;
    //どこの画面に遷移しましたよっていう値

    public static Intent createIntent(Context context) {
        return new Intent(context, GameClientManager.class);
    }

    public enum Ranking {
        Easy("初級ランキング", "CgkIxf6K1KAMEAIQAQ"),
        Normal("中級ランキング", "CgkIxf6K1KAMEAIQAw"),
        Hard("上級ランキング", "CgkIxf6K1KAMEAIQBA"),
        Original("オリジナルランキング", "CgkIxf6K1KAMEAIQDQ");
        //GooglePlayのリーダーボード名,ID

        public String name;
        public String id;

        Ranking(String name, String id) {
            this.name = name;
            this.id = id;
        }
    }

    public enum Medal {
        //enum=列挙子
        //大量にデータを宣言使うときに使う
        FirstTutorial("初めてのLightsOut", "CgkIxf6K1KAMEAIQCA"),
        FirstMakePuzzlePlay("自作Lights Outをプレイしよう", "CgkIxf6K1KAMEAIQDA"),
        FirstEazy("初級初クリア", "CgkIxf6K1KAMEAIQBQ"),
        FirstNomal("中級初クリア", "CgkIxf6K1KAMEAIQBg"),
        FirstHard("上級初クリア", "CgkIxf6K1KAMEAIQBw"),
        ProEazy("初級パズルのプロ", "CgkIxf6K1KAMEAIQCQ"),
        ProNomal("中級パズルのプロ", "CgkIxf6K1KAMEAIQCg"),
        ProHard("上級パズルのプロ", "CgkIxf6K1KAMEAIQCw");
        //(実績名,実績コード(ディベロッパーコンソールにあり))
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
            //ランキングに送るデータ
        }
    }

    public static void unlockMedal(GoogleApiClient apiClient, Medal medal) {
        if (apiClient.isConnected()) {
            Games.Achievements.unlock(apiClient, medal.id);
            //実績解除のためのコード
        }
    }

    public static void intentRanking(Activity activity, GoogleApiClient apiClient, Ranking ranking) {
        if (apiClient.isConnected()) {
            activity.startActivityForResult(
                    Games.Leaderboards.getLeaderboardIntent(apiClient, ranking.id),
                    CODE_RANKING
                    //ランキングに送るデータ

            );
        }
    }

    public static void intentMedal(Activity activity, GoogleApiClient apiClient) {
        if (apiClient.isConnected()) {
            activity.startActivityForResult(Games.Achievements.getAchievementsIntent(apiClient), CODE_MEDAL);
            //実績解除のためのコード
        }
    }

}
