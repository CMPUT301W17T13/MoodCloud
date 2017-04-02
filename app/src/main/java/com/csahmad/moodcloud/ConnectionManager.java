package com.csahmad.moodcloud;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/** Check if connected to internet. */
public class ConnectionManager {

    /** Return whether connected to internet. */
    public static boolean haveConnection(Context context) {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /** Display a message to the user indicating that there was some kind of connection error. */
    public static void showConnectionError(Context context) {

        Toast.makeText(context, "Connection error", Toast.LENGTH_LONG).show();
    }
}
