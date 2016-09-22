package com.valkyrie.nabeshimamac.lightsout.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.valkyrie.nabeshimamac.lightsout.R;

public class SettingActivity extends AppCompatActivity {
    private boolean onMute;
    private ImageView muteImageView;
    private TextView muteInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        muteImageView = (ImageView)findViewById(R.id.muteImageView);
        muteInfo = (TextView)findViewById(R.id.muteInfomation);
        muteInfo.setText("タップ音をミュートにすることができます。");
        onMute =false;
    }
}
