package com.valkyrie.nabeshimamac.lightsout;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements LightsOutView.LightsOutListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private static final String EASY_RANKING_ID = "CgkIxf6K1KAMEAIQAQ";
    private static final String NOMAL_RANKING_ID = "CgkIxf6K1KAMEAIQAw";
    private static final String HARD_RANKING_ID = "CgkIxf6K1KAMEAIQBA";


    private LightsOutView lightsOutView;
    private TextView timerTextView, counterTextView;
    TextView title, messege, total, timeResult, countResult, titleClear;
    Button modalButton;
    RelativeLayout startLayout, clearLayout;

    //    private android.media.MediaPlayer mp;
    long startedAt;
    Timer timer;
    Handler h = new Handler();//TODO エラーの理由が不明
    boolean isPlaying = false;
    int mode;
    Random random = new Random();
    private String rankingId;
    private List<Point> prePoints;

    private GoogleApiClient apiClient;
    private boolean mIntentInProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lightsOutView = (LightsOutView) findViewById(R.id.lightsOutView);
        timerTextView = (TextView) findViewById(R.id.timer);
        counterTextView = (TextView) findViewById(R.id.counter);
        total = (TextView) findViewById(R.id.total);
        timeResult = (TextView) findViewById(R.id.timeResult);
        countResult = (TextView) findViewById(R.id.countResult);
        titleClear = (TextView) findViewById(R.id.titleClear);

        lightsOutView.setOnLigitsOutListener(this);
        counterTextView.setText(String.format("%1$02d", 0));
        timerTextView.setText("00:00:00");
        timerTextView.setTextColor(Color.BLACK);

        title = (TextView) findViewById(R.id.title);
        messege = (TextView) findViewById(R.id.messege);
        modalButton = (Button) findViewById(R.id.modalButton);
        startLayout = (RelativeLayout) findViewById(R.id.startLayout);
        startLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        clearLayout = (RelativeLayout) findViewById(R.id.clearLayout);
        clearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
//        mp = android.media.MediaPlayer.create(this, R.raw.bgm01);
        //TODO リピート機能を導入させること

        showStartModal();

        mode = getIntent().getIntExtra("mode", 0);
        prePoints = new ArrayList<>();
        if (mode == 0) {
            // 初級
            rankingId = EASY_RANKING_ID;
        } else if (mode == 1) {
            // 中級
            for (int i = 0; i < 5; i++) {
                prePoints.add(new Point(random.nextInt(6), random.nextInt(6)));
                rankingId = NOMAL_RANKING_ID;
            }
        } else if (mode == 2) {
            // 上級
            for (int i = 0; i < 10; i++) {
                prePoints.add(new Point(random.nextInt(6), random.nextInt(6)));
                rankingId = HARD_RANKING_ID;
            }
        }
        loadPrePoints();

        // 初期設定
        // 各種リスナー登録とGoogleAPIで利用するAPIやスコープの設定
        apiClient = ((MyApplication) getApplication()).getGoogleApiClient();
    }

    private void loadPrePoints() {
        for (Point point : prePoints) {
            lightsOutView.checkFlag(point.x, point.y);
        }
        lightsOutView.updateFlags();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_reset:
                reset();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goRetly(View v){
        finish();
    }

    public void goRank(View v) {
        if (apiClient.isConnected()) {
            startActivityForResult(
                    Games.Leaderboards.getLeaderboardIntent(apiClient, rankingId/* 見たいリーダーボードのID */),
                    200/*REQUEST_CODE*/
            );
        }
    }

    public void reset() { //Resetの内容
        counterTextView.setText("00");
        counterTextView.setTextColor(Color.BLACK);
        lightsOutView.resetGame();
        loadPrePoints();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPlaying) {
            startTimer();
        }
    }

    public void onPause() {
        super.onPause();
//        mp.stop();
        stopTimer();
    }

    @Override
    public void onButtonTapped(int line, int row, int tapCount) {
        counterTextView.setText(String.format("%1$02d", tapCount));
    }

    @Override
    public void onClearListener() {
        isPlaying = false;
        stopTimer();
        showClearModal();
//        mp.stop();
    }

    private void showStartModal() {
        startLayout.setVisibility(View.VISIBLE);
        clearLayout.setVisibility(View.INVISIBLE);
        title.setText("さぁ始めよう！");
        messege.setText("全て赤いパネルにしてください。\nStartでゲーム開始です。");
        modalButton.setText("START");
        modalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playGame();
            }
        });
    }

    private void showClearModal() {
        if (apiClient.isConnected()) {
            Games.Leaderboards.submitScore(apiClient, rankingId/* リーダーボードのID */, lightsOutView.getTapCount()/* 送信する値 */);
        }
        startLayout.setVisibility(View.INVISIBLE);
        clearLayout.setVisibility(View.VISIBLE);
        titleClear.setText("Congratulations");

        final long nowTime = System.currentTimeMillis();
        long minute = (nowTime - startedAt) / 1000 / 60;
        long second = (nowTime - startedAt) / 1000 % 60;
        long mili = (nowTime - startedAt) % 1000 / 10;
        timeResult.setText("Time:  " + String.format("%1$02d:%2$02d:%3$02d", minute, second, mili));
        countResult.setText("Count:    " + String.format("%1$02d", lightsOutView.getTapCount()));
    }

    private void playGame() {
        startLayout.setVisibility(View.INVISIBLE);

//                mp.start();
        startedAt = System.currentTimeMillis();
        startTimer();
        isPlaying = true;
        //TODO タイマーの実装をさせること
    }

    public void startTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        timer = new Timer();
           timer.schedule(new TimerTask() {
                @Override
            public void run() {
                final long nowTime = System.currentTimeMillis();
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        long minute = (nowTime - startedAt) / 1000 / 60;
                        long second = (nowTime - startedAt) / 1000 % 60;
                        long mili = (nowTime - startedAt) % 1000 / 10;
                        timerTextView.setText(String.format("%1$02d:%2$02d:%3$02d", minute, second, mili));
                    }
                });
            }
        }, 0, 10);

    }//TODO Timerの実装部分

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        apiClient.registerConnectionCallbacks(this);
        apiClient.registerConnectionFailedListener(this);
        // 今回は画面が表示されるたびに必ず接続させる
        if (apiClient.hasConnectedApi(Games.API)) {
            return;
        } else if (!apiClient.isConnected()) {
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
        if (requestCode == 100) {
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
                connectionResult.startResolutionForResult(this, 100);
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

    //TODO  Pauseから戻ってきた時にBGMを流れさせること
}

