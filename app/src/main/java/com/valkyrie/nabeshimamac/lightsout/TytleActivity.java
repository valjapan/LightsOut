package com.valkyrie.nabeshimamac.lightsout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TytleActivity extends AppCompatActivity {
    Button button1,button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tytle);
        button1 = (Button)findViewById(R.id.button39);
        button2 = (Button)findViewById(R.id.button40);
    }


    public void help (View v){
        Intent intent = new Intent(this,HelpActivity.class);
        startActivity(intent);
    }

    public void goGame(View v){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }


}
