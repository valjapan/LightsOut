package com.valkyrie.nabeshimamac.lightsout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;

public class TitleActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    TextView textView ,versiontextView;
    ImageView googlePlay ,editButton;

    LinearLayout modeLayout;
    RelativeLayout rankLayout;
//    private Handler mHandler = new Handler();
//    private ScheduledExecutorService mScheduledExecutor;
//    private TextView mLblMeasuring;

    private GoogleApiClient apiClient;
    private boolean mIntentInProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        modeLayout = (LinearLayout) findViewById(R.id.modeLayout);
        rankLayout = (RelativeLayout) findViewById(R.id.rankLayout);

        googlePlay = (ImageView) findViewById(R.id.googleGame);
        editButton = (ImageView) findViewById(R.id.EditButton);

        textView = (TextView) findViewById(R.id.textView);
        versiontextView = (TextView)findViewById(R.id.versionName);
        textView.setText("Lights Out");

        modeLayout.setVisibility(View.VISIBLE);
        rankLayout.setVisibility(View.INVISIBLE);
        apiClient = ((MyApplication) getApplication()).getGoogleApiClient();


        versiontextView.setText("v" + BuildConfig.VERSION_NAME);
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
    }

    public void goEdit(View v){
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
    }

    public void goMedal(View v) {
        GameClientManager.intentMedal(this, apiClient);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setTitle("アプリケーションの終了")
                    .setMessage("アプリケーションを終了してよろしいですか？")
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
