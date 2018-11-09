package com.fly.run.manager.upload.callback;

import android.content.Context;
import android.util.Log;

public class IMFileUploadCallback implements UploadCallback {
    private static final String TAG = "IMFileUploadCallback";
    private long startTime;
    private static final long INTERVAL = 800;
    private Context context;
    private String path;
    private String messageId;

    public IMFileUploadCallback(Context context, String path, String messageId) {
        this.context = context;
        this.path = path;
        this.messageId = messageId;
    }

    @Override
    public void start() {
        Log.i(TAG, "start");
    }

    @Override
    public void progress(int progress) {
        Log.i(TAG, "progress = "+progress);
        long nowTime = System.currentTimeMillis();
        if (nowTime - startTime < INTERVAL) {
            return;
        }
        startTime = nowTime;
    }

    @Override
    public void end() {
    }

    @Override
    public void success(String result) {

    }

    @Override
    public void error() {

    }

}
