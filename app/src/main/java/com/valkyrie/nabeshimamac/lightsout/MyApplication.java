package com.valkyrie.nabeshimamac.lightsout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;

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
    public FirebaseAnalytics analytics;

    //GoogleGame、analytics実装クラス

    @Override
    public void onCreate() {
        super.onCreate();

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Games.API)
                .addScope(Games.SCOPE_GAMES)
                .build();

        analytics = FirebaseAnalytics.getInstance(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }
}
