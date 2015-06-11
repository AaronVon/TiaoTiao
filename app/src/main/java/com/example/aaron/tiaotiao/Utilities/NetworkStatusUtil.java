package com.example.aaron.tiaotiao.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Aaron on 6/11/15.
 */
public class NetworkStatusUtil {
    private Context context;
    public NetworkStatusUtil(Context context) {
        this.context = context;
    }

    public boolean isAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo().isAvailable();
    }
}
