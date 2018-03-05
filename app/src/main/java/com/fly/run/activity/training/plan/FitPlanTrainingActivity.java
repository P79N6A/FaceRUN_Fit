package com.fly.run.activity.training.plan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.bean.FitPlanBean;
import com.fly.run.config.AppConstants;
import com.fly.run.utils.Logger;
import com.fly.run.utils.SharePreferceTool;

import java.util.Timer;
import java.util.TimerTask;

public class FitPlanTrainingActivity extends BaseUIActivity implements SpeechSynthesizerListener {
    private TextView tv_target, tv_training;
    private FitPlanBean fitPlanBean;

    private Timer timer;
    private TimerTask timerTask;
    private boolean isStart = false;
    private long useTime = 0;
    private int mCount = 0;

    public static void startActivityJump(Context context, FitPlanBean bean) {
        Intent intent = new Intent(context, FitPlanTrainingActivity.class);
        intent.putExtra("FitPlanBean", bean);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fit_plan_training);
        fitPlanBean = (FitPlanBean) getIntent().getSerializableExtra("FitPlanBean");
        initView();
        initialTts();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uninitTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        tv_target = (TextView) findViewById(R.id.tv_target);
        tv_training = (TextView) findViewById(R.id.tv_training);
        tv_training.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCount >= fitPlanBean.getCounts()){
                    finish();
                    return;
                }
                initTimer();
            }
        });
        if (fitPlanBean != null) {
            int type = fitPlanBean.getCountstype();
            if (type == 1) { //计时
                tv_target.setText(fitPlanBean.getTitle()+"：" + fitPlanBean.getCounts() + "秒");
            } else { //计数
                tv_target.setText(fitPlanBean.getTitle()+"：" + fitPlanBean.getCounts() + "次");
            }
            if (fitPlanBean.getCounts() == 0)
                tv_target.setText("不设目标");
            tv_training.setVisibility(View.VISIBLE);
        } else {
            tv_training.setVisibility(View.INVISIBLE);
        }
    }

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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (mCount >= fitPlanBean.getCounts()){
                        tv_training.setText("完成");
                        return;
                    }
                    mCount++;
                    tv_training.setText("" + mCount);
                    String message = tv_training.getText().toString();
                    int result = mSpeechSynthesizer.speak(message);
                    if (result < 0) {
                        Logger.e(TAG, "error,"+message);
                    }
                    break;
                case 2:
                    break;
            }
        }
    };

    private void initTimer() {
        if (timer != null) {
            return;
        }
        int period = 4000;
        if (fitPlanBean != null) {
            int type = fitPlanBean.getCountstype();
            if (type == 1) { //计时
                period = 1000;
            } else { //计数
                period = 3000;
            }
        } else
            return;
        tv_training.setText("准备");
        int result = mSpeechSynthesizer.speak("准备");
        if (result < 0) {
            Logger.e(TAG, "error,准备");
        }
        if (timer == null) {
            timer = new Timer();
        }
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(1);
                }
            };
        }
        try {
            timer.schedule(timerTask, 3000, period);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uninitTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

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

    }

    @Override
    public void onSpeechProgressChanged(String s, int i) {

    }

    @Override
    public void onSpeechFinish(String s) {

    }

    @Override
    public void onError(String s, SpeechError speechError) {

    }
}
