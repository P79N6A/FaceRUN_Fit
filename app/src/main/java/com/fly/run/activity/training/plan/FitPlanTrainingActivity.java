package com.fly.run.activity.training.plan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.bean.FitPlanBean;

import java.util.Timer;
import java.util.TimerTask;

public class FitPlanTrainingActivity extends BaseUIActivity {
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
                initTimer();
            }
        });
        if (fitPlanBean != null) {
            int type = fitPlanBean.getCountstype();
            if (type == 1) { //计时
                tv_target.setText("目标：" + fitPlanBean.getCounts() + "秒");
            } else { //计数
                tv_target.setText("目标：" + fitPlanBean.getCounts() + "次");
            }
            if (fitPlanBean.getCounts() == 0)
                tv_target.setText("不设目标");
            tv_training.setVisibility(View.VISIBLE);
        } else {
            tv_training.setVisibility(View.INVISIBLE);
        }
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
            timer.schedule(timerTask, 4000, 4000);
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
}
