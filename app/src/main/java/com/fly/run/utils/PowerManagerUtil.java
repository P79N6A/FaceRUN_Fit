package com.fly.run.utils;

import android.content.Context;
import android.os.PowerManager;

/**
 * Created by xinzhendi-031 on 2016/12/21.
 */
public class PowerManagerUtil {

    public static void keepScreenOn(Context context, PowerManager.WakeLock wakeLock, boolean on) {
        Logger.e("keepScreenOn", "on = " + on);
        if (on) {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "==KeepScreenOn==");
            wakeLock.acquire();
            wakeLock.release();
            wakeLock = null;
        } else {
            if (wakeLock != null) {
                wakeLock.release();
                wakeLock = null;
            }
        }
    }

    public static void brightScreen(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                | PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "==brightScreen==");
        wakeLock.acquire();
        wakeLock.release();
        wakeLock = null;
    }

    public static boolean isScreenOn(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean screen = pm.isScreenOn();
        return screen;
    }

}
