package com.example.food_app.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NetworkChangeReceiver extends BroadcastReceiver {
    private NetworkChangeListener listener;

    public NetworkChangeReceiver() {
    }

    public NetworkChangeReceiver(NetworkChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isConnected = NetworkUtil.isInternetAvailable(context);

        if (listener != null) {
            listener.onNetworkChange(isConnected);
        }

    }

    public interface NetworkChangeListener {
        void onNetworkChange(boolean isConnected);
    }
}
