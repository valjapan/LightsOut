package com.valkyrie.nabeshimamac.lightsout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ClearActivity extends AppCompatActivity {
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear);
        button = (Button)findViewById(R.id.button38);
    }

    public void goTytle(View v){
        Intent intent = new Intent(this,TytleActivity.class);
        startActivity(intent);
    }
}
