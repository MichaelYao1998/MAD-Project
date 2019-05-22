package com.e.assignment.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE );
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        Toast.makeText(context, "Internet Connected", Toast.LENGTH_LONG).show();
        Log.i("!!??", "onReceive: ");
        if (isConnected) {
            Log.i("!!??", "activeNetwork: ");
        }
        else{
            Log.i("!!??", "no receive: ");
        }
    }
}
