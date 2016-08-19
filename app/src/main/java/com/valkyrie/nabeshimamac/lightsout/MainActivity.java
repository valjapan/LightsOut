package com.valkyrie.nabeshimamac.lightsout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements LightsOutView.LightsOutListener {

    private LightsOutView lightsOutView;
    private TextView timerTextView, counterTextView;
    private android.media.MediaPlayer mp;
    long startedAt;
    Timer timer;
    Handler h = new Handler();//TODO エラーの理由が不明

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lightsOutView = (LightsOutView) findViewById(R.id.lightsOutView);
        timerTextView = (TextView) findViewById(R.id.timer);
        counterTextView = (TextView) findViewById(R.id.counter);

        lightsOutView.setOnLigitsOutListener(this);
        counterTextView.setText(String.format("%1$02d", 0));
        timerTextView.setText("00:00:00");
        timerTextView.setTextColor(Color.BLACK);

        mp = android.media.MediaPlayer.create(this, R.raw.bgm01);
        //TODO リピート機能を導入させること
        playGame();

        int mode = 1;
        if (mode == 0) {
            // 初級
        } else if(mode == 1) {
            // 中級
            lightsOutView.checkFlag(1, 1);
            lightsOutView.checkFlag(5, 5);
            lightsOutView.updateFlags();
        } else if(mode == 2) {
            // 上級
        }
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

    public void startTimer() {
        if (timer != null) {
            timer.cancel();
        }
        startedAt = System.currentTimeMillis();
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

    public void reset() { //Resetの内容
        counterTextView.setText("0");
        counterTextView.setTextColor(Color.BLACK);
        lightsOutView.resetGame();
    }

    public void onPause() {
        super.onPause();
        mp.stop();
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
        mp.stop();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("クリア！");
        builder.setMessage("Congratulations!!");
        builder.setPositiveButton("次へ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(MainActivity.this, ClearActivity.class);
                startActivity(intent);
            }
        });
        builder.show();
        Log.d("check", "class");
    }

    private void playGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("さぁ始めよう！");
        builder.setMessage("全て赤いパネルにしてください。Startでゲーム開始です。(BGMが流れます)");
        builder.setPositiveButton("Start", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mp.start();
                startTimer();
                //TODO タイマーの実装をさせること
            }
        });
        builder.show();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    //TODO  Pauseから戻ってきた時にBGMを流れさせること
}

