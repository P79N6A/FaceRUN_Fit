package com.fly.run.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created on 2016/3/10.
 */
public class NetWorkUtil {
    public static boolean haveNetWork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo tmpInfo = connectivityManager.getActiveNetworkInfo();
        boolean haveNetWork = tmpInfo != null && tmpInfo.isAvailable();
        return haveNetWork;
    }
}
