package com.example.privy.energy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;

public class RegionSelectionActivity extends AppCompatActivity {
    //implements SharedPreferences.OnSharedPreferenceChangeListener

    String pos = "0";
    String phase = "1";

    boolean dark_theme;
    String lan;

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate","1");
        super.onCreate(savedInstanceState);
        Log.d("onCreate","2");
        SharedPreferences sharedPreferences;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        dark_theme = sharedPreferences.getBoolean(getString(R.string.pref_dark_theme_key), getResources().getBoolean(R.bool.pref_dark_theme_bool));
        //Button nextButton=(Button) findViewById(R.id.button_next);
        //LinearLayout buttonLayout=(LinearLayout) findViewById(R.id.next_button_layout);


        if (dark_theme) {
            setTheme(R.style.AppThemeDark);
        }
        else {
            setTheme(R.style.AppTheme);
        }
        //lan=sharedPreferences.getString(getString(R.string.pref_language_key),"en");
        lan=sharedPreferences.getString("SELECTED_LANGUAGE","en");

        setContentView(R.layout.activity_region_selection);

        if (dark_theme) {
            RadioButton onePhase = (RadioButton) findViewById(R.id.onePhase);
            RadioButton threePhase = (RadioButton) findViewById(R.id.threePhase);
            onePhase.setTextColor(ContextCompat.getColorStateList(this, R.color.white));
            threePhase.setTextColor(ContextCompat.getColorStateList(this, R.color.white));
        }

        //adding list of locations to spinner
        Spinner locationSp = (Spinner) findViewById(R.id.locationSpinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.location, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSp.setAdapter(spinnerAdapter);

        //setting size of selection in spinner
        locationSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Change the selected item's text size
                ((TextView) view).setTextSize(COMPLEX_UNIT_DIP, 20);
                Integer i = position;
                pos = i.toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        RegionSelectionActivity.context=getApplicationContext();
    }


    @Override
    public void onResume() {
        Log.d("onResReg","beg");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (dark_theme != sharedPreferences.getBoolean(getString(R.string.pref_dark_theme_key), getResources().getBoolean(R.bool.pref_dark_theme_bool))) {
            this.finish();
            this.startActivity(new Intent(this, this.getClass()));
        }
        /*if (!lan.equals(sharedPreferences.getString(getString(R.string.pref_language_key),"en"))) {
            Log.d("onRes:lan",""+lan);
            lan=sharedPreferences.getString(getString(R.string.pref_language_key),"en");
            Log.d("onRes:lan",""+lan);
            this.finish();
            this.startActivity(new Intent(this, this.getClass()));
        }*/
        if(lan!=sharedPreferences.getString("SELECTED_LANGUAGE","en")) {
            Log.d("inside lang change if","here");
            lan = sharedPreferences.getString("SELECTED_LANGUAGE", "en");
            updateViews(lan);
            this.finish();
            this.startActivity(new Intent(this, this.getClass()));
        }
        super.onResume();
        Log.d("onRes","end"+lan);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator = getMenuInflater();
        inflator.inflate(R.menu.energy_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void phaseSelect(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.onePhase:
                if (checked) {
                    phase = "1";
                }
                break;
            case R.id.threePhase:
                if (checked) {
                    phase = "3";
                }
                break;
        }
    }

    public void clickedNext(View v) {
        if (Integer.parseInt(pos) > 1) {
            Toast.makeText(RegionSelectionActivity.this, "Please select an available energy provider.", Toast.LENGTH_SHORT).show();
            return;
        }
        Context context = RegionSelectionActivity.this;
        Class destinationActivity = FareCalcActivity.class;
        Intent startChildActivityIntent = new Intent(context, destinationActivity);
        startChildActivityIntent.putExtra("locIndex", pos);
        startChildActivityIntent.putExtra("phase", phase);
        startActivity(startChildActivityIntent);
    }

    private void updateViews(String languageCode) {
        Context context = LocaleHelper.setLocale(this, languageCode);
        Resources resources = context.getResources();

        TextView tv_provider = (TextView) findViewById(R.id.tv_provider);
        RadioButton onePhase = (RadioButton) findViewById(R.id.onePhase);
        RadioButton threePhase = (RadioButton) findViewById(R.id.threePhase);
        Button next = (Button) findViewById(R.id.button_next);

        tv_provider.setText(resources.getString(R.string.provider));
        onePhase.setText(resources.getString(R.string.singlePhase));
        threePhase.setText(resources.getString(R.string.threePhase));
        next.setText(resources.getString(R.string.next));
        Log.d("updateviews","hhere");
        //mToENButton.setText(resources.getString(R.string.main_activity_to_en_button));

        //setTitle(resources.getString(R.string.main_activity_toolbar_title));
    }
}