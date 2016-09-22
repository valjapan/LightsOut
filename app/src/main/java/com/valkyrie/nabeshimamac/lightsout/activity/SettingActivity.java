package com.valkyrie.nabeshimamac.lightsout.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.valkyrie.nabeshimamac.lightsout.R;

public class SettingActivity extends AppCompatActivity {
    public boolean onMuteBoolean;
    private ImageView muteImageView;
    private TextView muteInfo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        muteImageView = (ImageView)findViewById(R.id.muteImageView);
        muteInfo = (TextView)findViewById(R.id.muteInformation);
        muteInfo.setText("タップ音をミュートにすることができます。");


        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //アクションバーの左側（閉じる）部分

        onMuteBoolean = false;

        readMute();


    }

    private void readMute(){

        SharedPreferences data = getSharedPreferences("onMuteShared", MODE_PRIVATE);
        Boolean onMute = data.getBoolean("onMute",false);

        if (onMute == false){
            onMuteBoolean = false;
            muteImageView.setBackgroundResource(R.drawable.ic_volume_up_black_48dp);
            muteInfo.setText("タップ音をミュートにすることができます。");
        }else {
            onMuteBoolean = true;
            muteImageView.setBackgroundResource(R.drawable.ic_volume_off_black_48dp);
            muteInfo.setText("タップ音はミュートになっています.。");
        }
    }

    public void mute(View v){
        SharedPreferences data = getSharedPreferences("onMuteShared", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        if(onMuteBoolean == true){
            muteImageView.setBackgroundResource(R.drawable.ic_volume_up_black_48dp);
            muteInfo.setText("タップ音をミュートにすることができます。");
            onMuteBoolean = false;
            editor.putBoolean("onMute", false);
        }else{
            muteImageView.setBackgroundResource(R.drawable.ic_volume_off_black_48dp);
            muteInfo.setText("タップ音はミュートになっています.。");
            onMuteBoolean = true;
            editor.putBoolean("onMute", true);
        }
        editor.commit();
    }

}
