package com.valkyrie.nabeshimamac.lightsout;

import android.support.annotation.NonNull;

import com.activeandroid.app.Application;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

/**
 * Created by NabeshimaMAC on 16/08/20.
 */
public class MyApplication extends Application {
    @NonNull
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Games.API)
                .addScope(Games.SCOPE_GAMES)
                .build();
    }
    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }
}
