package com.fly.run.utils;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by kongwei on 2017/3/6.
 * 震动工具类
 */

public class VibratorUtil {

    public static void doVibrator(Context context) {
        /*
         * 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
         * */
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
//        long[] pattern = {100, 400, 100, 400};   // 停止 开启 停止 开启
//        vibrator.vibrate(pattern, 2);           //重复两次上面的pattern 如果只想震动一次，index设为-1
        vibrator.vibrate(200);
    }
}
