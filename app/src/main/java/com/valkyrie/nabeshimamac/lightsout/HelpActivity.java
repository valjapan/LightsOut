package com.valkyrie.nabeshimamac.lightsout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HelpActivity extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        setTitle("ルール説明");
        button = (Button)findViewById(R.id.button41);
    }

    public void tytle(View v){
        finish();
    }
}
