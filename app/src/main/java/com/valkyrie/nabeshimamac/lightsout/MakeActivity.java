package com.valkyrie.nabeshimamac.lightsout;

import android.content.DialogInterface;
import android.net.Uri;
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
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Date;

public class MakeActivity extends AppCompatActivity{
    private LightsOutView lightsOutEachView;
    EditText editText;
    TextView detailText;
    Spinner spinner;
    long questionId;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("New and Edited");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //ツールバーのプロパティ

        detailText = (TextView) findViewById(R.id.detaleTextView);
        //盤面の情報のテキスト

        spinner = (Spinner) findViewById(R.id.spinner);
        final ArrayAdapter spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.size_spinner));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        //盤面入れ替えのスピナーの部分

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
                    if (position == 0) {
                        lightsOutEachView.setBoardHeight(5);
                        lightsOutEachView.setBoardWidth(5);
                    } else if (position == 1){
                        lightsOutEachView.setBoardHeight(6);
                        lightsOutEachView.setBoardWidth(6);
                    } else if (position == 2){
                        lightsOutEachView.setBoardHeight(5);
                        lightsOutEachView.setBoardWidth(4);
                    }
                    updateDetailsText();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } else {
            // 編集（新規じゃない）
            Question question = new Select().from(Question.class).where("id = ?", questionId).executeSingle();
            editText.setText("" + question.title);
            spinner.setEnabled(false);
            if (question.size == 5) {
                spinner.setSelection(0, false);
            } else if(question.size == 6) {
                spinner.setSelection(1, false);
            } else {
                spinner.setSelection(2, false);
            }
            lightsOutEachView.setBoardHeight(question.size);
            lightsOutEachView.setFlagsFromString(question.board);
            lightsOutEachView.updateFlags();
        }
        updateDetailsText();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_make, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //ツールバーの右側のアイコン

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                save();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //セーブアイコンのみ

    private void save() {
        Question question = new Question();
        if (TextUtils.isEmpty(editText.getText())) {
            question.title = "No Name";
            //もしtitleに何も入力しないでセーブしたら『No Name』と入る
        } else {
            question.title = editText.getText().toString();
            //titleに何か入っていたらString型で配置
        }
        // 現在日時の取得
        question.board = lightsOutEachView.getFlagsToString();
        question.size = lightsOutEachView.getBoardHeight();
        question.size = lightsOutEachView.getBoardWidth();
        question.createdAt = new Date();
        question.save();
        setResult(RESULT_OK);
        finish();
    }
    //save部分

    private void updateDetailsText() {
        String board = lightsOutEachView.getFlagsToString();
        int emptyCount = 0;
        for (int i = 0; i < board.length(); i++) {
            if (board.charAt(i) == '0') {
                emptyCount++;
            }
        }
        //ListViewに表示させる内容
        detailText.setText("盤面のサイズ : " + lightsOutEachView.getBoardHeight()+ "×" +lightsOutEachView.getBoardWidth() + "  空のマス : " + emptyCount);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
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
                            // 自動生成されたメソッド・スタブ
                        }
                    })
                    .show();

            return true;
        }
        return false;
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Make Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.valkyrie.nabeshimamac.lightsout/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Make Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.valkyrie.nabeshimamac.lightsout/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

}
