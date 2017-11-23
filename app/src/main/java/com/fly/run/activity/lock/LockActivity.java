package com.fly.run.activity.lock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.config.Configs;
import com.fly.run.utils.LockUtil;
import com.fly.run.utils.Logger;
import com.fly.run.utils.ToastUtil;

import static com.fly.run.R.id.tv_kcal;

/**
 * Created by kongwei on 2017/3/2.
 */

public class LockActivity extends BaseUIActivity {

    private LockUtil lockLayer;
    private View lockView;

    private BroadcastReceiver receiver;
    private ImageView blurImage; // 显示模糊的图片
    private TextView tv_time;
    private TextView tv_distance;
    private TextView tv_speed;
    private TextView tv_signal;

    private int ClickCount = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        registerReceiver();
        lockView = View.inflate(this, R.layout.activity_lock, null);
        initViw(lockView);
        lockLayer = new LockUtil(this);
        lockLayer.setLockView(lockView);// 设置要展示的页面
        lockLayer.lock();// 开启锁屏
    }


    private void initViw(View view) {
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        tv_distance = (TextView) view.findViewById(R.id.tv_distance);
        tv_speed = (TextView) view.findViewById(R.id.tv_speed);
        tv_signal = (TextView) view.findViewById(tv_kcal);
        blurImage = (ImageView) view.findViewById(R.id.iv_wallpapger);
        blurImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickCount++;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (ClickCount == 2) {
                            lockLayer.unlock();
                            finish();
                        } else {
                            ToastUtil.show("双击查看详情");
                            ClickCount = 0;
                        }
                    }
                }, 600);
            }
        });
//        Drawable drawable = WallpaperUtil.getWallpagerDrawable(LockActivity.this);
//        if (drawable != null) {
//            blurImage.setImageDrawable(drawable);
//        }
    }

    public void registerReceiver() {
        final String SYSTEM_REASON = "reason";
        final String SYSTEM_HOME_KEY = "homekey";
        final String SYSTEM_HOME_KEY_LONG = "recentapps";
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Configs.Broadcast_Receiver_Time);
        intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        if (null == receiver) {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (action.equals(Configs.Broadcast_Receiver_Time)) {
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
                    } else if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                        String reason = intent.getStringExtra(SYSTEM_REASON);
                        if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
                            //表示按了home键,程序到了后台
                            ToastUtil.show("home");
                            Logger.e(TAG, "home");
                        } else if (TextUtils.equals(reason, SYSTEM_HOME_KEY_LONG)) {
                            //表示长按home键,显示最近使用的程序列表
                            ToastUtil.show("long home");
                            Logger.e(TAG, "long home");
                        }
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
