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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.valkyrie.nabeshimamac.lightsout.BuildConfig;
import com.valkyrie.nabeshimamac.lightsout.MyApplication;
import com.valkyrie.nabeshimamac.lightsout.R;
import com.valkyrie.nabeshimamac.lightsout.manager.GameClientManager;
import com.valkyrie.nabeshimamac.lightsout.manager.ShareManager;

/**
 * title画面のActivity
 */

public class TitleActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private TextView textView, versionTextView;
    private ImageView googlePlayImageView, editButtonImageView, shareTwitter;
    private Button playEazy, playNomal, playHard , playShare , goEazyRank , goNomalRank ,
            goHardRank , goShareRank ,returmMode;
    private LinearLayout modeLayout, otherLayout;
    private RelativeLayout rankLayout;

    private GoogleApiClient apiClient;
    private boolean mIntentInProgress;

    public static Intent createIntent(Context context) {
        return new Intent(context, TitleActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        Typeface gothicAdobe = Typeface.createFromAsset(getAssets(), "AdobeGothicStd-Bold.otf");
        Typeface gothicApple = Typeface.createFromAsset(getAssets(), "AppleSDGothicNeo.ttc");


        modeLayout = (LinearLayout) findViewById(R.id.modeLayout);
        rankLayout = (RelativeLayout) findViewById(R.id.rankLayout);
        otherLayout = (LinearLayout) findViewById(R.id.OtherLayout);

        googlePlayImageView = (ImageView) findViewById(R.id.googleGame);
        editButtonImageView = (ImageView) findViewById(R.id.EditButton);
        shareTwitter = (ImageView) findViewById(R.id.shareTwitter);

        textView = (TextView) findViewById(R.id.textView);



        versionTextView = (TextView) findViewById(R.id.versionName);

        playEazy = (Button) findViewById(R.id.PlayEazy);
        playNomal = (Button) findViewById(R.id.PlayNomal);
        playHard = (Button) findViewById(R.id.PlayHard);
        playShare = (Button) findViewById(R.id.PlayShare);
        goEazyRank = (Button) findViewById(R.id.rankEazy);
        goNomalRank = (Button) findViewById(R.id.rankNomal);
        goHardRank = (Button) findViewById(R.id.rankHard);
        goShareRank = (Button) findViewById(R.id.rankOriginal);
        returmMode = (Button) findViewById(R.id.returnTitle);

        textView.setTypeface(gothicAdobe);
        playEazy.setTypeface(gothicApple);
        playNomal.setTypeface(gothicApple);
        playHard.setTypeface(gothicApple);
        playShare.setTypeface(gothicApple);
        goEazyRank.setTypeface(gothicApple);
        goNomalRank.setTypeface(gothicApple);
        goHardRank.setTypeface(gothicApple);
        goShareRank.setTypeface(gothicApple);

        modeLayout.setVisibility(View.VISIBLE);
        rankLayout.setVisibility(View.INVISIBLE);
        apiClient = ((MyApplication) getApplication()).getGoogleApiClient();


        versionTextView.setText("v" + BuildConfig.VERSION_NAME);

            findViewById(R.id.shareTwitter).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareManager.shareTwitter(TitleActivity.this,
                            "新感覚シンプルパズルゲーム【LightsOut】。" +
                            "\nルール は簡単、押したパネルとその上下左右のボタンの色が反転する。" +
                            "全てのパネルを水色からピンクにすればゲームクリアだ。" +
                            "\n君もチャレンジしてみないか。 #LightsOutGame" +
                            "\nhttps://play.google.com/store/apps/details?id=com.valkyrie.nabeshimamac.lightsout");
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

    public void goEazy(View v) {
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("mode", 0);
        startActivity(intent);
    }

    public void goNomal(View v) {
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("mode", 1);
        startActivity(intent);
    }

    public void goHard(View v) {
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("mode", 2);
        startActivity(intent);
    }
    
    public void goShare(View v){
        final Intent intent = new Intent(this, SharedQuestionListActivity.class);
        startActivity(intent);
    }

    public void goSetting(View v){
        final Intent intent = SettingActivity.createIntent(this);
        startActivity(intent);
    }

    public void googleGame(View v) {
        playEazy.setVisibility(View.INVISIBLE);
        playNomal.setVisibility(View.INVISIBLE);
        playHard.setVisibility(View.INVISIBLE);
        playShare.setVisibility(View.INVISIBLE);
        rankLayout.setVisibility(View.VISIBLE);
    }

    public void goEdit(View v) {
        Intent intent = new Intent(this, MakeListActivity.class);
        startActivity(intent);
    }

    public void goEazyRank(View v) {
        GameClientManager.intentRanking(this, apiClient, GameClientManager.Ranking.Easy);
    }

    public void goNomalRank(View v) {
        GameClientManager.intentRanking(this, apiClient, GameClientManager.Ranking.Normal);
    }

    public void goHardRank(View v) {
        GameClientManager.intentRanking(this, apiClient, GameClientManager.Ranking.Hard);
    }

    public void goOriginalRank(View v){
        GameClientManager.intentRanking(this, apiClient, GameClientManager.Ranking.Original);
    }

    public void goTitle(View v) {
        playEazy.setVisibility(View.VISIBLE);
        playNomal.setVisibility(View.VISIBLE);
        playHard.setVisibility(View.VISIBLE);
        playShare.setVisibility(View.VISIBLE);
        rankLayout.setVisibility(View.INVISIBLE);
    }

    public void goMedal(View v) {
        GameClientManager.intentMedal(this, apiClient);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
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
            return true;
        }
        return false;
    }
}
