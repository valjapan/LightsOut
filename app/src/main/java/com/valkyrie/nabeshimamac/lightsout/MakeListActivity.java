package com.valkyrie.nabeshimamac.lightsout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.activeandroid.query.Select;

import java.util.List;

public class MakeListActivity extends AppCompatActivity {
    Button makeNewButton;
    private ListView listView;
    private QuestionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_list);

        setTitle("Create mode");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        makeNewButton = (Button)findViewById(R.id.newMake);
        listView = (ListView) findViewById(R.id.listView);

        adapter = new QuestionAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(MakeListActivity.class.getSimpleName(), "onItemClick");

                final Question question = adapter.getItem(position);
                final Intent intent = new Intent(MakeListActivity.this, MainActivity.class);
                intent.putExtra("question_id", question.getId());
                startActivity(intent);
            }
        });
        List<Question> questions = new Select().from(Question.class).execute();
        adapter.addAll(questions);
    }

    public void make(View v){
        Intent intent = new Intent(this,MakeActivity.class);
        startActivityForResult(intent, 10);
        //新規作成
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK){
            List<Question> questions = new Select().from(Question.class).execute();
            adapter.clear();
            adapter.addAll(questions);
        }

    }
}
