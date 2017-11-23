package com.fly.run.activity.base;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.fly.run.activity.MainRunActivity;
import com.fly.run.config.Configs;
import com.fly.run.utils.Logger;

import java.util.List;

/**
 * Created by kongwei on 2017/2/16.
 */

public class BaseDataActivity extends FragmentActivity {
    protected String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected String getNowActivityName() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = manager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo cinfo = runningTasks.get(0);
        ComponentName component = cinfo.topActivity;
        Logger.e("current activity is ", component.getShortClassName());
        return component.getShortClassName();
    }

    protected void voiceWakeUp(String msg) {
        switch (msg) {
            case "巴弟快跑":
                wakeUpBADIRUN();
                break;
            case "开始跑步":
                wakeUpBeginRUN();
                break;
            case "跑步结束":
                wakeUpOverRUN();
                break;
            case "增大音量":
                wakeUpVoiceUp();
                break;
            case "减小音量":
                wakeUpVoiceDown();
                break;
            case "下一首":
                wakeUpNext();
                break;
        }
    }

    protected void wakeUpBADIRUN() {

    }

    protected void wakeUpBeginRUN() {
        String nowActivity = getNowActivityName();
        Logger.e(TAG, "nowActivity = " + nowActivity);
        if (!TextUtils.isEmpty(nowActivity)) {
            String[] s = nowActivity.split(".");
            if (s != null && s.length > 0) {
                nowActivity = s[s.length - 1];
            }
        }
        if (!TextUtils.isEmpty(nowActivity) && !nowActivity.endsWith("MainRunActivity")) {
            Intent intent = new Intent(getApplicationContext(), MainRunActivity.class);
            intent.putExtra("StartRunNow", true);
            startActivity(intent);
            return;
        }
        sendBroadcast(Configs.Broadcast_Receiver_Begin_Run);
    }

    protected void wakeUpOverRUN() {
        sendBroadcast(Configs.Broadcast_Receiver_Over_Run);
    }

    protected void wakeUpVoiceUp() {

    }

    protected void wakeUpVoiceDown() {

    }

    protected void wakeUpNext() {

    }

    /**
     * 发送广播
     */
    private void sendBroadcast(String broad) {
        Intent intent = new Intent();
        intent.setAction(broad);
        sendBroadcast(intent);
    }

    protected void intentToActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    protected void intentExtraToActivity(Class c, Bundle bundle) {
        Intent intent = new Intent(getApplicationContext(), c);
        intent.putExtra("Bundle", bundle);
        startActivity(intent);
    }
}
