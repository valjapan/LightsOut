package com.valkyrie.nabeshimamac.lightsout;

import android.support.annotation.NonNull;

import com.activeandroid.app.Application;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by NabeshimaMAC on 16/08/20.
 */
public class MyApplication extends Application {
    @NonNull
    private GoogleApiClient googleApiClient;
    private FirebaseAnalytics analytics;

    @Override
    public void onCreate() {
        super.onCreate();

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Games.API)
                .addScope(Games.SCOPE_GAMES)
                .build();

        analytics = FirebaseAnalytics.getInstance(this);
    }

    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }
}
