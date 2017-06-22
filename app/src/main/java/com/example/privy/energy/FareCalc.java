package com.example.privy.energy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.util.Arrays;

/**
 * Created by privy on 22-Jun-17.
 */

public class FareCalc extends DialogFragment {
    String devices[] = {"14\" CRT TELEVISION", "21\" CRT TELEVISION", "29\" CRT TELEVISION", "22\" LCD TELEVISION", "32\" LCD TELEVISION", "42\" LCD TELEVISION", "32\" LED TELEVISION", "46\" LED TELEVISION", "55\" LED TELEVISION", "42\" PLASMA TELEVISION", "50\" PLASMA TELEVISION", "VIDEO/DVD PLAYER/RECORDER", "VIDEO GAME", "DIGITAL/ANALOGUE RADIO", "HOME THEATER", "MUSIC CENTRE", "MICRO SYSTEM", "SATELLITE/FREEVIEW/CABLE BOX", "GAMES CONSOLE", "DESKTOP PC AND CRT MONITOR", "SCANNER", "INKJET PRINTER", "BROADBAND ROUTER", "TELEPHONE (2 DIGITAL HANDSETS)", "COMPACT FLUORESCENT BULB", "FLUORESCENT STRIP LIGHT", "HALOGEN BULB", "100W TUNGSTEN LIGHT BULB", "OIL FILLED RADIATOR", "DEHUMIDIFIER", "FAN HEATER", "HALOGEN HEATER", "IMMERSION HEATER", "AIR CONDITIONER (7.500 BTUS)", "AIR CONDITIONER (10.000 BTUS)", "AIR CONDITIONER (12.000 BTUS)", "AIR CONDITIONER (15.000 BTUS)", "AIR CONDITIONER (18.000 BTUS)", "ELECTRIC SHOWER", "ELECTRIC BLANKET", "ELECTRIC KETTLE", "ELECTRIC OVEN", "ELECTRIC HOB RING", "MICROWAVE", "A RATED FRIDGE", "A RATED FREEZER", "A RATED DISH WASHER", "A RATED WASHING MACHINE", "TUMBLE DRIER", "STEAM IRON", "VACUUM CLEANER", "ELECTRIC DRILL", "CEILING FAN", "TABLE FAN"};
    int power[] = {55, 100, 95, 56, 170, 220, 95, 165, 220, 280, 450, 40, 15, 4, 350, 75, 20, 40, 45, 200, 55, 60, 10, 7, 20, 36, 60, 100, 2500, 100, 3000, 1200, 3000, 1000, 1350, 1450, 2000, 2100, 10500, 130, 2200, 2200, 1400, 800, 120, 150, 1050, 3000, 2500, 1800, 1200, 900, 120, 65};
    int pow;

    public void calcFare(AutoCompleteTextView tv_devices, float Time) {
        pow = -1;
        String currentDevice = tv_devices.getText().toString().toUpperCase();
        if (!Arrays.asList(devices).contains(currentDevice)) {
            //Toast.makeText(FareCalcActivity.this, "Please enter devices which are available in suggestions.", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(R.layout.add_device_dialog);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //onclick code
                    Dialog f = (Dialog) dialog;
                    EditText et_power = (EditText) f.findViewById(R.id.et_power);
                    if (et_power == null)
                        Log.d("alert", "null");
                    pow = Integer.parseInt(et_power.getText().toString());
                }
            });
        }
    }
}
