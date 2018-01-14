package com.whut.mine.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkUtils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (!(networkinfo == null || !networkinfo.isAvailable())) {
            return true;
        } else {
            Toast.makeText(context, "请检查网络设置！", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}