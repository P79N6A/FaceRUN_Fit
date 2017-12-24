package com.fly.run.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.trace.TraceLocation;
import com.baidu.speech.VoiceRecognitionService;
import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizeBag;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.activity.circle.CirclePublishActivity;
import com.fly.run.activity.lock.Lock2Activity;
import com.fly.run.activity.map.smooth.SmoothMoveActivity;
import com.fly.run.activity.setting.SportModeActivity;
import com.fly.run.bean.NearByBean;
import com.fly.run.bean.RunBean;
import com.fly.run.config.AppConstants;
import com.fly.run.config.Configs;
import com.fly.run.config.Constant;
import com.fly.run.db.helper.RunDBHelper;
import com.fly.run.fragment.RunDataFragment;
import com.fly.run.fragment.RunMapFragment;
import com.fly.run.httptask.HttpTaskUtil;
import com.fly.run.service.LockService;
import com.fly.run.utils.AudioManagerUtil;
import com.fly.run.utils.LocationUtils;
import com.fly.run.utils.Logger;
import com.fly.run.utils.MediaPlayerUtil;
import com.fly.run.utils.MyOrientationListener;
import com.fly.run.utils.NetWorkUtil;
import com.fly.run.utils.ObjectAnimUtil;
import com.fly.run.utils.OkHttpClientManager;
import com.fly.run.utils.SharePreferceTool;
import com.fly.run.utils.TimeFormatUtils;
import com.fly.run.utils.ToastUtil;
import com.fly.run.view.CircleAnimView;
import com.fly.run.view.RunTrainView;
import com.fly.run.view.actionbar.MainRunActionBar;
import com.fly.run.view.viewpager.CustomViewPager;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.fly.run.utils.TimeFormatUtils.retainTwo;
import static com.fly.run.view.CircleAnimView.TYPE_RUN_KEEP;

public class MainRunActivity extends BaseUIActivity implements SpeechSynthesizerListener, RecognitionListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private CustomViewPager mViewPager;
    private MainRunActionBar actionBar;
    private RunDataFragment runDataFragment;
    private RunMapFragment runMapFragment;
    private CircleAnimView circleAnimView;
    private RunTrainView runTrainView;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = new AMapLocationClientOption();
    private AMapLocation aMapLocation;
    public boolean isRun = false;

    private Timer mTimer;
    private TimerTask mTimerTask;
    private Handler handler = new Handler();
    private int TYPE_RUN_ACTION = -1;

    private int SportModel = 0;
    private int LimitingSpeed = 8;

    private LockService.TrainBinder binder;

    private MediaPlayerUtil mediaPlayerUtil;
    private boolean isAudioConnect = false;

    private MyOrientationListener myOrientationListener;
    private float mCurrentX = 0;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (LockService.TrainBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private BroadcastReceiver receiver;

    public void registerReceiver() {
        final String SYSTEM_REASON = "reason";
        final String SYSTEM_HOME_KEY = "homekey";
        final String SYSTEM_LOCK = "lock";
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction("android.intent.action.HEADSET_PLUG"); //耳机是否插入
        intentFilter.addAction(Configs.Broadcast_Receiver_Begin_Run);
        intentFilter.addAction(Configs.Broadcast_Receiver_Over_Run);
        if (null == receiver) {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                        String reason = intent.getStringExtra(SYSTEM_REASON);
                        if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
                            //表示按了home键,程序到了后台
                            Logger.e(TAG, "home");
                        } else if (TextUtils.equals(reason, SYSTEM_LOCK)) {
                            lockJumpLock();
                        }
                    } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                        lockJumpLock();
                        Logger.e(TAG, "off");
                    } else if (action.equals(Intent.ACTION_SCREEN_ON)) {
                        Logger.e(TAG, "on");
                    } else if (action.equals("android.intent.action.HEADSET_PLUG")) {
                        if (AudioManagerUtil.checkAudioConnect(MainRunActivity.this)) {
                            isAudioConnect = true;
                            //play
                            playMusic();
                        } else {
                            isAudioConnect = false;
                            //pause
                            mediaPlayerUtil.pauseMedia();
                        }
                    } else if (action.equals(Configs.Broadcast_Receiver_Begin_Run)) {
                        wakeUpBeginRUN();
                    } else if (action.equals(Configs.Broadcast_Receiver_Over_Run)) {
                        wakeUpOverRUN();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_run);
