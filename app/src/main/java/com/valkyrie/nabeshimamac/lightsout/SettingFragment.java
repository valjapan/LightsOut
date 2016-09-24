package com.valkyrie.nabeshimamac.lightsout;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by NabeshimaMAC on 2016/09/24.
 */

public class SettingFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_setting);
    }
}
