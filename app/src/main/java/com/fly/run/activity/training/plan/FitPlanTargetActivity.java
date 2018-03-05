package com.fly.run.activity.training.plan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.bean.FitPlanBean;
import com.fly.run.utils.TimeFormatUtils;

public class FitPlanTargetActivity extends BaseUIActivity {
    private TextView tv_title, tv_jishu,tv_train;
    private SeekBar seekBar;
    private FitPlanBean fitPlanBean;

    public static void startActivityJump(Context context, FitPlanBean bean) {
        Intent intent = new Intent(context, FitPlanTargetActivity.class);
        intent.putExtra("FitPlanBean", bean);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fit_plan_target);
        fitPlanBean = (FitPlanBean) getIntent().getSerializableExtra("FitPlanBean");
        initView();
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_jishu = (TextView) findViewById(R.id.tv_jishu);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        tv_train = (TextView) findViewById(R.id.tv_train);
        tv_train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fitPlanBean.setCounts(seekBar.getProgress());
                FitPlanTrainingActivity.startActivityJump(FitPlanTargetActivity.this,fitPlanBean);
            }
        });
        if (fitPlanBean != null) {
            tv_jishu.setText(fitPlanBean.getTitle());
            int type = fitPlanBean.getCountstype();
            if (type == 1) { //计时
                seekBar.setMax(10 * 60);
//                tv_jishu.setText("计时");
                tv_title.setText("坚持" + " " + TimeFormatUtils.formatDurationHHMMss(fitPlanBean.getCounts() * 1000));
            } else { //计数
                seekBar.setMax(100);
//                tv_jishu.setText("计数");
                tv_title.setText("坚持" + fitPlanBean.getCounts() + "个");
            }
            seekBar.setProgress(fitPlanBean.getCounts());
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i == 0) {
                    tv_title.setText("不设目标");
                } else {
                    int type = fitPlanBean.getCountstype();
                    if (type == 1) { //计时
                        tv_title.setText("坚持" + " " + TimeFormatUtils.formatDurationHHMMss(i * 1000));
                    } else { //计数
                        tv_title.setText("坚持" + i + "个");
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void setData() {
    }
}
