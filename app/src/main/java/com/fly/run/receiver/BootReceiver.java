package com.fly.run.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fly.run.utils.Logger;

/**
 * Created by kongwei on 2017/3/2.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.e("BootReceiver", "Action = " + intent.getAction());
//        context.startService(new Intent(context, LockService.class));//此Service可在首次启动Activity中启动
    }
}
