package com.valkyrie.nabeshimamac.lightsout.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.valkyrie.nabeshimamac.lightsout.manager.FirebaseManager;
import com.valkyrie.nabeshimamac.lightsout.model.SharedQuestion;

import java.util.List;

/**
 * Created by NabeshimaMAC on 2016/09/24.
 */

public class SharedQuestionCallbackActivity extends AppCompatActivity {

    public static Intent createIntent(Context context) {
        return new Intent(context, SharedQuestionListActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        parseIntent(intent);
    }

    private void parseIntent(Intent intent) {
        Log.d(SharedQuestionCallbackActivity.class.getSimpleName(), "parseIntent");
        if (intent == null || intent.getData() == null) {
            finish();
            return;
        }
        final Uri uri = intent.getData();
        List<String> paths = uri.getPathSegments();
        if (!paths.isEmpty()) {
            String sharedKey = paths.get(paths.size() - 1);
            Log.d(SharedQuestionCallbackActivity.class.getSimpleName(), "ShareKey: " + sharedKey);
            getData(sharedKey);
        }
    }


    public void getData(final String sharedKey) {
        final Query query = FirebaseManager.getObjects("questions");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(SharedQuestionCallbackActivity.class.getSimpleName(), "onChildAdded");
                if (dataSnapshot.getKey().equals(sharedKey)) {
                    final SharedQuestion sharedQuestion = dataSnapshot.getValue(SharedQuestion.class);
                    final Intent intent = MainActivity.createIntent(SharedQuestionCallbackActivity.this, sharedQuestion);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}
