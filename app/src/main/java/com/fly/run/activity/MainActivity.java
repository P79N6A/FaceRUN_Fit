package com.fly.run.activity;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.VoiceRecognitionService;
import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.activity.circle.CircleActivity;
import com.fly.run.activity.login.LoginActivity;
import com.fly.run.activity.person.PersonInfoActivity;
import com.fly.run.activity.setting.SettingActivity;
import com.fly.run.activity.training.FitTimeActivity;
import com.fly.run.activity.training.TrainPlanActivity;
import com.fly.run.adapter.NavMainAdapter;
import com.fly.run.bean.AccountBean;
import com.fly.run.config.Constant;
import com.fly.run.db.helper.RunDBHelper;
import com.fly.run.fragment.dialog.DialogWeatherFragment;
import com.fly.run.manager.UserInfoManager;
import com.fly.run.utils.AudioManagerUtil;
import com.fly.run.utils.DisplayUtil;
import com.fly.run.utils.IOTools;
import com.fly.run.utils.Logger;
import com.fly.run.utils.MediaPlayerUtil;
import com.fly.run.utils.PowerManagerUtil;
import com.fly.run.utils.TimeFormatUtils;
import com.fly.run.utils.ToastUtil;
import com.fly.run.utils.WeatherUtil;
import com.fly.run.view.TextView.TextViewDrawable;
import com.fly.run.view.actionbar.CommonMainActionBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.fly.run.R.id.iv_run_begin;
import static com.fly.run.R.id.listview_menu;
import static com.fly.run.R.id.tv_all_distance;

public class MainActivity extends BaseUIActivity
        implements NavigationView.OnNavigationItemSelectedListener, RecognitionListener {

    private DrawerLayout drawer;
    private CommonMainActionBar actionBar;
    public double mAllDistance = 0;
    private RelativeLayout layoutMid1;
    private TextView tvAllDistance, tvNavAllDistance;

    private RelativeLayout layoutUserInfo;
    private ImageView navHeader;
    private TextView navUserName, navUserAccount;

    private EventManager mWpEventManager;
    private MediaPlayerUtil mediaPlayerUtil;

    private RelativeLayout layoutWeather;
    private TextView tvWeatherAir;
    private TextView tvWeatherC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化语音唤醒功能
//        initWakeUpGroup();
        //语音识别
        initSpeechRecognizer();
        mediaPlayerUtil = MediaPlayerUtil.getInstance();
        mediaPlayerUtil.initMediaPlayer();
        registerReceiver();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //将侧边栏顶部延伸至status bar
            drawer.setFitsSystemWindows(true);
            //将主页面顶部延伸至status bar;虽默认为false,但经测试,DrawerLayout需显示设置
            drawer.setClipToPadding(false);
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
        View navHeaderRoot = navigationView.inflateHeaderView(R.layout.nav_header_main);

        layoutUserInfo = (RelativeLayout) navHeaderRoot.findViewById(R.id.layout_user_info);
        tvNavAllDistance = (TextView) navHeaderRoot.findViewById(tv_all_distance);
        navUserName = (TextView) navHeaderRoot.findViewById(R.id.tv_name);
        navUserAccount = (TextView) navHeaderRoot.findViewById(R.id.tv_subname);
        navHeader = (ImageView) navHeaderRoot.findViewById(R.id.imageView);
//        ImageLoader.getInstance().displayImage("drawable://" + R.drawable.ic_user_header_default, navHeader, ImageLoaderOptions.optionsUserCornerHeader);
        layoutWeather = (RelativeLayout) navHeaderRoot.findViewById(R.id.layout_weather);
        tvWeatherAir = (TextView) navHeaderRoot.findViewById(R.id.tv_weather_air);
        tvWeatherC = (TextView) navHeaderRoot.findViewById(R.id.tv_weather_c);

        String path = Constant.UserConfigPath + "Account.info";
        AccountBean accountBean = (AccountBean) IOTools.readObject(path);
        if (accountBean != null) {
            UserInfoManager.getInstance().setAccountInfo(accountBean);
            if (accountBean != null) {
                if (!TextUtils.isEmpty(accountBean.getUsername()))
                    navUserName.setText(accountBean.getUsername());
                if (!TextUtils.isEmpty(accountBean.getAccount()))
                    navUserAccount.setText(accountBean.getAccount());
            }
        }
        layoutUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserInfoManager.getInstance().isLogin())
                    intentToActivity(PersonInfoActivity.class);
                else
                    intentToActivity(LoginActivity.class);
            }
        });
        ListView navListView = (ListView) navHeaderRoot.findViewById(listview_menu);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) navListView.getLayoutParams();
        params.height = DisplayUtil.screenHeight - DisplayUtil.dp2px(190) - DisplayUtil.getStatusBarHeight(this);
        navListView.setLayoutParams(params);
        NavMainAdapter navMainAdapter = new NavMainAdapter(this);
        navListView.setAdapter(navMainAdapter);
        navListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                handler.sendEmptyMessageDelayed(position, 0);
            }
        });
        actionBar = (CommonMainActionBar) findViewById(R.id.action_bar);
        actionBar.setActionLeftListenr(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        actionBar.setActionRightListenr(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToActivity(CircleActivity.class);
            }
        });
        if (WeatherUtil.weatherBean != null) {
            layoutWeather.setVisibility(View.VISIBLE);
            tvWeatherC.setText(WeatherUtil.weatherBean.getTemperature());
            tvWeatherAir.setText(WeatherUtil.weatherBean.getWeather());
            tvWeatherAir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogWeatherFragment dialogWeatherFragment = DialogWeatherFragment.newInstance("", "");
                    if (!dialogWeatherFragment.isVisible())
                        dialogWeatherFragment.show(getSupportFragmentManager(), "DialogWeatherFragment");
                }
            });
            String airCondition = WeatherUtil.weatherBean.getAirCondition();
            if (!TextUtils.isEmpty(airCondition)) {
                if ("优".equalsIgnoreCase(airCondition) || "良".equalsIgnoreCase(airCondition)) {
                    TextViewDrawable.setTextViewDrawableLeft(tvWeatherAir, this, R.drawable.aqi_excellent);
                } else if ("污染".equalsIgnoreCase(airCondition)
                        || "轻度污染".equalsIgnoreCase(airCondition)
                        || "中度污染".equalsIgnoreCase(airCondition)) {
                    TextViewDrawable.setTextViewDrawableLeft(tvWeatherAir, this, R.drawable.aqi_average);
                } else {
                    TextViewDrawable.setTextViewDrawableLeft(tvWeatherAir, this, R.drawable.aqi_poor);
                }
            }
        } else {
            layoutWeather.setVisibility(View.GONE);
        }

        ImageView ivRunBegin = (ImageView) findViewById(iv_run_begin);
