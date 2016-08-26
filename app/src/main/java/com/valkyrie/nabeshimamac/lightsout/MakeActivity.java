package com.valkyrie.nabeshimamac.lightsout;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.activeandroid.query.Select;

import java.util.Date;

public class MakeActivity extends AppCompatActivity {
    private LightsOutView lightsOutEachView;
    EditText editText;
    TextView detailText;
    Spinner spinner;
    long questionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("新規問題作成・編集");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        detailText = (TextView)findViewById(R.id.detaleTextView);

        spinner = (Spinner)findViewById(R.id.spinner);
        final ArrayAdapter spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.size_spinner));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        lightsOutEachView = (LightsOutView) findViewById(R.id.lightsOutView2);
        lightsOutEachView.setMode(LightsOutView.MODE_MAKE);
        lightsOutEachView.setOnLigitsOutListener(new LightsOutView.LightsOutListener() {
            @Override
            public void onButtonTapped(int line, int row, int tapCount) {
                updateDetailsText();
            }
            @Override
            public void onClearListener() {
            }
        });
        editText = (EditText) findViewById(R.id.titleEditText);

        questionId = getIntent().getLongExtra("question_id", -1);
        if (questionId == -1) {
            spinner.setEnabled(true);
            // 新規作成
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position == 0) {
                        lightsOutEachView.setBoardSize(5);
                    } else {
                        lightsOutEachView.setBoardSize(6);
                    }
                    updateDetailsText();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            // 編集
            Question question = new Select().from(Question.class).where("id = ?", questionId).executeSingle();
            editText.setText("" + question.title);
            spinner.setEnabled(false);
            if (question.size == 5) {
                spinner.setSelection(0, false);
            } else {
                spinner.setSelection(1, false);
            }
            lightsOutEachView.setBoardSize(question.size);
            lightsOutEachView.setFlagsFromString(question.board);
            lightsOutEachView.updateFlags();
        }
        updateDetailsText();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_make, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                save();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void save() {
        Question question = new Question();
        if (TextUtils.isEmpty(editText.getText())){
            question.title = "無題";
        }else {
            question.title = editText.getText().toString();
        }
        // 現在日時の取得
        question.board = lightsOutEachView.getFlagsToString();
        question.size = lightsOutEachView.getBoardSize();
        question.createdAt = new Date();
        question.save();
        setResult(RESULT_OK);
        finish();
    }

    private void updateDetailsText() {
        String board = lightsOutEachView.getFlagsToString();
        int emptyCount = 0;
        for (int i = 0; i < board.length(); i++) {
            if (board.charAt(i) == '0') {
                emptyCount++;
            }
        }
        detailText.setText("盤面のサイズ : "+ lightsOutEachView.getBoardSize() +"  空のマス : "+ emptyCount);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK){
            new AlertDialog.Builder(this)
                    .setTitle("注意")
                    .setMessage("戻ってしまうとパズルの編集が消えてしまいます！")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 自動生成されたメソッド・スタブ
                            MakeActivity.this.finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //  自動生成されたメソッド・スタブ
                        }
                    })
                    .show();

            return true;
        }
        return false;
    }


}
