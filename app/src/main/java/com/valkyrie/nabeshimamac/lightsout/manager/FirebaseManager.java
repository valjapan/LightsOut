package com.valkyrie.nabeshimamac.lightsout.manager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by NabeshimaMAC on 2016/09/22.
 */
public class FirebaseManager {
    private static final Object LOCK = new Object();

    private static FirebaseDatabase database;

    private static FirebaseDatabase getInstnace() {
        synchronized (LOCK) {
            if (database == null) {
                database = FirebaseDatabase.getInstance();
            }
            return database;
        }
    }

    public static String pushObject(String prefKey, Object object) {
        final DatabaseReference reference = getInstnace().getReference(prefKey).push();
        reference.setValue(object);
        return reference.getKey();
    }

    public static String updateObject(String prefKey, String key, Object object) {
        final DatabaseReference reference = getInstnace().getReference(prefKey).child(key);
        if (reference != null) {
            reference.setValue(object);
            return key;
        } else {
            return pushObject(prefKey, object);
        }
    }

}
