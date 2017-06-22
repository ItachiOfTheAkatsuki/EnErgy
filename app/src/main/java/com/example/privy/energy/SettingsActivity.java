package com.example.privy.energy;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import static android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    boolean dark_theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences sharedPreferences;
        sharedPreferences = getDefaultSharedPreferences(this);
        dark_theme = sharedPreferences.getBoolean(getString(R.string.pref_dark_theme_key), getResources().getBoolean(R.bool.pref_dark_theme_bool));
        Log.d("SettingsActivity", "DarkTheme in onCreate" + dark_theme);
        if (dark_theme) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppTheme);
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        setContentView(R.layout.activity_settings);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        } else {
            this.finish();
        }
        Log.d("Settings", "3 Pressed: " + id);
        Log.d("Settings", "4 home: " + R.id.home);
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_dark_theme_key))) {
            recreate();
        } else if (key.equals("SELECTED_LANGUAGE")) {
            recreate();
        }
    }
}