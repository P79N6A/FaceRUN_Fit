package com.fly.run.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by xinzhendi-031 on 2017/1/24.
 */
public class PhoneUtil {

    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String tmDevice = "" + tm.getDeviceId();
        return tmDevice;
    }
}
