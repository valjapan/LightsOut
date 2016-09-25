package com.valkyrie.nabeshimamac.lightsout.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.activeandroid.query.Select;
import com.valkyrie.nabeshimamac.lightsout.R;
import com.valkyrie.nabeshimamac.lightsout.adapter.QuestionAdapter;
import com.valkyrie.nabeshimamac.lightsout.model.Question;

import java.util.List;

/**
 * CreateモードのListViewのActivity
 */
public class MakeListActivity extends AppCompatActivity {
    private static final int CODE_MAKE = 1;

    private ListView listView;
    private QuestionAdapter adapter;

    public static Intent createIntent(Context context) {
        return new Intent(context, MakeListActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_list);

        setTitle("Create mode");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView = (ListView) findViewById(R.id.listView);

        adapter = new QuestionAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(MakeListActivity.class.getSimpleName(), "onItemClick");
                final Question question = adapter.getItem(position);
                startActivity(
                        MainActivity.createIntent(MakeListActivity.this, question.getId())
                );
            }
        });
        List<Question> questions = new Select().from(Question.class).execute();
        adapter.addAll(questions);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_MAKE && resultCode == RESULT_OK) {
            // 新しいアイテムが追加されたので、画面を更新する
            final List<Question> questions = new Select().from(Question.class).execute();
            adapter.clear();
            adapter.addAll(questions);
        }
    }

    public void make(View v) {
        final Intent intent = MakeActivity.createIntent(this);
        startActivityForResult(intent, CODE_MAKE);
        //新規作成
    }
}
