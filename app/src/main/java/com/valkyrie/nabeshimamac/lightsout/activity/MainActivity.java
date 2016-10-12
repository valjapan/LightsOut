package com.valkyrie.nabeshimamac.lightsout.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.valkyrie.nabeshimamac.lightsout.MyApplication;
import com.valkyrie.nabeshimamac.lightsout.R;
import com.valkyrie.nabeshimamac.lightsout.manager.GameClientManager;
import com.valkyrie.nabeshimamac.lightsout.manager.PreferencesManager;
import com.valkyrie.nabeshimamac.lightsout.model.Question;
import com.valkyrie.nabeshimamac.lightsout.model.SharedQuestion;
import com.valkyrie.nabeshimamac.lightsout.view.LightsOutView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * ゲーム画面のActivity
 */
public class MainActivity extends AppCompatActivity implements
        LightsOutView.LightsOutListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
    private static final String KEY_QUESTION_ID = "question_id";
    private static final String KEY_SHARED_QUESTION = "shared_question";

    public static Intent createIntent(Context context, long questionid) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(KEY_QUESTION_ID, questionid);
        return intent;
    }

    public static Intent createIntent(Context context, SharedQuestion sharedQuestion) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(KEY_SHARED_QUESTION, sharedQuestion);
        return intent;
    }

    private LightsOutView lightsOutView;
    private TextView timerTextView, counterTextView , titleTextView, messageTextView,
            totalTextView, timeResultTextView, countResultTextView, titleClearTextView;
    private Button modalButton, returnTitleButton, goMyRankButton;
    private RelativeLayout startLayout, clearLayout;
    private long startedAt;
    private Timer timer;
    private Handler h = new Handler();
    private boolean isPlaying = false;
    private Random random = new Random();
    private GameClientManager.Ranking ranking;
    private List<Point> prePoints;
    private GoogleApiClient apiClient;
    private boolean mIntentInProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //アクションバーの左側（閉じる）部分

        lightsOutView = (LightsOutView) findViewById(R.id.lightsOutView);
        //LightsOutViewのIDの関連付け

        timerTextView = (TextView) findViewById(R.id.timer);
        counterTextView = (TextView) findViewById(R.id.counter);
        totalTextView = (TextView) findViewById(R.id.total);
        timeResultTextView = (TextView) findViewById(R.id.timeResult);
        countResultTextView = (TextView) findViewById(R.id.countResult);
        titleClearTextView = (TextView) findViewById(R.id.titleClear);
        titleTextView = (TextView) findViewById(R.id.title);
        messageTextView = (TextView) findViewById(R.id.messege);
        //TextViewのIDの関連付け


        modalButton = (Button) findViewById(R.id.modalButton);
        returnTitleButton = (Button) findViewById(R.id.retryButton);
        goMyRankButton = (Button) findViewById(R.id.rankButton);
        //ButtonのIDの関連付け


        startLayout = (RelativeLayout) findViewById(R.id.startLayout);
        clearLayout = (RelativeLayout) findViewById(R.id.clearLayout);
        //RelativeLayoutのIDの関連付け

        Typeface gothicAdobe = Typeface.createFromAsset(getAssets(), "AdobeGothicStd-Bold.otf");
        Typeface gothicApple = Typeface.createFromAsset(getAssets(), "AppleSDGothicNeo.ttc");
        Typeface sign = Typeface.createFromAsset(getAssets(), "SignPainter.otf");
        //フォントの呼び出し

        lightsOutView.setOnLightsOutListener(this);
        counterTextView.setText(String.format("%1$02d", 0));
        timerTextView.setText("00:00:00");
        timerTextView.setTextColor(Color.BLACK);
        //timerの表示部分

        startLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        clearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        //startレイアウトとclearレイアウトの表示部分

        timerTextView.setTypeface(gothicAdobe);
        counterTextView.setTypeface(gothicAdobe);
        totalTextView.setTypeface(gothicAdobe);
        messageTextView.setTypeface(gothicApple);
        titleTextView.setTypeface(gothicApple);
        titleClearTextView.setTypeface(sign);
        countResultTextView.setTypeface(gothicAdobe);
        timeResultTextView.setTypeface(gothicAdobe);
        modalButton.setTypeface(gothicAdobe);
        returnTitleButton.setTypeface(gothicAdobe);
        goMyRankButton.setTypeface(gothicAdobe);
        //フォントの変更

        showStartModal();
        //showStartModelを起動

        final int mode = getIntent().getIntExtra("mode", 0);

        prePoints = new ArrayList<>();

        if (getIntent().hasExtra(KEY_QUESTION_ID)) {
            final long questionId = getIntent().getLongExtra(KEY_QUESTION_ID, -1);
            ranking = GameClientManager.Ranking.Original;
            final Question question = new Select().from(Question.class).where("id = ?", questionId).executeSingle();
            loadQuestion(question);
        } else if(getIntent().hasExtra(KEY_SHARED_QUESTION)) {
            final SharedQuestion sharedQuestion = (SharedQuestion) getIntent().getSerializableExtra(KEY_SHARED_QUESTION);
            ranking = GameClientManager.Ranking.Original;
            final Question question = sharedQuestion.toQuestion();
            loadQuestion(question);
        } else {

            if (mode == 0) {
                lightsOutView.setBoardSize(4, 4);
                // 初級
                ranking = GameClientManager.Ranking.Easy;
            } else if (mode == 1) {
                lightsOutView.setBoardSize(5, 5);
                // 中級
                for (int i = 0; i < 8; i++) {
                    prePoints.add(new Point(random.nextInt(5), random.nextInt(5)));
                }
                ranking = GameClientManager.Ranking.Normal;
            } else if (mode == 2) {
                lightsOutView.setBoardSize(6, 6);
                // 上級
                for (int i = 0; i < 15; i++) {
                    prePoints.add(new Point(random.nextInt(6), random.nextInt(6)));
                }
                ranking = GameClientManager.Ranking.Hard;
            }
        }
        loadPrePoints();

        apiClient = ((MyApplication) getApplication()).getGoogleApiClient();
        // 初期設定
        // 各種リスナー登録とGoogleAPIで利用するAPIやスコープの設定

        if (!PreferencesManager.getInstance(this).isTutorialEnd()) {
            Intent intent = new Intent(this, TutorialInformationActivity.class);
            startActivity(intent);
            // チュートリアルが終わってない場合
            // Tutorial画面に移動
            GameClientManager.unlockMedal(apiClient, GameClientManager.Medal.FirstTutorial);
        }

        // SharedPreferencesからMuteかどうかの設定を読み込む
        lightsOutView.setSound(PreferencesManager.getInstance(this).isSound());
        lightsOutView.setColor(PreferencesManager.getInstance(this).getThemeColor());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
        //ツールバーの呼び出し
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_reset:
                reset();
                break;
            //ツールバーのリセット部分
            case R.id.menu_info:
                Intent intent = new Intent(this, TutorialInformationActivity.class);
                startActivity(intent);
                break;
            //ツールバーのチュートリアル部分
        }
        return super.onOptionsItemSelected(item);
        //それらを表示させるための部分
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPlaying) {
            startTimer();
            //途中に何かしらで止めていた時にプレイさせる場所
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTimer();
        //タブやホームに移った時にする場所
    }

    @Override
    public void onButtonTapped(int line, int row, int tapCount) {
        counterTextView.setText(String.format("%1$02d", tapCount));
        //フォーマットによってタップカウントが1から01にするようにした
    }

    @Override
    public void onClearListener() {
        isPlaying = false;
        final int clearCount = PreferencesManager.getInstance(this).addClearCount(ranking);
        GameClientManager.Medal medal = null;

        if (clearCount == 1) {
            // いずれかの難易度の初回クリア(実績)
            if (ranking == GameClientManager.Ranking.Easy) {
                medal = GameClientManager.Medal.FirstEazy;
            } else if (ranking == GameClientManager.Ranking.Normal) {
                medal = GameClientManager.Medal.FirstNomal;
            } else if (ranking == GameClientManager.Ranking.Hard) {
                medal = GameClientManager.Medal.FirstHard;
            } else if (ranking == GameClientManager.Ranking.Original) {
                medal = GameClientManager.Medal.FirstMakePuzzlePlay;
            }

        } else if (clearCount == 10) {
            // いずれかの難易度の10回クリア(実績)
            if (ranking == GameClientManager.Ranking.Easy) {
                medal = GameClientManager.Medal.ProEazy;
            } else if (ranking == GameClientManager.Ranking.Normal) {
                medal = GameClientManager.Medal.ProNomal;
            } else if (ranking == GameClientManager.Ranking.Hard) {
                medal = GameClientManager.Medal.ProHard;
            }
        }
        if (medal != null) {
            GameClientManager.unlockMedal(apiClient, medal);
            //実績解除
        }
        stopTimer();
        showClearModal();
    }

    @Override
    protected void onStart() {
        super.onStart();
        apiClient.registerConnectionCallbacks(this);
        apiClient.registerConnectionFailedListener(this);
        // 画面が表示されるたびに必ず接続させる
        if (!apiClient.isConnected() || !apiClient.hasConnectedApi(Games.API)) {
            apiClient.connect();
            Log.d(TitleActivity.class.getSimpleName(), "Connecting Google play");
        }
        //Googleログインの部分
    }

    @Override
    protected void onStop() {
        apiClient.unregisterConnectionCallbacks(this);
        apiClient.unregisterConnectionFailedListener(this);
        super.onStop();
        //appがバグで落ちた時にバグを送信する
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isPlaying) {
            new AlertDialog.Builder(this)
                    .setTitle("プレイ中の記録は戻りません！")
                    .setMessage("よろしいですか？")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //自動生成されたメソッド・スタブ
                            finish();
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
        return super.onKeyDown(keyCode, event);
    }

    public void goRetry(View v) {
        finish();
    }
    //リトライ(そのまんま)

    public void goRank(View v) {
        GameClientManager.intentRanking(this, apiClient, ranking);
    }
    //ランキングを起動させる

    public void reset() {
        counterTextView.setText("00");
        counterTextView.setTextColor(Color.BLACK);
        lightsOutView.resetGame();
        loadPrePoints();
        //Resetの内容
    }

    private void loadQuestion(Question question) {
        for (int i = 0; i < question.height; i++) {
            for (int j = 0; j < question.width; j++) {
                if (question.board.charAt(i * question.width + j) == '1') {
                    prePoints.add(new Point(i, j));
                }
                //Ranking実装部分
            }
        }
        lightsOutView.setBoardHeight(question.height);
        lightsOutView.setBoardWidth(question.width);
    }

    private void loadPrePoints() {
        for (Point point : prePoints) {
            lightsOutView.checkFlag(point.x, point.y);
        }
        lightsOutView.updateFlags();
        //パネルONかOFFになってるかの確認
    }

    private void showStartModal() {
        startLayout.setVisibility(View.VISIBLE);
        clearLayout.setVisibility(View.INVISIBLE);
        titleTextView.setText("さぁ始めよう！");
        messageTextView.setText("全て反対のパネルにしよう。\nStartでゲーム開始です。");
        modalButton.setText("START");
        modalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playGame();
            }
        });
        //Play開始前の画面
    }

    private void showClearModal() {
        startLayout.setVisibility(View.INVISIBLE);
        clearLayout.setVisibility(View.VISIBLE);
        titleClearTextView.setText("Congratulations!");

        final long nowTime = System.currentTimeMillis();
        long minute = (nowTime - startedAt) / 1000 / 60;
        long second = (nowTime - startedAt) / 1000 % 60;
        long mili = (nowTime - startedAt) % 1000 / 10;

        long totalMinute = minute * 60;
        long totalSecond = second;

        long lightsOutViewTotal = lightsOutView.getTapCount();

        final int total = (int) (totalMinute + totalSecond + lightsOutViewTotal);

        timeResultTextView.setText("Time:  " + String.format("%1$02d:%2$02d:%3$02d", minute, second, mili));
        countResultTextView.setText("Count:    " + String.format("%1$02d", lightsOutView.getTapCount()));
        this.totalTextView.setText("Total:  " + total);

        GameClientManager.submitScore(apiClient, ranking, total);
        //Play開始後の画面
    }

    private void playGame() {
        startLayout.setVisibility(View.INVISIBLE);
        startedAt = System.currentTimeMillis();
        startTimer();
        isPlaying = true;
        //Play中
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
        //Timerの処理部分
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        //Timerを止める時の処理部分
    }

}