//        ivRunBegin.setColorFilter(getResources().getColor(R.color.color_ffffff));
        ivRunBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainRunActivity.class);
                startActivity(intent);
            }
        });
        layoutMid1 = (RelativeLayout) findViewById(R.id.layout_mid_content_1);
        tvAllDistance = (TextView) findViewById(tv_all_distance);
        layoutMid1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToActivity(RecordActivity.class);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRunDataDB();
        Logger.e(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.e(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.e(TAG, "onStop");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return true;
        } else {
            PackageManager pm = getPackageManager();
            ResolveInfo homeInfo =
                    pm.resolveActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), 0);
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                ActivityInfo ai = homeInfo.activityInfo;
                Intent startIntent = new Intent(Intent.ACTION_MAIN);
                startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                startIntent.setComponent(new ComponentName(ai.packageName, ai.name));
                startActivitySafely(startIntent);
                return true;
            } else
                return super.onKeyDown(keyCode, event);
        }
    }

    private void startActivitySafely(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "null",
                    Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(this, "null",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
//        handler.sendEmptyMessage(111);
        int id = item.getItemId();
//        if (id == nav_routes) {
//        } else if (id == R.id.nav_location) {
//        } else if (id == R.id.nav_music) {
//        } else if (id == R.id.nav_setting) {
//
//        } else if (id == R.id.nav_introduce) {
//        } else if (id == R.id.nav_about_we) {
//        }
        handler.sendEmptyMessageDelayed(id, 0);
        return true;
    }

    private final int nav_routes = R.id.nav_routes;
    private final int nav_location = R.id.nav_location;
    private final int nav_music = R.id.nav_music;
    private final int nav_setting = R.id.nav_setting;
    private final int nav_introduce = R.id.nav_introduce;
    private final int nav_about_we = R.id.nav_about_we;

    //    private String[] navNames = {"我的行程", "位置可见", "音乐开关", "应用设置", "唤醒提示", "关于我们"};
    //    private String[] navNames = {"我的行程", "跑步训练", "Fit-Time", "应用设置", "唤醒提示", "关于我们"};

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = null;
            switch (msg.what) {
                case 0:
                    intentToActivity(RecordActivity.class);
                    break;
                case 1:
                    intentToActivity(TrainPlanActivity.class);
                    break;
                case 2:
                    intentToActivity(FitTimeActivity.class);
                    break;
                case 3:
                    intent = new Intent(MainActivity.this, SettingActivity.class);
                    startActivityForResult(intent, 1024);
                    break;
                case 4:
//                    intentToActivity(WakeUpAlertActivity.class);
                    break;
                case 5:
                    break;
                case 111:
                    /**
                     * 关闭左边导航栏
                     * */
                    drawer.closeDrawer(GravityCompat.START);
                    break;
            }
        }
    };

    private void loadRunDataDB() {
        mAllDistance = RunDBHelper.queryAllDistance(null);
        if (mAllDistance == 0) {
            layoutMid1.setVisibility(View.GONE);
            tvNavAllDistance.setVisibility(View.GONE);
        } else {
            layoutMid1.setVisibility(View.VISIBLE);
            tvNavAllDistance.setVisibility(View.VISIBLE);
            String strAllDistance = TimeFormatUtils.retainOne(mAllDistance / 1000f);
            tvAllDistance.setText(strAllDistance);
            tvNavAllDistance.setText("运动总里程 ：" + strAllDistance + "公里");
        }
    }


    @Override
    protected void wakeUpBADIRUN() {
        startSpeechRecognizer();
    }

