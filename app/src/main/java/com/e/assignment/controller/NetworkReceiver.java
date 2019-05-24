package com.e.assignment.controller;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.util.Log;

import com.e.assignment.Service.NotificationService;


public class NetworkReceiver {
    private final String TAG = getClass().getName();

    /**
     *
     * @param context
     * enable the network check, if connect to network...
     * if disconnect form the network....
     */
    public void enable(Context context) {
//        networkRequest = new NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR).addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build();

        Log.i(TAG, "enable ");
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        connectivityManager.registerNetworkCallback(
                builder.build(),//network, give the current set of capabilities
                new ConnectivityManager.NetworkCallback() {

                    @Override
                    //connect to the internet
                    public void onAvailable(Network network) {
                        context.sendBroadcast(getConnectivityIntent(false));
                        Log.i(TAG, "activeNetwork: ");

                        Intent intent = new Intent(context, NotificationService.class);
                        context.startService(intent);
                    }

                    //disconnect from the internet
                    public void onLost(Network network) {
                        context.sendBroadcast(getConnectivityIntent(true));
                        Log.i(TAG, "no receive: ");
                    }
                }
        );
    }

    /**
     *
     * @param noConnection
     * @return
     *
     */
    private Intent getConnectivityIntent(boolean noConnection) {

        Intent intent = new Intent();

        intent.setAction("com.e.assignment.CONNECTIVITY_CHANGE");
        intent.putExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, noConnection);

        return intent;

    }

}
