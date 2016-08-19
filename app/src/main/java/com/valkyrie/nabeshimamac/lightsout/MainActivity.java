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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button[][] btn;
    TextView timerTextView, counterTextView;
    boolean[][] flag;
    int count;
    private android.media.MediaPlayer mp;
    Tap tapInstance;
    long startedAt;
    Timer timer;
    Handler h = new Handler();//TODO エラーの理由が不明



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerTextView = (TextView) findViewById(R.id.timer);
        counterTextView = (TextView) findViewById(R.id.counter);


        btn = new Button[6][6];
        flag = new boolean[6][6];

        btn[0][0] = (Button) findViewById(R.id.button);
        btn[0][1] = (Button) findViewById(R.id.button2);
        btn[0][2] = (Button) findViewById(R.id.button3);
        btn[0][3] = (Button) findViewById(R.id.button4);
        btn[0][4] = (Button) findViewById(R.id.button5);
        btn[0][5] = (Button) findViewById(R.id.button6);
        btn[1][0] = (Button) findViewById(R.id.button7);
        btn[1][1] = (Button) findViewById(R.id.button8);
        btn[1][2] = (Button) findViewById(R.id.button9);
        btn[1][3] = (Button) findViewById(R.id.button10);
        btn[1][4] = (Button) findViewById(R.id.button11);
        btn[1][5] = (Button) findViewById(R.id.button12);
        btn[2][0] = (Button) findViewById(R.id.button13);
        btn[2][1] = (Button) findViewById(R.id.button14);
        btn[2][2] = (Button) findViewById(R.id.button15);
        btn[2][3] = (Button) findViewById(R.id.button16);
        btn[2][4] = (Button) findViewById(R.id.button17);
        btn[2][5] = (Button) findViewById(R.id.button18);
        btn[3][0] = (Button) findViewById(R.id.button19);
        btn[3][1] = (Button) findViewById(R.id.button20);
        btn[3][2] = (Button) findViewById(R.id.button21);
        btn[3][3] = (Button) findViewById(R.id.button22);
        btn[3][4] = (Button) findViewById(R.id.button23);
        btn[3][5] = (Button) findViewById(R.id.button24);
        btn[4][0] = (Button) findViewById(R.id.button25);
        btn[4][1] = (Button) findViewById(R.id.button26);
        btn[4][2] = (Button) findViewById(R.id.button27);
        btn[4][3] = (Button) findViewById(R.id.button28);
        btn[4][4] = (Button) findViewById(R.id.button29);
        btn[4][5] = (Button) findViewById(R.id.button30);
        btn[5][0] = (Button) findViewById(R.id.button31);
        btn[5][1] = (Button) findViewById(R.id.button32);
        btn[5][2] = (Button) findViewById(R.id.button33);
        btn[5][3] = (Button) findViewById(R.id.button34);
        btn[5][4] = (Button) findViewById(R.id.button35);
        btn[5][5] = (Button) findViewById(R.id.button36);

        count = 0;
        counterTextView.setText(String.format("%1$02d", count));
        timerTextView.setTextColor(Color.BLACK);


        @ColorInt int blue = ContextCompat.getColor(this, R.color.colorBlueOriginal);

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                btn[i][j].setOnClickListener(this);
                btn[i][j].setBackgroundColor(blue);
                flag[i][j] = false;
            }
        }
        tapInstance = new Tap(this.getApplicationContext());


        mp = android.media.MediaPlayer.create(this, R.raw.bgm01);
        //TODO リピート機能を導入させること

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

        timerTextView.setText("00:00:00");


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_reset:
                reset(0,0);
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


    @Override
    public void onClick(View v) {

        @ColorInt int pink = ContextCompat.getColor(this, R.color.colorPinkOriginal);
        count = count + 1;
        counterTextView.setText(String.format("%1$02d", count));
        if (count > 28) {
            counterTextView.setTextColor(pink);
        }


        switch (v.getId()) {
            case R.id.button:
                check(0, 0);
                tapInstance.play();
                break;
            case R.id.button2:
                check(0, 1);
                tapInstance.play();
                break;
            case R.id.button3:
                check(0, 2);
                tapInstance.play();
                break;
            case R.id.button4:
                check(0, 3);
                tapInstance.play();
                break;
            case R.id.button5:
                check(0, 4);
                tapInstance.play();
                break;
            case R.id.button6:
                check(0, 5);
                tapInstance.play();
                break;
            case R.id.button7:
                check(1, 0);
                tapInstance.play();
                break;
            case R.id.button8:
                check(1, 1);
                tapInstance.play();
                break;
            case R.id.button9:
                check(1, 2);
                tapInstance.play();
                break;
            case R.id.button10:
                check(1, 3);
                tapInstance.play();
                break;
            case R.id.button11:
                check(1, 4);
                tapInstance.play();
                break;
            case R.id.button12:
                check(1, 5);
                tapInstance.play();
                break;
            case R.id.button13:
                check(2, 0);
                tapInstance.play();
                break;
            case R.id.button14:
                check(2, 1);
                tapInstance.play();
                break;
            case R.id.button15:
                check(2, 2);
                tapInstance.play();
                break;
            case R.id.button16:
                check(2, 3);
                tapInstance.play();
                break;
            case R.id.button17:
                check(2, 4);
                tapInstance.play();
                break;
            case R.id.button18:
                check(2, 5);
                tapInstance.play();
                break;
            case R.id.button19:
                check(3, 0);
                tapInstance.play();
                break;
            case R.id.button20:
                check(3, 1);
                tapInstance.play();
                break;
            case R.id.button21:
                check(3, 2);
                tapInstance.play();
                break;
            case R.id.button22:
                check(3, 3);
                tapInstance.play();
                break;
            case R.id.button23:
                check(3, 4);
                tapInstance.play();
                break;
            case R.id.button24:
                check(3, 5);
                tapInstance.play();
                break;
            case R.id.button25:
                check(4, 0);
                tapInstance.play();
                break;
            case R.id.button26:
                check(4, 1);
                tapInstance.play();
                break;
            case R.id.button27:
                check(4, 2);
                tapInstance.play();
                break;
            case R.id.button28:
                check(4, 3);
                tapInstance.play();
                break;
            case R.id.button29:
                check(4, 4);
                tapInstance.play();
                break;
            case R.id.button30:
                check(4, 5);
                tapInstance.play();
                break;
            case R.id.button31:
                check(5, 0);
                tapInstance.play();
                break;
            case R.id.button32:
                check(5, 1);
                tapInstance.play();
                break;
            case R.id.button33:
                check(5, 2);
                tapInstance.play();
                break;
            case R.id.button34:
                check(5, 3);
                tapInstance.play();
                break;
            case R.id.button35:
                check(5, 4);
                tapInstance.play();
                break;
            case R.id.button36:
                check(5, 5);
                tapInstance.play();
                break;

        }
    }

    public void check(int line, int row) {
        checkColor(line, row);
        if (line > 0) {
            checkColor(line - 1, row);
        }
        if (line < 5) {
            checkColor(line + 1, row);
        }
        if (row > 0) {
            checkColor(line, row - 1);
        }
        if (row < 5) {
            checkColor(line, row + 1);
        }

        boolean isClear = true;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                isClear = isClear && flag[i][j];
                if (flag[i][j]) {
                    btn[i][j].setBackground(getDrawable(R.drawable.red_off_view));
                } else {
                    btn[i][j].setBackground(getDrawable(R.drawable.blue_off_view));
                }
            }
        }
        setJustColor(line, row);

        Log.d("check", "isClear: " + isClear);
        if (isClear) {
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

    }


    public void checkColor(int line, int row) {
        flag[line][row] = !flag[line][row];
        //TODO 全てが反転色になったらクリアを実装させること
    }


    public void setJustColor(int line, int row) {
        if (flag[line][row]) {
            btn[line][row].setBackground(getDrawable(R.drawable.red_on_view));
        } else {
            btn[line][row].setBackground(getDrawable(R.drawable.blue_on_view));
        }
    }

    public void reset(int line, int row) { //Resetの内容
        @ColorInt int blue = ContextCompat.getColor(this, R.color.colorBlueOriginal);

        for (line = 0; line < 6; line++) {
            for (row = 0; row < 6; row++) {
                btn[line][row].setOnClickListener(this);
                btn[line][row].setBackgroundColor(blue);
                flag[line][row] = false;
                count = 0;
                counterTextView.setText(count + "0");
                counterTextView.setTextColor(Color.BLACK);
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("LifeCycle", "onDestroy");
    }

    public void onPause() {
        super.onPause();
        mp.stop();
        stopTimer();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    //TODO  Pauseから戻ってきた時にBGMを流れさせること
}

