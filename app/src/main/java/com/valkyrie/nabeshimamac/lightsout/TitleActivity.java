package com.valkyrie.nabeshimamac.lightsout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TitleActivity extends AppCompatActivity {

    Button button1,button2,button3,button4,button6,eazyRank,nomalRank,hardRank;
    TextView textView;
    ImageView googlePlay;

    LinearLayout modeLayout;
    RelativeLayout rankLayout;
//TODO ここにタイトル点滅を実装させる
//    private Handler mHandler = new Handler();
//    private ScheduledExecutorService mScheduledExecutor;
//    private TextView mLblMeasuring;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        button2 = (Button)findViewById(R.id.PlayEazy);
        button3 = (Button)findViewById(R.id.PlayNomal);
        button4 = (Button)findViewById(R.id.PlayHard);
        eazyRank = (Button)findViewById(R.id.rankEazy);
        nomalRank = (Button)findViewById(R.id.rankNomal);
        hardRank = (Button)findViewById(R.id.rankHard);

        modeLayout = (LinearLayout)findViewById(R.id.modeLayout);
        rankLayout = (RelativeLayout) findViewById(R.id.rankLayout);

        googlePlay = (ImageView)findViewById(R.id.googleGame);

        textView = (TextView)findViewById(R.id.textView);
        textView.setText("Lights Out");

        modeLayout.setVisibility(View.VISIBLE);
        rankLayout.setVisibility(View.INVISIBLE);

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

    public void googleGame(View v){
        modeLayout.setVisibility(View.INVISIBLE);
        rankLayout.setVisibility(View.VISIBLE);
    }

    public void goEazyRank(View v){

    }

    public void goNomalRank (View v){

    }

    public void goHardRank(View v){

    }

    public void goTitle(View v){
        modeLayout.setVisibility(View.VISIBLE);
        rankLayout.setVisibility(View.INVISIBLE);
    }

}
