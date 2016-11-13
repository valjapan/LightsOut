package com.valkyrie.nabeshimamac.lightsout.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.valkyrie.nabeshimamac.lightsout.BuildConfig;
import com.valkyrie.nabeshimamac.lightsout.MyApplication;
import com.valkyrie.nabeshimamac.lightsout.R;
import com.valkyrie.nabeshimamac.lightsout.manager.GameClientManager;
import com.valkyrie.nabeshimamac.lightsout.manager.ShareManager;

import java.util.Locale;

/**
 * title画面のActivity
 */

public class TitleActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private TextView textView, versionTextView;
    private ImageView googlePlayImageView, editButtonImageView, shareTwitter;
    private Button playEasy, playNormal, playHard , playShare , goEasyRank , goNormalRank,
            goHardRank , goShareRank ,returmMode;
    private LinearLayout modeLayout, otherLayout;
    private RelativeLayout rankLayout;

    private GoogleApiClient apiClient;
    private boolean mIntentInProgress;
    private Locale locale = Locale.getDefault();
    public static Intent createIntent(Context context) {
        return new Intent(context, TitleActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        Typeface gothicAdobe = Typeface.createFromAsset(getAssets(), "AdobeGothicStd-Bold.otf");
        Typeface gothicApple = Typeface.createFromAsset(getAssets(), "AppleSDGothicNeo.ttc");
        Typeface cooperBlack = Typeface.createFromAsset(getAssets(), "CooperBlackStd.otf");
        //フォントの読み込み

        modeLayout = (LinearLayout) findViewById(R.id.modeLayout);
        otherLayout = (LinearLayout) findViewById(R.id.OtherLayout);
        rankLayout = (RelativeLayout) findViewById(R.id.rankLayout);
        //Layout関係のIDの関連付け


        googlePlayImageView = (ImageView) findViewById(R.id.googleGame);
        editButtonImageView = (ImageView) findViewById(R.id.EditButton);
        shareTwitter = (ImageView) findViewById(R.id.shareTwitter);
        //ImageViewのIDの関連付け

        textView = (TextView) findViewById(R.id.textView);
        versionTextView = (TextView) findViewById(R.id.versionName);
        //TextViewIDの関連付け

        playEasy = (Button) findViewById(R.id.PlayEasy);
        playNormal = (Button) findViewById(R.id.PlayNormal);
        playHard = (Button) findViewById(R.id.PlayHard);
        playShare = (Button) findViewById(R.id.PlayShare);
        goEasyRank = (Button) findViewById(R.id.rankEasy);
        goNormalRank = (Button) findViewById(R.id.rankNormal);
        goHardRank = (Button) findViewById(R.id.rankHard);
        goShareRank = (Button) findViewById(R.id.rankOriginal);
        returmMode = (Button) findViewById(R.id.returnTitle);
        //ButtonのIDの関連付け

        textView.setTypeface(gothicAdobe);
        playEasy.setTypeface(gothicApple);
        playNormal.setTypeface(gothicApple);
        playHard.setTypeface(gothicApple);
        playShare.setTypeface(gothicApple);
        goEasyRank.setTypeface(gothicApple);
        goNormalRank.setTypeface(gothicApple);
        goHardRank.setTypeface(gothicApple);
        goShareRank.setTypeface(gothicApple);
        //フォントの指定

        modeLayout.setVisibility(View.VISIBLE);
        rankLayout.setVisibility(View.INVISIBLE);
        //表示させるかどうか

        apiClient = ((MyApplication) getApplication()).getGoogleApiClient();


        versionTextView.setText("v" + BuildConfig.VERSION_NAME);

        if (locale.equals(Locale.JAPAN)){
            playEasy.setText("初級");
            playNormal.setText("中級");
            playHard.setText("上級");
            playShare.setText("共有問題");
            goEasyRank.setText("初級ランキング");
            goNormalRank.setText("中級ランキング");
            goHardRank.setText("上級ランキング");
            goShareRank.setText("オリジナルランキング");
            returmMode.setText("戻る");

            findViewById(R.id.shareTwitter).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareManager.shareTwitter(TitleActivity.this,
                            "新感覚シンプルパズルゲーム【LightsOut】。" +
                                    "\nルール は簡単、押したパネルとその上下左右のボタンの色が反転する。" +
                                    "全てのパネルを反転色にすればゲームクリアだ。" +
                                    "\n君もチャレンジしてみないか。 #LightsOut" +
                                    "\nhttps://play.google.com/store/apps/details?id=com.valkyrie.nabeshimamac.lightsout");
                }
            });
            //Twitterの投稿文章

        }else {
            playEasy.setText("Easy");
            playNormal.setText("Normal");
            playHard.setText("Hard");
            playShare.setText("ShareMode");
            goEasyRank.setText("EasyRanking");
            goNormalRank.setText("NormalRanking");
            goHardRank.setText("HardRanking");
            goShareRank.setText("OriginalRanking");
            returmMode.setText("Back");

            findViewById(R.id.shareTwitter).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareManager.shareTwitter(TitleActivity.this,
                            "LightsOut is a simple puzzle game." +
                                    "\n Anyone can easily play." +
                                    "\n Waiting challenge." +
                                    "\n #LightsOut" +
                                    "\nhttps://play.google.com/store/apps/details?id=com.valkyrie.nabeshimamac.lightsout");
                }
            });
            //Twitterの投稿文章
        }

        editButtonImageView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                Toast.makeText(getApplicationContext(), getString(R.string.create_mode), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        apiClient.registerConnectionCallbacks(this);
        apiClient.registerConnectionFailedListener(this);
        // 今回は画面が表示されるたびに必ず接続させる
        if (!apiClient.isConnected() || !apiClient.hasConnectedApi(Games.API)) {
            apiClient.connect();
            Log.d(TitleActivity.class.getSimpleName(), "Connecting Google play");
        }
    }

    @Override
    protected void onStop() {
        apiClient.unregisterConnectionCallbacks(this);
        apiClient.unregisterConnectionFailedListener(this);
        super.onStop();
    }
    //stopした時の処理

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GameClientManager.CODE_CONNECT) {
            mIntentInProgress = false;
            if (resultCode != RESULT_OK) {
                // エラーの場合、resultCodeにGamesActivityResultCodes内の値が入っている
                return;
            }
            if (!apiClient.isConnected()) {
                apiClient.reconnect();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        int errorCode = connectionResult.getErrorCode();
        // サインインしていない場合、サインイン処理を実行する
        if (errorCode == ConnectionResult.SIGN_IN_REQUIRED
                && !mIntentInProgress && connectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                connectionResult.startResolutionForResult(this, GameClientManager.CODE_CONNECT);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                apiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // プレイヤー情報取得
        Player player = Games.Players.getCurrentPlayer(apiClient);
        Log.d(MainActivity.class.getSimpleName(), "onConnected:" + player.getName());
    }

    @Override
    public void onConnectionSuspended(int i) {
        apiClient.connect();
    }

    public void goEasy(View v) {
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("mode", 0);
        startActivity(intent);
        //初級
    }

    public void goNormal(View v) {
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("mode", 1);
        startActivity(intent);
        //中級
    }

    public void goHard(View v) {
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("mode", 2);
        startActivity(intent);
        //上級
    }
    
    public void goShare(View v){
        final Intent intent = new Intent(this, SharedQuestionListActivity.class);
        startActivity(intent);
        //共有問題
    }

    public void goSetting(View v){
        final Intent intent = SettingActivity.createIntent(this);
        startActivity(intent);
        //設定画面
    }

    public void googleGame(View v) {
        playEasy.setVisibility(View.INVISIBLE);
        playNormal.setVisibility(View.INVISIBLE);
        playHard.setVisibility(View.INVISIBLE);
        playShare.setVisibility(View.INVISIBLE);
        rankLayout.setVisibility(View.VISIBLE);
        //PlayGameを利用したランキング一覧を表示
    }

    public void goEdit(View v) {
        Intent intent = new Intent(this, MakeListActivity.class);
        startActivity(intent);
        //クリエイトモード
    }

    public void goEasyRank(View v) {
        GameClientManager.intentRanking(this, apiClient, GameClientManager.Ranking.Easy);
        //初級ランキング
    }

    public void goNormalRank(View v) {
        GameClientManager.intentRanking(this, apiClient, GameClientManager.Ranking.Normal);
        //中級ランキング
    }

    public void goHardRank(View v) {
        GameClientManager.intentRanking(this, apiClient, GameClientManager.Ranking.Hard);
        //上級ランキング
    }

    public void goOriginalRank(View v){
        GameClientManager.intentRanking(this, apiClient, GameClientManager.Ranking.Original);
        //オリジナルランキング
    }

    public void goTitle(View v) {
        playEasy.setVisibility(View.VISIBLE);
        playNormal.setVisibility(View.VISIBLE);
        playHard.setVisibility(View.VISIBLE);
        playShare.setVisibility(View.VISIBLE);
        rankLayout.setVisibility(View.INVISIBLE);
        //ランキング選択画面の非表示
    }

    public void goMedal(View v) {
        GameClientManager.intentMedal(this, apiClient);
    }
    //実績を見る

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (locale.equals(Locale.JAPAN)) {
                new AlertDialog.Builder(this)
                        .setTitle("アプリケーションの終了")
                        .setMessage("アプリケーションを終了してもよろしいですか？")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //自動生成されたメソッド・スタブ
                                TitleActivity.this.finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //自動生成されたメソッド・スタブ
                            }
                        })
                        .show();
            }else {
                new AlertDialog.Builder(this)
                        .setTitle("End of application")
                        .setMessage("Do you want to exit the application?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //自動生成されたメソッド・スタブ
                                TitleActivity.this.finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //自動生成されたメソッド・スタブ
                            }
                        })
                        .show();
            }
            return true;
        }
        return false;
    }
    //アプリが落ちていいかの確認
}
