package com.fly.run.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by xinzhendi-031 on 2018/6/29.
 */

public class BroadcastUtil {

    public static final String USER_INFO_UPDATE = "user_info_update";
    public static final String CIRCLE_REPLY_UPDATE = "CIRCLE_REPLY_UPDATE";
    public static final String CIRCLE_LIKE_UPDATE = "CIRCLE_LIKE_UPDATE";
    public static final String CIRCLE_SHARE_UPDATE = "CIRCLE_SHARE_UPDATE";

    /**
     * 发送广播
     * 不带参数
     */
    public static void sendBroadcast(Context context,String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        context.sendBroadcast(intent);
    }

    /**
     * 发送广播
     * 带参数：DATA
     */
    public static void sendBroadcast(Context context, String action, Bundle bundle) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra("DATA",bundle);
        context.sendBroadcast(intent);
    }
}
