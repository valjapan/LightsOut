package com.valkyrie.nabeshimamac.lightsout.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.valkyrie.nabeshimamac.lightsout.R;
import com.valkyrie.nabeshimamac.lightsout.adapter.SharedQuestionAdapter;
import com.valkyrie.nabeshimamac.lightsout.manager.FirebaseManager;
import com.valkyrie.nabeshimamac.lightsout.model.SharedQuestion;

public class SharedQuestionListActivity extends AppCompatActivity {
    private static final String TAG = SharedQuestionListActivity.class.getSimpleName();

    private ListView listView;
    private SharedQuestionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_question_list);
        adapter = new SharedQuestionAdapter(this);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(MakeListActivity.class.getSimpleName(), "onItemClick");
                startActivity(
                        MainActivity.createIntent(SharedQuestionListActivity.this, adapter.getItem(position))
                );
            }
        });
        getData();
    }


    public void getData() {
        final Query query = FirebaseManager.getObjects("questions");
        query.limitToLast(20);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded");
                SharedQuestion sharedQuestion = dataSnapshot.getValue(SharedQuestion.class);
                adapter.add(sharedQuestion);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildMoved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled");
            }
        });
    }
}