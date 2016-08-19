package com.valkyrie.nabeshimamac.lightsout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements LightsOutView.LightsOutListener {

    private LightsOutView lightsOutView;
    private TextView timerTextView, counterTextView;
    TextView title, messege;
    Button modalButton;
    RelativeLayout modalLayout;

    //    private android.media.MediaPlayer mp;
    long startedAt;
    Timer timer;
    Handler h = new Handler();//TODO エラーの理由が不明
    boolean isPlaying = false;
    int mode;
    Random random = new Random();
    private List<Point> prePoints;


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

        lightsOutView.setOnLigitsOutListener(this);
        counterTextView.setText(String.format("%1$02d", 0));
        timerTextView.setText("00:00:00");
        timerTextView.setTextColor(Color.BLACK);

        title = (TextView) findViewById(R.id.title);
        messege = (TextView) findViewById(R.id.messege);
        modalButton = (Button) findViewById(R.id.modalButton);
        modalLayout = (RelativeLayout) findViewById(R.id.modalLayout);
        modalLayout.setOnTouchListener(new View.OnTouchListener() {
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
        } else if (mode == 1) {
            // 中級
            for (int i = 0; i < 5; i++) {
                prePoints.add(new Point(random.nextInt(6), random.nextInt(6)));
            }
        } else if (mode == 2) {
            // 上級
            for (int i = 0; i < 10; i++) {
                prePoints.add(new Point(random.nextInt(6), random.nextInt(6)));
            }
        }
        loadPrePoints();
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

    public void reset() { //Resetの内容
        counterTextView.setText("0");
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
        if (tapCount > 28) {
            @ColorInt int pink = ContextCompat.getColor(this, R.color.colorPinkOriginal);
            counterTextView.setTextColor(pink);
        }
    }

    @Override
    public void onClearListener() {
        stopTimer();
        showClearModal();
//        mp.stop();
    }

    private void showStartModal() {
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
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("クリア！");
//        builder.setMessage("Congratulations!!");
//        builder.setPositiveButton("次へ", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                Intent intent = new Intent(MainActivity.this, ClearActivity.class);
//                startActivity(intent);
//                startTimer();
//            }
//        });
//        builder.show();
        Log.d("check", "class");
        modalLayout.setVisibility(View.VISIBLE);
        title.setText("クリア！");
        messege.setText("Congratulations!!");
        modalButton.setText("NEXT");
        modalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ClearActivity.class);
                startActivity(intent);
            }
        });
    }

    private void playGame() {
        modalLayout.setVisibility(View.INVISIBLE);
//                mp.start();
        startedAt = System.currentTimeMillis();
        startTimer();
        isPlaying = true;
        //TODO タイマーの実装をさせること
    }

    public void startTimer() {
        if (timer != null) {
            timer.cancel();
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
    //TODO  Pauseから戻ってきた時にBGMを流れさせること
}

