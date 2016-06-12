package com.valkyrie.nabeshimamac.lightsout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button [] btn;
    Button btnReset,btnReturn;
    TextView textView;
    private BoardManegerClass bm;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bm = new BoardManegerClass();

        textView = (TextView)findViewById(R.id.textView);
        btn = new Button[36];

        btn[0] = (Button)findViewById(R.id.button);
        btn[1] = (Button)findViewById(R.id.button2);
        btn[2] = (Button)findViewById(R.id.button3);
        btn[3] = (Button)findViewById(R.id.button4);
        btn[4] = (Button)findViewById(R.id.button5);
        btn[5] = (Button)findViewById(R.id.button6);
        btn[6] = (Button)findViewById(R.id.button7);
        btn[7] = (Button)findViewById(R.id.button8);
        btn[8] = (Button)findViewById(R.id.button9);
        btn[9] = (Button)findViewById(R.id.button10);
        btn[10] = (Button)findViewById(R.id.button11);
        btn[11] = (Button)findViewById(R.id.button12);
        btn[12] = (Button)findViewById(R.id.button13);
        btn[13] = (Button)findViewById(R.id.button14);
        btn[14] = (Button)findViewById(R.id.button15);
        btn[15] = (Button)findViewById(R.id.button16);
        btn[16] = (Button)findViewById(R.id.button17);
        btn[17] = (Button)findViewById(R.id.button18);
        btn[18] = (Button)findViewById(R.id.button19);
        btn[19] = (Button)findViewById(R.id.button20);
        btn[20] = (Button)findViewById(R.id.button21);
        btn[21] = (Button)findViewById(R.id.button22);
        btn[22] = (Button)findViewById(R.id.button23);
        btn[23] = (Button)findViewById(R.id.button24);
        btn[24] = (Button)findViewById(R.id.button25);
        btn[25] = (Button)findViewById(R.id.button26);
        btn[26] = (Button)findViewById(R.id.button27);
        btn[27] = (Button)findViewById(R.id.button28);
        btn[28] = (Button)findViewById(R.id.button29);
        btn[29] = (Button)findViewById(R.id.button30);
        btn[30] = (Button)findViewById(R.id.button31);
        btn[31] = (Button)findViewById(R.id.button32);
        btn[32] = (Button)findViewById(R.id.button33);
        btn[33] = (Button)findViewById(R.id.button34);
        btn[34] = (Button)findViewById(R.id.button35);
        btn[35] = (Button)findViewById(R.id.button36);

        btnReset = (Button)findViewById(R.id.button37);
        btnReturn =(Button)findViewById(R.id.button38);

        btnReset.setOnClickListener(this);
        btnReturn.setOnClickListener(this);
        for (int i = 0; i < btn.length; i++){
            btn[i].setOnClickListener(this);
            btn[i].setBackgroundColor(Color.BLUE);
        }
    }

    @Override
    public void onClick(View v){

    }
}