//        startService(MainRunActivity.this);
        myOrientationListener = new MyOrientationListener(this);
        myOrientationListener.setmOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x, float y) {
                mCurrentX = x;
            }
        });
        myOrientationListener.star();
        SportModel = SharePreferceTool.getInstance().getInt("SportModel", 0);
        LimitingSpeed = SportModeActivity.LimitingSpeed[SportModel];
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (CustomViewPager) findViewById(R.id.container);
        mViewPager.setScanScroll(false);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(pageChangeListener);
        actionBar = (MainRunActionBar) findViewById(R.id.action_bar);
        actionBar.setActionLeftListenr(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TYPE_RUN_ACTION > 0) {
                    showAlertSaveTrain();
                    return;
                }
                finish();
            }
        });
        actionBar.setActionRightListenr(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToActivity(CirclePublishActivity.class);
            }
        });
        actionBar.setSectionListener(new MainRunActionBar.SectionListener() {
            @Override
            public void selectSection(int position) {
                if (mViewPager.getCurrentItem() != position)
                    try {
                        mViewPager.setCurrentItem(position);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        });
        circleAnimView = (CircleAnimView) findViewById(R.id.view_circle_anim);
        circleAnimView.setClickListener(circlrClickListener);
        runTrainView = (RunTrainView) findViewById(R.id.view_run_train);

        //初始化tts
        new Thread(new Runnable() {
            @Override
            public void run() {
                mediaPlayerUtil = MediaPlayerUtil.getInstance();
                mediaPlayerUtil.initMediaPlayer();
                //注册广播
                registerReceiver();
                //初始化TTS
                initialTts();
                //初始化定位
                initLocation();
                startLocation();
            }
        }).start();

//        initWakeUpGroup();
//        initSpeechRecognizer();

        initTimer();
        if (getIntent() != null
                && getIntent().getExtras() != null
                && getIntent().getExtras().containsKey("StartRunNow")) {
            boolean startRnNow = getIntent().getExtras().getBoolean("StartRunNow", false);
            if (startRnNow)
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        wakeUpBeginRUN();
                    }
                }, 800);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (runMapFragment != null) {
            runMapFragment.setMapFocusable();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void setTrainNowData(String distance, String time, String speed, String kcal) {
        if (!TextUtils.isEmpty(distance))
            runTrainView.setTvDistanceText(distance);
        if (!TextUtils.isEmpty(time))
            runTrainView.setTvTimeText(time);
        if (!TextUtils.isEmpty(speed))
            runTrainView.setTvSpeedText(speed);
        if (!TextUtils.isEmpty(kcal))
            runTrainView.setTvHeatText(kcal);
    }

    @Override
    public void onBackPressed() {
        if (TYPE_RUN_ACTION > 0) {
            showAlertSaveTrain();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setOfflineTask();
        if (myOrientationListener != null)
            myOrientationListener.stop();
        if (mSpeechSynthesizer != null)
            mSpeechSynthesizer.release();
        if (speechRecognizer != null)
            speechRecognizer.destroy();
        unRegisterReceiver();
//        if (mediaPlayerUtil != null) {
//            mediaPlayerUtil.pauseMedia();
//            mediaPlayerUtil.resetMedia();
//            mediaPlayerUtil.releaseMediaPlayer();
//            mediaPlayerUtil = null;
//        }
//        stopService(MainRunActivity.this);
        destroyLocation();
        destoryTimer();
    }

    protected void wakeUpBeginRUN() {
        if (!isRun) {
            circleAnimView.setTYPE_RUN_NOMAL(CircleAnimView.TYPE_RUN_KEEP);
            startRunView();
        }
    }

    protected void wakeUpOverRUN() {
        if (TYPE_RUN_ACTION > 0) {
            saveLocLogData();
            speak("跑步已结束");
        } else {
            speak("没有开始，何来结束");
        }
    }

    private void lockJumpLock() {
        if (!isRun)
            return;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i1 = new Intent(MainRunActivity.this, Lock2Activity.class);
                i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i1);
            }
        }, 220);
    }

    CircleAnimView.ClickListener circlrClickListener = new CircleAnimView.ClickListener() {
        @Override
        public void doAction(int action) {
            TYPE_RUN_ACTION = action;
            if (action == TYPE_RUN_KEEP) {
                startRunView();
            } else if (action == CircleAnimView.TYPE_RUN_PAUSE) {
                circleAnimView.setTvRText("");
            } else if (action == CircleAnimView.TYPE_RUN_OVER) {
                showAlertSaveTrain();
            }
        }
    };

    private void startRunView() {
        TYPE_RUN_ACTION = TYPE_RUN_KEEP;
        if (startLocationTime == 0)
            startLocationTime = System.currentTimeMillis();
        isRun = true;
        circleAnimView.setTvRText("结束");
        if (binder != null)
            binder.actionDo(true);
        ObjectAnimUtil.alphaAnim(runTrainView, 0.0f, 1.0f);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                speak("跑步已开始");
            }
        }, 810);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0) {
                runMapFragment = RunMapFragment.newInstance("", "");
                return runMapFragment;
            } else {
                runDataFragment = RunDataFragment.newInstance("", "");
                return runDataFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION Map";
                case 1:
                    return "SECTION DATA";
            }
            return null;
        }
    }

    private int pager_state = 0;
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (pager_state != 0) {
                if (positionOffset == 0)
                    return;
                float mapOffset = 1 - positionOffset;
                runMapFragment.getMapView().setAlpha(mapOffset);
                runDataFragment.getIvBg().setAlpha(positionOffset);
            }
        }

        @Override
        public void onPageSelected(int position) {
            actionBar.changeTopSectionView(position);
            if (position == 0 && runMapFragment != null) {
                runMapFragment.setMapFocusable();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            /**
             * 1、按住viewpager滑动状态；
             * 2、释放后viewpager仍在滑动状态
             * 0、滑动结束
             * */
            pager_state = state;
        }
    };

    /**
     * 初始化定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    // 根据控件的选择，重新设置定位参数
    private void resetOption() {
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(false);
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(true);
        // 设置是否单次定位
        locationOption.setOnceLocation(false);
        //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
        locationOption.setOnceLocationLatest(false);
        //设置是否使用传感器
        locationOption.setSensorEnable(false);
        //设置是否开启wifi扫描，如果设置为false时同时会停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        try {
            // 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
            locationOption.setInterval(Long.valueOf(2000));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            // 设置网络请求超时时间
            locationOption.setHttpTimeOut(30 * 1000);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

//    boolean first = true;

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            if (null != loc) {
                aMapLocation = loc;
//                if (first) {
//                    first = false;
//                }
                if (runMapFragment != null)
                    runMapFragment.move2MyLocation();
                //解析定位结果
//                String result = LocationUtils.getLocationStr(loc);
                recordPosition(loc);
            } else {
                Logger.e(TAG, "定位失败");
                if (runDataFragment != null)
                    setTrainNowData("", "", "", "定位失败");
            }
        }
    };

    public List<TraceLocation> mTraceList = new ArrayList<>();
    public List<LatLng> traceMapList = new ArrayList<>();
    public AMapLocation startAMapLocation;
    public AMapLocation endAMapLocation;
    private boolean isRunFirst = true;
    private boolean isRunLast = false;
    public double mAllDistance = 0;
    private JSONArray locArray;
    private JSONArray usableArray;

    private synchronized void recordPosition(AMapLocation loc) {
        if (isRun) {
            if (loc.getErrorCode() != 0) {
                if (runDataFragment != null)
                    setTrainNowData("", "", "", "定位失败");
                return;
            }
            if (isRunFirst) {
                isRunFirst = false;
                startAMapLocation = loc;
                locArray = new JSONArray();
            }
            if (isRunLast)
                return;
            int locationType = loc.getLocationType();
            String strLocType = LocationUtils.checkLocType(locationType);
//            if (runDataFragment != null)
//                setTrainNowData("", "", "", strLocType);

            /**
             * 1 GPS定位结果
             * 2 前次定位结果
             * 4 缓存定位结果
             * 5 Wifi定位结果
             * 6 基站定位结果
             * 8 离线定位结果
             * */
