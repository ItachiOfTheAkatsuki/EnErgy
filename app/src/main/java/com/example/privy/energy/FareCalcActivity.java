package com.example.privy.energy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Arrays;

import static com.example.privy.energy.R.id.tv_device;

public class FareCalcActivity extends AppCompatActivity {

    int region = -1;
    int phase = -1;
    float totalUnits = 0;

    String devices[] = {"14\" CRT TELEVISION", "21\" CRT TELEVISION", "29\" CRT TELEVISION", "22\" LCD TELEVISION", "32\" LCD TELEVISION", "42\" LCD TELEVISION", "32\" LED TELEVISION", "46\" LED TELEVISION", "55\" LED TELEVISION", "42\" PLASMA TELEVISION", "50\" PLASMA TELEVISION", "VIDEO/DVD PLAYER/RECORDER", "VIDEO GAME", "DIGITAL/ANALOGUE RADIO", "HOME THEATER", "MUSIC CENTRE", "MICRO SYSTEM", "SATELLITE/FREEVIEW/CABLE BOX", "GAMES CONSOLE", "DESKTOP PC AND CRT MONITOR", "SCANNER", "INKJET PRINTER", "BROADBAND ROUTER", "TELEPHONE (2 DIGITAL HANDSETS)", "COMPACT FLUORESCENT BULB", "FLUORESCENT STRIP LIGHT", "HALOGEN BULB", "100W TUNGSTEN LIGHT BULB", "OIL FILLED RADIATOR", "DEHUMIDIFIER", "FAN HEATER", "HALOGEN HEATER", "IMMERSION HEATER", "AIR CONDITIONER (7.500 BTUS)", "AIR CONDITIONER (10.000 BTUS)", "AIR CONDITIONER (12.000 BTUS)", "AIR CONDITIONER (15.000 BTUS)", "AIR CONDITIONER (18.000 BTUS)", "ELECTRIC SHOWER", "ELECTRIC BLANKET", "ELECTRIC KETTLE", "ELECTRIC OVEN", "ELECTRIC HOB RING", "MICROWAVE", "A RATED FRIDGE", "A RATED FREEZER", "A RATED DISH WASHER", "A RATED WASHING MACHINE", "TUMBLE DRIER", "STEAM IRON", "VACUUM CLEANER", "ELECTRIC DRILL", "CEILING FAN", "TABLE FAN"};
    int power[] = {55, 100, 95, 56, 170, 220, 95, 165, 220, 280, 450, 40, 15, 4, 350, 75, 20, 40, 45, 200, 55, 60, 10, 7, 20, 36, 60, 100, 2500, 100, 3000, 1200, 3000, 1000, 1350, 1450, 2000, 2100, 10500, 130, 2200, 2200, 1400, 800, 120, 150, 1050, 3000, 2500, 1800, 1200, 900, 120, 65};

    AutoCompleteTextView tv_devices;
    TableLayout device_table;

    boolean dark_theme;
    String lan;

