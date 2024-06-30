package com.example.lab2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class ConnectionReceiver extends BroadcastReceiver {

    public Context c; // class parent of class activity
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("App", ""+intent.getAction());
        if(intent.getAction().equals("com.example.lab2.SOME_ACTION"))
            Toast.makeText(context, "SOME_ACTION is received",
                    Toast.LENGTH_SHORT).show();
        else if(intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")){
            ConnectivityManager cm =
                    (ConnectivityManager)
                            context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            if (isConnected) {
                try {
                    Toast.makeText(context, "Network is connected",
                            Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(context, "Network is changed or reconnected",
                        Toast.LENGTH_LONG).show();
            }
        }else if(intent.getAction().equals(Intent.ACTION_AIRPLANE_MODE_CHANGED))
        {
            if (isAirplaneModeOn(context.getApplicationContext())) {
                Toast.makeText(context, "AirPlane mode is on",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "AirPlane mode is off",
                        Toast.LENGTH_SHORT).show();
            }
        }


    }
    private static boolean isAirplaneModeOn(Context context) {
        return Settings.System.getInt(context.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
    }
}
