package com.valkyrie.nabeshimamac.lightsout.manager;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * FirebaseのManager
 */
public class FirebaseManager {
    private static final Object LOCK = new Object();

    private static FirebaseDatabase database;

    public static Intent createIntent(Context context) {
        return new Intent(context, FirebaseManager.class);
    }

    private static FirebaseDatabase getInstnace() {
        synchronized (LOCK) {
            if (database == null) {
                database = FirebaseDatabase.getInstance();
            }
            return database;
        }
    }

    public static String pushObject(String prefKey, Object object, OnCompleteListener<Void> completeListener) {
        final DatabaseReference reference = getInstnace().getReference(prefKey).push();
        if (completeListener != null) {
            reference.setValue(object).addOnCompleteListener(completeListener);
        } else {
            reference.setValue(object);
        }
        return reference.getKey();
    }

    public static String updateObject(String prefKey, String key, Object object) {
        final DatabaseReference reference = getInstnace().getReference(prefKey).child(key);
        if (reference != null) {
            reference.setValue(object);
            return key;
        } else {
            return pushObject(prefKey, object, null);
        }
    }

    public static Query  getObjects(String prefKey) {
        return getInstnace().getReference(prefKey).orderByKey();
    }

}