    private int pow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate", "beg");
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        dark_theme = sharedPreferences.getBoolean(getString(R.string.pref_dark_theme_key), getResources().getBoolean(R.bool.pref_dark_theme_bool));
        //lan=sharedPreferences.getString(getString(R.string.pref_language_key),"en");
        lan = sharedPreferences.getString("SELECTED_LANGUAGE", "en");
        if (dark_theme) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_fare_calc);

        Log.d("onCreate", "2");


        tv_devices = (AutoCompleteTextView) findViewById(tv_device);


        //get data from RegionSelectionActivity
        Bundle bundle = getIntent().getExtras();
        try {
            region = Integer.parseInt(bundle.getString("locIndex"));
            phase = Integer.parseInt(bundle.getString("phase"));
            Log.d("onCreate", "3");
        } catch (Exception e) {
            Log.d("try", "No info");
        }
        Log.d("FareCalcActivity", "Region: " + region + "Phase: " + phase);

        //add suggestions to AutoCompleteTextView for devices

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, devices);

        tv_devices.setAdapter(adapter);
        tv_devices.setThreshold(0);

        device_table = (TableLayout) findViewById(R.id.device_list);
        Log.d("FareCalcActivity", "dev_len: " + devices.length + " pow_len: " + power.length);
    }

    @Override
    public void onResume() {
        Log.d("onRes", "1");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (dark_theme != sharedPreferences.getBoolean(getString(R.string.pref_dark_theme_key), getResources().getBoolean(R.bool.pref_dark_theme_bool))) {
            this.finish();
            Intent restartActivityIntent = new Intent(this, getClass());
            restartActivityIntent.putExtra("locIndex", "" + region);
            restartActivityIntent.putExtra("phase", "" + phase);
            this.startActivity(restartActivityIntent);
        }
        if (!lan.equals(sharedPreferences.getString("SELECTED_LANGUAGE", "en"))) {
            Log.d("onRes:lan", "" + lan);
            lan = sharedPreferences.getString("SELECTED_LANGUAGE", "en");
            Log.d("onRes:lan", "" + lan);
            updateViews(lan);
        }

        super.onResume();
        Log.d("onRes", "3 lan " + lan);
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

    public void addList(View v) {
        TextView tv_device = new TextView(FareCalcActivity.this);
        TextView tv_time = new TextView(FareCalcActivity.this);
        EditText dTime = (EditText) findViewById(R.id.et_time);

        float time;
        try {
            time = Float.parseFloat(dTime.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(FareCalcActivity.this, "Please enter a valid time.", Toast.LENGTH_SHORT).show();
            return;
        } catch (Exception e) {
            Toast.makeText(FareCalcActivity.this, "Please enter a practical and valid time.", Toast.LENGTH_SHORT).show();
            return;
        }
        pow=-1;
        String currentDevice = tv_devices.getText().toString().toUpperCase();
        if (!Arrays.asList(devices).contains(currentDevice)) {
            Toast.makeText(FareCalcActivity.this, "Please enter devices which are available in suggestions.", Toast.LENGTH_SHORT).show();
            //add code for dialog here
            /*AddCustomDevice customDevice = new AddCustomDevice();
            customDevice.show(getFragmentManager(), "CustomDevice");
            pow = customDevice.getPower();
            if (customDevice.getPower() != -1) {
                pow = customDevice.getPower();
                Log.d("custom power", "= " + pow);
            } else {
                Log.d("custom power in else", "= " + pow);
                return;
            }*/

            /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(R.layout.add_device_dialog);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //onclick code
                    Dialog f = (Dialog) dialog;
                    EditText et_power = (EditText) f.findViewById(R.id.et_power);
                    if (et_power == null)
                        Log.d("alert","null");
                    pow = Integer.parseInt(et_power.getText().toString());
                }
            });*/
            return;
            //AlertDialog alertDialog = builder.create();
            //alertDialog.show();
            //Log.d("User input power",""+pow);
        }
        else {
            int idx = Arrays.asList(devices).indexOf(currentDevice);
            pow = power[idx];
        }
        if (phase == 3)
            pow *= 1.732;
        tv_device.setText(currentDevice);
        tv_time.setText("" + time);
        tv_device.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        tv_time.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        TableRow row = new TableRow(FareCalcActivity.this);
        row.addView(tv_device);
        row.addView(tv_time);
        device_table.addView(row);
        switch (region) {
            case 0:
                calcFareKSEB(pow, time);
                break;
            default:
                Log.d("FareCalcActivity: ", "Coming soon...");
        }
        tv_devices.setText("");
        dTime.setText("");

    }

    public void calcFareKSEB(int pow, float time) {
        double cost = 0;
        totalUnits += pow * time / 1000;
        if (totalUnits < 100)
            cost = totalUnits * 2.8;
        else if (totalUnits < 200)
            cost = 280 + (totalUnits - 100) * 3.2;
        else if (totalUnits < 300)
            cost = 600 + (totalUnits - 200) * 4.2;
        else if (totalUnits < 400)
            cost = 1020 + (totalUnits - 300) * 5.8;
        else if (totalUnits < 500)
            cost = 1600 + (totalUnits - 400) * 7;
        else if (totalUnits < 600)
            cost = totalUnits * 5;
        else if (totalUnits < 700)
            cost = totalUnits * 5.7;
        else if (totalUnits < 800)
            cost = totalUnits * 6.1;
        else if (totalUnits < 1000)
            cost = totalUnits * 6.7;
        else
            cost = totalUnits * 7.5;
        switch (phase) {
            case 1:
                cost += 40;
                break;
            case 3:
                cost += 120;
        }
        cost += 0.1 * cost;
        cost = roundTo2Decimals(cost);
        TextView tv_charges = (TextView) findViewById(R.id.tv_charges);
        tv_charges.setText("₹" + cost);
        Log.d("FareCalcActivity: calc", "Units: " + totalUnits);
    }

    public void clearData(View v) {
        totalUnits = 0;

        TextView tv_charges = (TextView) findViewById(R.id.tv_charges);
        tv_charges.setText("0");

        TextView et_time = (TextView) findViewById(R.id.et_time);
        tv_devices.setText("");
        et_time.setText("");
        device_table.removeAllViews();

    }

    private void updateViews(String languageCode) {
        Context context = LocaleHelper.setLocale(this, languageCode);
        Resources resources = context.getResources();

        TextView tv_charges_label = (TextView) findViewById(R.id.tv_charges_label);
        TextView tv_devices_label = (TextView) findViewById(R.id.tv_device_label);
        TextView tv_time_label = (TextView) findViewById(R.id.tv_time_label);
        EditText et_time = (EditText) findViewById(R.id.et_time);
        Button addDevice = (Button) findViewById(R.id.button_add_device);
        Button clear = (Button) findViewById(R.id.button_clear);


        tv_charges_label.setText(resources.getString(R.string.totalCharges));
        tv_devices_label.setText(resources.getString(R.string.deviceText));
        tv_time_label.setText(resources.getString(R.string.timeText));
        tv_devices.setHint(resources.getString(R.string.deviceHint));
        et_time.setHint(resources.getString(R.string.timeHint));
        addDevice.setText(resources.getString(R.string.addDevice));
        clear.setText(resources.getString(R.string.clearButton));
        /*onePhase.setText(resources.getString(R.string.singlePhase));
        threePhase.setText(resources.getString(R.string.threePhase));
        next.setText(resources.getString(R.string.next));
        Log.d("updateviews","hhere");*/
        //mToENButton.setText(resources.getString(R.string.main_activity_to_en_button));

        //setTitle(resources.getString(R.string.main_activity_toolbar_title));
    }

    double roundTo2Decimals(double val) {
        DecimalFormat df2 = new DecimalFormat("###.##");
        return Double.valueOf(df2.format(val));
    }
}