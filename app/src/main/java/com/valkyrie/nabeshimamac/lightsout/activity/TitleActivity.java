package com.valkyrie.nabeshimamac.lightsout.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.Uri;
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

/**
 * title画面のActivity
 */
public class TitleActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    TextView textView, versiontextView;
    ImageView googlePlay, editButton, shareTwitter;
    Button playEazy, playNomal, playHard;

    LinearLayout modeLayout;
    RelativeLayout rankLayout;

    private GoogleApiClient apiClient;
    private boolean mIntentInProgress;

    private final int TWITTER_ID = 0;
    private final String[] sharePackages = {"com.twitter.android"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        modeLayout = (LinearLayout) findViewById(R.id.modeLayout);
        rankLayout = (RelativeLayout) findViewById(R.id.rankLayout);

        googlePlay = (ImageView) findViewById(R.id.googleGame);
        editButton = (ImageView) findViewById(R.id.EditButton);
        shareTwitter = (ImageView) findViewById(R.id.shareTwitter);

        textView = (TextView) findViewById(R.id.textView);
        versiontextView = (TextView) findViewById(R.id.versionName);
        textView.setText("Lights Out");

        playEazy = (Button) findViewById(R.id.PlayEazy);
        playNomal = (Button) findViewById(R.id.PlayNomal);
        playHard = (Button) findViewById(R.id.PlayHard);

        modeLayout.setVisibility(View.VISIBLE);
        rankLayout.setVisibility(View.INVISIBLE);
        apiClient = ((MyApplication) getApplication()).getGoogleApiClient();


        versiontextView.setText("v" + BuildConfig.VERSION_NAME);

            findViewById(R.id.shareTwitter).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isShareAppInstall(TWITTER_ID)) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.setPackage(sharePackages[TWITTER_ID]);
                        intent.setType("image/png");
                        intent.putExtra(Intent.EXTRA_TEXT,
                                "新感覚シンプルパズルゲーム【LightsOut】。" +
                                        "\nルール は簡単、押したパネルとその上下左右のボタンの色が反転する。" +
                                        "全てのパネルを水色からピンクにすればゲームクリアだ。" +
                                        "\n君もチャレンジしてみないか。 #LightsOutGame" +
                                        "\nhttps://play.google.com/store/apps/details?id=com.valkyrie.nabeshimamac.lightsout");
                        startActivity(intent);
                    } else {
                        shareAppDl(TWITTER_ID);
                    }
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
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("mode", 0);
        startActivity(intent);
    }

    public void goNomal(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("mode", 1);
        startActivity(intent);
    }

    public void goHard(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("mode", 2);
        startActivity(intent);
    }

    public void googleGame(View v) {
        modeLayout.setVisibility(View.VISIBLE);
        rankLayout.setVisibility(View.VISIBLE);
        playEazy.setVisibility(View.INVISIBLE);
        playNomal.setVisibility(View.INVISIBLE);
        playHard.setVisibility(View.INVISIBLE);
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

    public void goTitle(View v) {
        modeLayout.setVisibility(View.VISIBLE);
        rankLayout.setVisibility(View.INVISIBLE);
        playEazy.setVisibility(View.VISIBLE);
        playNomal.setVisibility(View.VISIBLE);
        playHard.setVisibility(View.VISIBLE);
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


    // アプリがインストールされているかチェック
    private Boolean isShareAppInstall(int shareId) {
        try {
            PackageManager pm = getPackageManager();
            pm.getApplicationInfo(sharePackages[shareId], PackageManager.GET_META_DATA);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    // アプリが無かったのでGooglePalyに飛ばす
    private void shareAppDl(int shareId) {
        Uri uri = Uri.parse("market://details?id=" + sharePackages[shareId]);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