//    @Override
//    protected void wakeUpBeginRUN() {
//
//    }
//
//    @Override
//    protected void wakeUpOverRUN() {
//
//    }

    @Override
    protected void wakeUpVoiceUp() {
        AudioManagerUtil.setAudioVolumeTemp(MainActivity.this, 1);
    }

    @Override
    protected void wakeUpVoiceDown() {
        AudioManagerUtil.setAudioVolumeTemp(MainActivity.this, 0);
    }

    @Override
    protected void wakeUpNext() {
        mediaPlayerUtil.playNextOne();
    }

    private void initWakeUpGroup() {
        // 唤醒功能打开步骤
        // 1) 创建唤醒事件管理器
        mWpEventManager = EventManagerFactory.create(MainActivity.this, "wp");
        // 2) 注册唤醒事件监听器
        mWpEventManager.registerListener(new EventListener() {
            @Override
            public void onEvent(String name, String params, byte[] data, int offset, int length) {
                Logger.e(TAG, String.format("event: name=%s, params=%s", name, params));
                try {
                    JSONObject json = new JSONObject(params);
                    if ("wp.data".equals(name)) { // 每次唤醒成功, 将会回调name=wp.data的时间, 被激活的唤醒词在params的word字段
                        String word = json.getString("word");
                        if (TextUtils.isEmpty(word))
                            return;
                        if (!PowerManagerUtil.isScreenOn(MainActivity.this)) {
                            PowerManagerUtil.brightScreen(MainActivity.this);
                        }
                        doVibrator(MainActivity.this);
                        voiceWakeUp(word);
                    } else if ("wp.exit".equals(name)) {
                    }
                } catch (JSONException e) {
                    throw new AndroidRuntimeException(e);
                }
            }
        });
        // 3) 通知唤醒管理器, 启动唤醒功能
        HashMap params = new HashMap();
        params.put("kws-file", "assets:///WakeUp.bin"); // 设置唤醒资源, 唤醒资源请到 http://yuyin.baidu.com/wake#m4 来评估和导出
        mWpEventManager.send("wp.start", new JSONObject(params).toString(), null, 0, 0);
//        txtLog.setText(DESC_TEXT);
    }

    private Vibrator vibrator;

    public void doVibrator(Context context) {
        /**
         * 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
         * */
        if (vibrator == null)
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(150);
    }

    @Override
    protected void onDestroy() {
        Logger.e(TAG, "destory");
        super.onDestroy();
        unRegisterReceiver();
        if (vibrator != null)
            vibrator.cancel();
        // 停止唤醒监听
        if (mWpEventManager != null)
            mWpEventManager.send("wp.stop", null, null, 0, 0);
        if (mediaPlayerUtil != null) {
            mediaPlayerUtil.pauseMedia();
            mediaPlayerUtil.resetMedia();
            mediaPlayerUtil.releaseMediaPlayer();
            mediaPlayerUtil = null;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1024) {
                AccountBean bean = UserInfoManager.getInstance().getAccountInfo();
                if (bean != null) {
                    if (!TextUtils.isEmpty(bean.getUsername()))
                        navUserName.setText(bean.getUsername());
                    if (!TextUtils.isEmpty(bean.getAccount()))
                        navUserAccount.setText(bean.getAccount());
                } else {
                    navUserName.setText("KEEP君");
                    navUserAccount.setText("KEEP-GONING@YOU");
                }
            }
        }
    }

    /**
     * 监听登录成功的Receiver
     */
    private BroadcastReceiver mLoginStateReceiver;
    public static final String Login_State_Success = "Login_State_Success";
    public static final String Login_State_Fail = "Login_State_Fail";
    public static final String Regist_State_Success = "Regist_State_Success";
    public static final String Regist_State_Fail = "Regist_State_Fail";

    public void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Login_State_Success);
        intentFilter.addAction(Login_State_Fail);
        intentFilter.addAction(Regist_State_Success);
        intentFilter.addAction(Regist_State_Fail);
        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_BATTERY_LOW);
        intentFilter.addAction(Intent.ACTION_BATTERY_OKAY);
        if (null == mLoginStateReceiver) {
            mLoginStateReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    switch (action) {
                        case Login_State_Success:
                        case Regist_State_Success:
                            AccountBean bean = UserInfoManager.getInstance().getAccountInfo();
                            if (bean != null) {
                                if (!TextUtils.isEmpty(bean.getUsername()))
                                    navUserName.setText(bean.getUsername());
                                if (!TextUtils.isEmpty(bean.getAccount()))
                                    navUserAccount.setText(bean.getAccount());
                            }
                            break;
                        case Login_State_Fail:
                            break;
                        case Regist_State_Fail:
                            break;
                        case Intent.ACTION_BATTERY_CHANGED:
//                            ToastUtil.show("Change");
                            break;
                        case Intent.ACTION_BATTERY_LOW:
                            ToastUtil.show("Low");
                            break;
                        case Intent.ACTION_BATTERY_OKAY:
                            ToastUtil.show("Okay");
                            break;
                        case "android.intent.action.HEADSET_PLUG":
                            if (AudioManagerUtil.checkAudioConnect(MainActivity.this)) {
                                //play
                                if (!TextUtils.isEmpty(mediaPlayerUtil.getMediaPath()))
                                    mediaPlayerUtil.playRandomMusic();
                            } else {
                                //pause
                                mediaPlayerUtil.pauseMedia();
                            }
                            break;
                    }
                }
            };
        }
        registerReceiver(mLoginStateReceiver, intentFilter);
    }

    public void unRegisterReceiver() {
        if (null != mLoginStateReceiver) {
            unregisterReceiver(mLoginStateReceiver);
        }
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
    }

    @Override
    public void onResults(Bundle results) {
        status = STATUS_None;
        ArrayList<String> nbest = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String json_res = results.getString("origin_result");
        if (nbest == null || nbest.size() == 0)
            return;
        for (String content : nbest) {
            if (TextUtils.isEmpty(content))
                continue;
            if (content.contains("音乐播放") || content.contains("播放音乐") || content.contains("播放")) {
                mediaPlayerUtil.playRandomMusic();
                break;
            } else if (content.contains("暂停音乐")
                    || content.contains("音乐暂停")
                    || content.contains("音乐关闭")
                    || content.contains("关闭音乐")
                    || content.contains("暂停")
                    || content.contains("关闭")) {
                mediaPlayerUtil.pauseMedia();
                break;
            }
        }
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
