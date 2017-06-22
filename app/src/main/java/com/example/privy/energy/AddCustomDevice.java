package com.example.privy.energy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

/**
 * Created by privy on 21-Jun-17.
 */

public class AddCustomDevice extends DialogFragment {

    private int power=-1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.add_device_dialog);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Dialog f = (Dialog) dialog;
                //Activity activity=getActivity();
                EditText et_power = (EditText) f.findViewById(R.id.et_power);
                if (et_power == null)
                    Log.d("alert","null");
                power = Integer.parseInt(et_power.getText().toString());
                Log.d("AddCustomDevice","power: "+power);
                //notifyAll();

            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //add negative code here
                power = -1;
            }
        });
        return builder.create();
    }
    public int getPower() {
        return power;
    }
}
