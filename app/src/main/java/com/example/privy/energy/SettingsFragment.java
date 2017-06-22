package com.example.privy.energy;


import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

/**
 * Created by privy on 16-Jun-17.
 */

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        Log.d("SettingsFragment","label1");
        addPreferencesFromResource(R.xml.pref_energy);
        Log.d("SettingsFragment","label2");
    }
}
