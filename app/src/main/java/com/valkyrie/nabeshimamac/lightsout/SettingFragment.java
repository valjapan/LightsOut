package com.valkyrie.nabeshimamac.lightsout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * 設定画面のFragment
 */

public class SettingFragment extends PreferenceFragment {

    public static Intent createIntent(Context context) {
        return new Intent(context,SettingFragment.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_setting);
    }

}