//            if (locationType == 2 || locationType == 4 || locationType == 5 || loc.getAccuracy() > 10) {
//                return;
//            }
            if (locationType != 1) {
                if (runDataFragment != null) {
                    String strSpeed = LocationUtils.caculateSpeed(mAllDistance, (System.currentTimeMillis() - startLocationTime));
                    setTrainNowData(retainTwo(mAllDistance / 1000f), "", strSpeed, strLocType);
                }
                return;
            }

//            LatLng mPreLatLng = null;
            LatLng mLatLng = new LatLng(loc.getLatitude(), loc.getLongitude());
//            TraceLocation traceLocation = new TraceLocation();
//            traceLocation.setLatitude(loc.getLatitude());
//            traceLocation.setLongitude(loc.getLongitude());
//            mTraceList.add(traceLocation);
//            if (mTraceList.size() >= 2) {
//                TraceLocation traceLocation1 = mTraceList.get(mTraceList.size() - 2);
//                mPreLatLng = new LatLng(traceLocation1.getLatitude(), traceLocation1.getLongitude());
//            }
            double oneDistance = 0;
//            if (mPreLatLng != null) {
//                oneDistance = AMapUtils.calculateLineDistance(mLatLng, mPreLatLng);
//                if (oneDistance > LimitingSpeed * 2) {
//                    return;
//                }
//            }
            traceMapList.add(mLatLng);
            LatLng realPreLatLon = null;
            if (traceMapList.size() > 2) {
                realPreLatLon = traceMapList.get(traceMapList.size() - 2);
                oneDistance = AMapUtils.calculateLineDistance(mLatLng, realPreLatLon);
            }

            mAllDistance += oneDistance;
            if (runDataFragment != null) {
                String strSpeed = LocationUtils.caculateSpeed(mAllDistance, (System.currentTimeMillis() - startLocationTime));
                setTrainNowData(retainTwo(mAllDistance / 1000f), "", strSpeed, strLocType);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mLat", loc.getLatitude());
            jsonObject.put("mLon", loc.getLongitude());
            locArray.add(jsonObject);

            // 运动状态下更新跑步位置
            setMyLocationOnline(loc);
        }
    }

    private boolean isCallBack = true;
    private List<NearByBean> listRunner;
    private HttpTaskUtil httpTaskUtil;
    private int httpErrorCount = 0;

    private void setMyLocationOnline(AMapLocation loc) {
        if (!isCallBack)
            return;
        if (!NetWorkUtil.haveNetWork(this))
            return;
        isCallBack = false;
        double lat = loc.getLatitude();
        double lon = loc.getLongitude();
        NearByBean runner = new NearByBean();
        runner.setAddress(loc.getAddress())
                .setLatitude(lat)
                .setLongitude(lon)
                .setOnLine(true);
        if (httpTaskUtil == null)
            httpTaskUtil = new HttpTaskUtil();
        httpTaskUtil.httpOnLineLocalTask(runner, new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Logger.e("httpLocalTask", "onFailure");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (httpErrorCount < 10)
                            isCallBack = true;
                        httpErrorCount++;
                    }
                }, 2001);
            }

            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    if (response.equals("no data")
                            || response.toLowerCase().startsWith("<html>")
                            || response.contains("not found")) {
                        Logger.e("no data");
                    } else {
                        try {
                            listRunner = JSON.parseArray(response, NearByBean.class);
                            runMapFragment.addMarkers(listRunner);
                            httpErrorCount = 0;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isCallBack = true;
                        }
                    }, 2001);
                }
            }
        });
    }

    private void setOfflineTask() {
        if (!isRun)
            return;
        if (httpTaskUtil == null)
            httpTaskUtil = new HttpTaskUtil();
        httpTaskUtil.httpOffLineLocalTask(new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Logger.e("httpLocalTask", "onFailure Offline");
            }

            @Override
            public void onResponse(String response) {
                Logger.e("httpLocalTask", "response = " + response);
            }
        });
    }

    /**
     * 发送广播给锁屏页面
     */
    private void sendBroadcast(String time, String distance, String speed, String kcal) {
        Intent intent = new Intent();
        intent.setAction(Configs.Broadcast_Receiver_Time);
        intent.putExtra("TIME", time);
        intent.putExtra("DISTANCE", distance);
        intent.putExtra("SPEED", speed);
        intent.putExtra("SIGNAL", kcal);
        sendBroadcast(intent);
    }

    /**
     * 数据保存
     */
    private synchronized void saveLocLogData() {
        isRunLast = true;
        if (locArray != null && locArray.size() > 0) {
            AMapLocation location = null;
            if (startAMapLocation != null)
                location = startAMapLocation;
            else if (aMapLocation != null)
                location = aMapLocation;
            boolean insertDB = false;
            String strPointsData = locArray.toString();
            RunBean bean = null;
            if (location != null) {
                bean = new RunBean();
                bean.setmLat(location.getLatitude())
                        .setmLon(location.getLongitude())
                        .setmAddress(location.getAddress())
                        .setmNearBy(location.getPoiName())
                        .setmRunDate(String.valueOf(startLocationTime))
                        .setmRunDistance(String.valueOf(mAllDistance))
                        .setmRunHeat("")
                        .setmUseTime(String.valueOf(System.currentTimeMillis() - startLocationTime))
                        .setmRunSpeed("")
                        .setmRunCoordinateList(strPointsData);
                try {
                    insertDB = RunDBHelper.insert(bean);
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtil.show("数据库保存失败");
                }
            }
            if (insertDB && bean != null) {
                if (getIntent() != null && getIntent().getExtras() != null) {
                    boolean isLevel = this.getIntent().getExtras().containsKey("LEVEL");
                    if (isLevel) {
                        int level = this.getIntent().getExtras().getInt("LEVEL", 1);
                        level++;
                        SharePreferceTool.getInstance().setCache("LEVEL", level);
                    }
                }
                Intent intent = new Intent(MainRunActivity.this, SmoothMoveActivity.class);
                intent.putExtra("Bean", bean);
                intent.putExtra("IsTrain", true);
                startActivity(intent);
            }
        }
        finish();
    }

    public AMapLocation getaMapLocation() {
        return aMapLocation;
    }

    /**
     * 开始定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void startLocation() {
//        根据控件的选择，重新设置定位参数
//        resetOption();
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 停止定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void stopLocation() {
        // 停止定位
        locationClient.stopLocation();
    }

    /**
     * 销毁定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    public long startLocationTime;
    public long nowLocationTime;

    /**
     * 初始化并开始计时
     */
    private void initTimer() {
        if (mTimer == null)
            mTimer = new Timer();
        if (mTimerTask == null)
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    if (TYPE_RUN_ACTION == -1 && startLocationTime == 0)
                        return;
                    if (runDataFragment != null)
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                nowLocationTime = System.currentTimeMillis();
                                long useTime = nowLocationTime - startLocationTime;
                                String strUseTime = TimeFormatUtils.formatDurationHHMMss(nowLocationTime - startLocationTime);
                                setTrainNowData("", strUseTime, "", "");
                                String strPreSpeed = LocationUtils.caculateSpeedMin(mAllDistance, useTime);
                                String strLocType = "";
                                if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                                    int locationType = aMapLocation.getLocationType();
                                    strLocType = LocationUtils.checkLocType(locationType);
                                } else {
                                    strLocType = "定位失败";
                                }
                                sendBroadcast(strUseTime, retainTwo(mAllDistance / 1000f), strPreSpeed, strLocType);
                            }
                        });
                }
            };
        mTimer.schedule(mTimerTask, 10, 1000);
    }

    /**
     * 销毁计时器
     */
    private void destoryTimer() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

    }


    /**
     * 退出提示
     */
    AlertDialog.Builder builder;

    private void showAlertSaveTrain() {
        handler.postDelayed(new Runnable() {
            @SuppressLint("NewApi")
            @Override
            public void run() {
                String message = mAllDistance > 200 ? "本次跑步距离" + retainTwo(mAllDistance / 1000f) : "本次跑步距离太短，是否退出";
                String mStrSave = mAllDistance > 200 ? "保存" : "退出";
                if (builder == null)
                    builder = new AlertDialog.Builder(MainRunActivity.this);
                builder.setTitle("保存数据");
                builder.setMessage(message);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setPositiveButton(mStrSave, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                saveLocLogData();
                            }
                        }, 200);
                    }
                });
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                    }
                });
                builder.create();
                builder.show();
            }
        }, 200);
    }

    /**
     * 启动服务
     */
    public boolean startService(Context context) {
        try {
            context.startService(buildIntent(context));
            boolean bind = bindService(buildIntent(context), connection, BIND_AUTO_CREATE);
            return bind;
        } catch (Exception e) {
            Logger.e("RunTrainActivity", "startService");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 停止服务
     */
    public boolean stopService(Context context) {
        try {
            boolean unregister = unRegisterService(context);
            boolean stop = context.stopService(buildIntent(context));
            return stop;
        } catch (Exception e) {
            Logger.e("RunTrainActivity", "stopService");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 接触服务绑定
     */
    public boolean unRegisterService(Context context) {
        if (connection != null) {
            context.unbindService(connection);
            connection = null;
            Logger.e("RunTrainActivity", "unbindService");
            return true;
        }
        return false;
    }

    // 构建intent
    private Intent buildIntent(Context context) throws Exception {
        Intent intent = new Intent();
        intent.setClass(context, LockService.class);
        return intent;
    }


    private String strWakeUpWord = "";

    private SpeechSynthesizer mSpeechSynthesizer;

    /**
     * 初始化语音解读（TTS）
     */
    private void initialTts() {
        this.mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        this.mSpeechSynthesizer.setContext(this);
        this.mSpeechSynthesizer.setSpeechSynthesizerListener(this);
        // 文本模型文件路径 (离线引擎使用)
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, AppConstants.mSampleDirPath + "/"
                + AppConstants.TEXT_MODEL_NAME);
        // 声学模型文件路径 (离线引擎使用)
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, AppConstants.mSampleDirPath + "/"
                + AppConstants.SPEECH_MALE_MODEL_NAME);
        // 本地授权文件路径,如未设置将使用默认路径.设置临时授权文件路径，LICENCE_FILE_NAME请替换成临时授权文件的实际路径，仅在使用临时license文件时需要进行设置，如果在[应用管理]中开通了正式离线授权，不需要设置该参数，建议将该行代码删除（离线引擎）
        // 如果合成结果出现临时授权文件将要到期的提示，说明使用了临时授权文件，请删除临时授权即可。
//        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_LICENCE_FILE, AppConstants.mSampleDirPath + "/"
//                + AppConstants.LICENSE_FILE_NAME);
        // 请替换为语音开发者平台上注册应用得到的App ID (离线授权)
        this.mSpeechSynthesizer.setAppId("9357855"/*这里只是为了让Demo运行使用的APPID,请替换成自己的id。*/); // 9033148 -- 8535996
        // 请替换为语音开发者平台注册应用得到的apikey和secretkey (在线授权)
        this.mSpeechSynthesizer.setApiKey("h1zxp5e1ZSaGfQvPeHgGeIHBSjn1QC2b",
                "u7l3qXoODc85KlE6GSNwyrHAurwKDYny"/*这里只是为了让Demo正常运行使用APIKey,请替换成自己的APIKey*/);
        // 发音人（在线引擎），可用参数为0,1,2,3。。。（服务器端会动态增加，各值含义参考文档，以文档说明为准。0--普通女声，1--普通男声，2--特别男声，3--情感男声。。。）
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "2");
        // 设置Mix模式的合成策略
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
        // 授权检测接口(只是通过AuthInfo进行检验授权是否成功。)
        // AuthInfo接口用于测试开发者是否成功申请了在线或者离线授权，如果测试授权成功了，可以删除AuthInfo部分的代码（该接口首次验证时比较耗时），不会影响正常使用（合成使用时SDK内部会自动验证授权）
        boolean isAuth = SharePreferceTool.getInstance().getBoolean("Auth");
        if (!isAuth) {
            AuthInfo authInfo = this.mSpeechSynthesizer.auth(TtsMode.MIX);
            if (authInfo.isSuccess()) {
                SharePreferceTool.getInstance().setCache("Auth", true);
                Logger.e(TAG, "auth success");
            } else {
                String errorMsg = authInfo.getTtsError().getDetailMessage();
            }
        }
        // 初始化tts
        mSpeechSynthesizer.initTts(TtsMode.MIX);
        // 加载离线英文资源（提供离线英文合成功能）
        int result =
                mSpeechSynthesizer.loadEnglishModel(AppConstants.mSampleDirPath + "/" + AppConstants.ENGLISH_TEXT_MODEL_NAME, AppConstants.mSampleDirPath
                        + "/" + AppConstants.ENGLISH_SPEECH_MALE_MODEL_NAME);
    }

    /**
     * 单句文本朗读
     */
    private void speak(final String message) {
        if (TextUtils.isEmpty(message))
            return;
        if (isSpeeking)
            return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                mediaPlayerUtil.pauseMedia();
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //需要合成的文本text的长度不能超过1024个GBK字节。
                isBatchSpeek = false;
                int result = mSpeechSynthesizer.speak(message);
                if (result < 0) {
                    Logger.e(TAG, "error,please look up error code in doc or URL:http://yuyin.baidu.com/docs/tts/122 ");
                }
            }
        }, 200);
    }

    /**
     * 多句文本合成朗读
     */
    private void batchDataSpeak() {
        if (!isRun || startLocationTime == 0) {
            speak("请开始跑步");
            return;
        }
        if (isSpeeking)
            return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                mediaPlayerUtil.pauseMedia();
            }
        });
        String strUseTime = TimeFormatUtils.formatDurationHHMMss(nowLocationTime - startLocationTime);
        String strPreSpeed = mAllDistance == 0 ? "0" : LocationUtils.caculateSpeedMin(mAllDistance, (System.currentTimeMillis() - startLocationTime)) + "分钟／公里";
        String strDistance = mAllDistance == 0 ? "0" : TimeFormatUtils.retainTwo(mAllDistance / 1000f) + "公里";
        Logger.e(TAG, "用时:" + strUseTime);
        final List<SpeechSynthesizeBag> bags = new ArrayList<SpeechSynthesizeBag>();
        bags.add(getSpeechSynthesizeBag("用时", "0"));
        bags.add(getSpeechSynthesizeBag(strUseTime, "1"));
        bags.add(getSpeechSynthesizeBag("里程", "2"));
        bags.add(getSpeechSynthesizeBag(strDistance, "3"));
        bags.add(getSpeechSynthesizeBag("配速", "4"));
        bags.add(getSpeechSynthesizeBag(strPreSpeed, "5"));
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isBatchSpeek = true;
                int result = mSpeechSynthesizer.batchSpeak(bags);
                if (result < 0) {
                    Logger.e(TAG, "error,please look up error code in doc or URL:http://yuyin.baidu.com/docs/tts/122 ");
                }
            }
        }, 200);

    }

    private SpeechSynthesizeBag getSpeechSynthesizeBag(String text, String utteranceId) {
        SpeechSynthesizeBag speechSynthesizeBag = new SpeechSynthesizeBag();
        //需要合成的文本text的长度不能超过1024个GBK字节。
        speechSynthesizeBag.setText(text);
        speechSynthesizeBag.setUtteranceId(utteranceId);
        return speechSynthesizeBag;
    }

    boolean isBatchSpeek = false;
    boolean isSpeeking = false;

    @Override
    public void onSynthesizeStart(String s) {
    }

    @Override
    public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {
    }

    @Override
    public void onSynthesizeFinish(String s) {
    }

    @Override
    public void onSpeechStart(String s) {
        Logger.e(TAG, "onSpeechStart = " + s);
        isSpeeking = true;
    }

    @Override
    public void onSpeechProgressChanged(String s, int i) {
    }

    @Override
    public void onSpeechFinish(String s) {
        Logger.e(TAG, "onSpeechFinish = " + s);
        isSpeeking = false;
        if (strWakeUpWord.equals("巴弟快跑"))
            return;
        if (isBatchSpeek) {
            if (!TextUtils.isEmpty(s) && s.equalsIgnoreCase("5"))
                playMusic();
        } else
            playMusic();
        isBatchSpeek = false;
//        strWakeUpWord = "";
    }

    @Override
    public void onError(String s, SpeechError speechError) {
        Logger.e(TAG, "error = " + s);
        strWakeUpWord = "";
        isSpeeking = false;
        isBatchSpeek = false;
        playMusic();
    }

    private void playMusic() {
        if (isAudioConnect)
            mediaPlayerUtil.playRandomMusic();
    }

    private SpeechRecognizer speechRecognizer;
    private Intent intentSpeechRecognizer = null;
    private long speechEndTime = -1;
    private static final int EVENT_ERROR = 11;
    private static final int REQUEST_UI = 1;
    private long time;
    public static final int STATUS_None = 0;
    public static final int STATUS_WaitingReady = 2;
    public static final int STATUS_Ready = 3;
    public static final int STATUS_Speaking = 4;
    public static final int STATUS_Recognition = 5;
    private int status = STATUS_None;

    /**
     * 初始化语音识别
     */
    private void initSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this, new ComponentName(this, VoiceRecognitionService.class));
        speechRecognizer.setRecognitionListener(this);
    }


    @Override
    public void onReadyForSpeech(Bundle params) {
        status = STATUS_Ready;
    }

    @Override
    public void onBeginningOfSpeech() {
        time = System.currentTimeMillis();
        status = STATUS_Speaking;
    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {
        speechEndTime = System.currentTimeMillis();
        status = STATUS_Recognition;
    }

    @Override
    public void onError(int error) {
        status = STATUS_None;
        time = 0;
        StringBuilder sb = new StringBuilder();
        switch (error) {
            case SpeechRecognizer.ERROR_AUDIO:
                sb.append("音频问题");
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                sb.append("没有语音输入");
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                sb.append("其它客户端错误");
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                sb.append("权限不足");
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                sb.append("网络问题");
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                sb.append("没有匹配的识别结果");
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                sb.append("引擎忙");
                break;
            case SpeechRecognizer.ERROR_SERVER:
                sb.append("服务端错误");
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                sb.append("连接超时");
                break;
        }
        Logger.e(TAG, sb.toString());
        if (strWakeUpWord.equals("巴弟快跑")) {
            strWakeUpWord = "";
            playMusic();
        }
    }

    @Override
    public void onResults(Bundle results) {
//        long end2finish = System.currentTimeMillis() - speechEndTime;
        status = STATUS_None;
        ArrayList<String> nbest = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String json_res = results.getString("origin_result");
        boolean isSelect = false;
        if (nbest == null || nbest.size() == 0)
            return;
        for (String content : nbest) {
            if (TextUtils.isEmpty(content))
                continue;
            if (content.contains("播放音乐") || content.contains("播放")) {
                isSelect = true;
                playMusic();
                break;
            } else if (content.contains("暂停音乐")
                    || content.contains("关闭音乐")
                    || content.contains("暂停")
                    || content.contains("关闭")) {
                isSelect = true;
                mediaPlayerUtil.pauseMedia();
                break;
            }
            if (isRun) {
                if (content.contains("用时") || content.contains("耗时")) {
                    isSelect = true;
                    batchDataSpeak();
                } else if (content.contains("里程") || content.contains("距离")) {
                    isSelect = true;
                    batchDataSpeak();
                } else if (content.contains("速度") || content.contains("配速")) {
                    isSelect = true;
                    batchDataSpeak();
                }
            }
        }
        if (!isSelect) {
            if (strWakeUpWord.equals("巴弟快跑")) {
                strWakeUpWord = "";
                playMusic();
            }
        }
        Logger.e(TAG, "json_res = " + json_res);
//        String strEnd2Finish = "";
//        if (end2finish < 60 * 1000) {
//            strEnd2Finish = "(waited " + end2finish + "ms)";
//        }
        time = 0;
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        switch (eventType) {
            case EVENT_ERROR:
                String reason = params.get("reason") + "";
                Logger.e(TAG, "reason = " + reason);
                break;
            case VoiceRecognitionService.EVENT_ENGINE_SWITCH:
                int type = params.getInt("engine_type");
                Logger.e(TAG, "*引擎切换至" + (type == 0 ? "在线" : "离线"));
                break;
        }
    }


    private void startSpeechRecognizer() {
//        if (intentSpeechRecognizer == null)
//            intentSpeechRecognizer = bindParams(new Intent());
        Intent intent = new Intent();
        bindParams(intent);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        {

            String args = sp.getString("args", "");
            if (null != args) {
                intent.putExtra("args", args);
            }
        }
//        boolean api = sp.getBoolean("api", false);
//        if (api) {
        speechEndTime = -1;
        speechRecognizer.startListening(intent);
//        } else {
//            intent.setAction("com.baidu.action.RECOGNIZE_SPEECH");
//            startActivityForResult(intent, REQUEST_UI);
//        }
    }

    private void stop() {
        speechRecognizer.stopListening();
    }

    private void cancel() {
        speechRecognizer.cancel();
        status = STATUS_None;
    }

    public Intent bindParams(Intent intent) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.getBoolean("tips_sound", true)) {
            intent.putExtra(Constant.EXTRA_SOUND_START, R.raw.bdspeech_recognition_start);
            intent.putExtra(Constant.EXTRA_SOUND_END, R.raw.bdspeech_speech_end);
            intent.putExtra(Constant.EXTRA_SOUND_SUCCESS, R.raw.bdspeech_recognition_success);
            intent.putExtra(Constant.EXTRA_SOUND_ERROR, R.raw.bdspeech_recognition_error);
            intent.putExtra(Constant.EXTRA_SOUND_CANCEL, R.raw.bdspeech_recognition_cancel);
        }
        if (sp.contains(Constant.EXTRA_INFILE)) {
            String tmp = sp.getString(Constant.EXTRA_INFILE, "").replaceAll(",.*", "").trim();
            intent.putExtra(Constant.EXTRA_INFILE, tmp);
        }
        if (sp.getBoolean(Constant.EXTRA_OUTFILE, false)) {
            intent.putExtra(Constant.EXTRA_OUTFILE, "sdcard/outfile.pcm");
        }
        if (sp.getBoolean(Constant.EXTRA_GRAMMAR, false)) {
            intent.putExtra(Constant.EXTRA_GRAMMAR, "assets:///baidu_speech_grammar.bsg");
        }
        if (sp.contains(Constant.EXTRA_SAMPLE)) {
            String tmp = sp.getString(Constant.EXTRA_SAMPLE, "").replaceAll(",.*", "").trim();
            if (null != tmp && !"".equals(tmp)) {
                intent.putExtra(Constant.EXTRA_SAMPLE, Integer.parseInt(tmp));
            }
        }
        if (sp.contains(Constant.EXTRA_LANGUAGE)) {
            String tmp = sp.getString(Constant.EXTRA_LANGUAGE, "").replaceAll(",.*", "").trim();
            if (null != tmp && !"".equals(tmp)) {
                intent.putExtra(Constant.EXTRA_LANGUAGE, tmp);
            }
        }
        if (sp.contains(Constant.EXTRA_NLU)) {
            String tmp = sp.getString(Constant.EXTRA_NLU, "").replaceAll(",.*", "").trim();
            if (null != tmp && !"".equals(tmp)) {
                intent.putExtra(Constant.EXTRA_NLU, tmp);
            }
        }

        if (sp.contains(Constant.EXTRA_VAD)) {
            String tmp = sp.getString(Constant.EXTRA_VAD, "").replaceAll(",.*", "").trim();
            if (null != tmp && !"".equals(tmp)) {
                intent.putExtra(Constant.EXTRA_VAD, tmp);
            }
        }
        String prop = null;
        if (sp.contains(Constant.EXTRA_PROP)) {
            String tmp = sp.getString(Constant.EXTRA_PROP, "").replaceAll(",.*", "").trim();
            if (null != tmp && !"".equals(tmp)) {
                intent.putExtra(Constant.EXTRA_PROP, Integer.parseInt(tmp));
                prop = tmp;
            }
        }

//        // offline asr
//        {
//            intent.putExtra(Constant.EXTRA_OFFLINE_ASR_BASE_FILE_PATH, "/sdcard/easr/s_1");
//            if (null != prop) {
//                int propInt = Integer.parseInt(prop);
//                if (propInt == 10060) {
//                    intent.putExtra(Constant.EXTRA_OFFLINE_LM_RES_FILE_PATH, "/sdcard/easr/s_2_Navi");
//                } else if (propInt == 20000) {
//                    intent.putExtra(Constant.EXTRA_OFFLINE_LM_RES_FILE_PATH, "/sdcard/easr/s_2_InputMethod");
//                }
//            }
//            intent.putExtra(Constant.EXTRA_OFFLINE_SLOT_DATA, buildTestSlotData());
//        }
        return intent;
    }
}
