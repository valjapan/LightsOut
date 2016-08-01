package com.valkyrie.nabeshimamac.lightsout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TytleActivity extends AppCompatActivity {
    Button button1,button2;
    TextView textView;

//TODO ここにタイトル点滅を実装させる
//    private Handler mHandler = new Handler();
//    private ScheduledExecutorService mScheduledExecutor;
//    private TextView mLblMeasuring;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tytle);
        button1 = (Button)findViewById(R.id.button39);
        button2 = (Button)findViewById(R.id.button40);
        button1.setVisibility(View.VISIBLE);
        button2.setVisibility(View.VISIBLE);
        textView = (TextView)findViewById(R.id.textView);

    }


    public void help (View v){
        Intent intent = new Intent(this,HelpActivity.class);
        startActivity(intent);
    }

    public void goGame(View v){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
//TODO ここにタイトル点滅を実装させる
//    private void startMeasure() {
//
//
//
//
//        /**
//         * 点滅させたいView
//         * TextViewじゃなくてもよい。
//         */
//        mLblMeasuring = (TextView) findViewById(R.id.textView);
//
//        /**
//         * 第一引数: 繰り返し実行したい処理
//         * 第二引数: 指定時間後に第一引数の処理を開始
//         * 第三引数: 第一引数の処理完了後、指定時間後に再実行
//         * 第四引数: 第二、第三引数の単位
//         *
//         * new Runnable（無名オブジェクト）をすぐに（0秒後に）実行し、完了後1700ミリ秒ごとに繰り返す。
//         * （ただしアニメーションの完了からではない。Handler#postが即時実行だから？？）
//         */
//        mScheduledExecutor = Executors.newScheduledThreadPool(2);
//
//        mScheduledExecutor.scheduleWithFixedDelay(new Runnable() {
//            @Override
//            public void run() {
//                textView.post(animateAlpha());
//            }
//
//
//            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//            private void animateAlpha() {
//
//                // 実行するAnimatorのリスト
//                List<Animator> animatorList = new ArrayList<Animator>();
//
//                // alpha値を0から1へ1000ミリ秒かけて変化させる。
//                ObjectAnimator animeFadeIn = ObjectAnimator.ofFloat(mLblMeasuring, "alpha", 0f, 1f);
//                animeFadeIn.setDuration(1000);
//                // alpha値を1から0へ600ミリ秒かけて変化させる。
//                ObjectAnimator animeFadeOut = ObjectAnimator.ofFloat(mLblMeasuring, "alpha", 1f, 0f);
//                animeFadeOut.setDuration(600);
//
//                // 実行対象Animatorリストに追加。
//                animatorList.add(animeFadeIn);
//                animatorList.add(animeFadeOut);
//
//                final AnimatorSet animatorSet = new AnimatorSet();
//
//                // リストの順番に実行
//                animatorSet.playSequentially(animatorList);
//
//                animatorSet.start();
//            }
//        }, 0, 1700, TimeUnit.MILLISECONDS);
//
//    }


}
