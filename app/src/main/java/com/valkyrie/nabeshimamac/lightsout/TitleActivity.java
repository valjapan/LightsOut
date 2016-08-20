package com.valkyrie.nabeshimamac.lightsout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TitleActivity extends AppCompatActivity {
    Button button1,button2,button3,button4,button6;
    TextView textView;
    ImageView googlePlay;

//TODO ここにタイトル点滅を実装させる
//    private Handler mHandler = new Handler();
//    private ScheduledExecutorService mScheduledExecutor;
//    private TextView mLblMeasuring;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tytle);
        button1 = (Button)findViewById(R.id.Help);
        button2 = (Button)findViewById(R.id.PlayEazy);
        button3 = (Button)findViewById(R.id.PlayNomal);
        button4 = (Button)findViewById(R.id.PlayHard);
        button6 = (Button)findViewById(R.id.GoRank);

        googlePlay = (ImageView)findViewById(R.id.googleGame);

        textView = (TextView)findViewById(R.id.textView);
        textView.setText("Lights Out");
    }

    public void help (View v){
        Intent intent = new Intent(this,HelpActivity.class);
        startActivity(intent);
    }

    public void goEazy(View v){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("mode",0);
        startActivity(intent);
    }

    public void goNomal(View v){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("mode",1);
        startActivity(intent);
    }

    public void goHard(View v){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("mode",2);
        startActivity(intent);
    }

    public void goRank(View v){
//        Intent intent = new Intent(this,)
//        startActivity(intent);
//        TODO ランクようのランキングレイアウトを作成させること
    }

    public void googleGame(View v){

    }



}
