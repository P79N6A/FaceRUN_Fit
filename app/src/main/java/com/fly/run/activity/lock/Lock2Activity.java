package com.fly.run.activity.lock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.config.Configs;
import com.fly.run.utils.ToastUtil;

import static com.fly.run.R.id.iv_wallpapger;
import static com.fly.run.R.id.tv_kcal;

/**
 * Created by kongwei on 2017/3/2.
 */

public class Lock2Activity extends BaseUIActivity {

    private BroadcastReceiver receiver;
    private ImageView blurImage; // 显示模糊的图片
    private Bitmap bitmap;
    private TextView tv_time;
    private TextView tv_distance;
    private TextView tv_speed;
    private TextView tv_signal;

    private Handler handler = new Handler();
    private int ClickCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        registerReceiver();
        setContentView(R.layout.activity_lock);
        initViw();
    }

    private void initViw() {
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_distance = (TextView) findViewById(R.id.tv_distance);
        tv_speed = (TextView) findViewById(R.id.tv_speed);
        tv_signal = (TextView) findViewById(tv_kcal);
        blurImage = (ImageView) findViewById(iv_wallpapger);
        blurImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickCount++;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (ClickCount == 2) {
                            finish();
                        } else {
                            ToastUtil.show("双击查看详情");
                            ClickCount = 0;
                        }
                    }
                }, 600);
            }
        });
//        Drawable drawable = WallpaperUtil.getWallpagerDrawable(Lock2Activity.this);
//        if (drawable != null) {
//            blurImage.setImageDrawable(drawable);
//        }
    }

    public void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Configs.Broadcast_Receiver_Time);
        if (null == receiver) {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals(Configs.Broadcast_Receiver_Time)) {
                        String strTime = intent.getStringExtra("TIME");
                        String strDistance = intent.getStringExtra("DISTANCE");
                        String strSpeed = intent.getStringExtra("SPEED");
                        String strSignal = intent.getStringExtra("SIGNAL");
                        if (!TextUtils.isEmpty(strTime))
                            tv_time.setText(strTime);
                        if (!TextUtils.isEmpty(strDistance))
                            tv_distance.setText(strDistance);
                        if (!TextUtils.isEmpty(strSpeed))
                            tv_speed.setText(strSpeed);
                        if (!TextUtils.isEmpty(strSignal))
                            tv_signal.setText(strSignal);
                    }
                }
            };
        }
        registerReceiver(receiver, intentFilter);
    }

    public void unRegisterReceiver() {
        if (null != receiver) {
            unregisterReceiver(receiver);
        }
    }

    @Override
    protected void onDestroy() {
        unRegisterReceiver();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

}
