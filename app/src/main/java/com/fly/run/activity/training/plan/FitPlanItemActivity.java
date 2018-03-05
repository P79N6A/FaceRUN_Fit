package com.fly.run.activity.training.plan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.bean.FitPlanBean;
import com.fly.run.config.UrlConstants;
import com.fly.run.utils.IOTools;
import com.fly.run.utils.SDCardUtil;
import com.fly.run.view.gif.GifView;

import java.io.File;
import java.io.IOException;

public class FitPlanItemActivity extends BaseUIActivity {

    private GifView gifView;
    private TextView tvTitle, tvDesc,tv_train_group,tv_train_times,tvDoIt,tv_train;
    private FitPlanBean fitPlanBean;

    public static void startActivityJump(Context context, FitPlanBean bean) {
        Intent intent = new Intent(context, FitPlanItemActivity.class);
        intent.putExtra("FitPlanBean", bean);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fit_plan_item);
        fitPlanBean = (FitPlanBean) getIntent().getSerializableExtra("FitPlanBean");
        initView();
    }

    private void initView() {
        gifView = (GifView) findViewById(R.id.gifView);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
        tv_train_group = (TextView) findViewById(R.id.tv_train_group);
        tv_train_times = (TextView) findViewById(R.id.tv_train_times);
        tvDoIt = (TextView) findViewById(R.id.tv_do_it);
        tv_train = (TextView) findViewById(R.id.tv_train);
        tv_train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FitPlanTargetActivity.startActivityJump(FitPlanItemActivity.this,fitPlanBean);
            }
        });
        setData();
    }

    public void setData() {
        if (fitPlanBean == null)
            return;
        tvTitle.setText(fitPlanBean.getTitle() + "  -  无器械");
        tvDesc.setText("健身是一种体育项目，如各种徒手健美操、韵律操、形体操以及各种自抗力动作，体操可以增强力量、柔韧性，增加耐力，提高协调，控制身体各部分的能力，从而使身体强健。如果要达到缓解压力的目的，至少一周锻炼3次。\n" +
                "游泳、快走、慢跑、骑自行车，及一切有氧运动都能锻炼心脏。有氧运动好处多：能锻炼心肺、增强循环系统功能、燃烧脂肪、加大肺活量、降低血压，甚至能预防糖尿病，减少心脏病的发生。美国运动医学院建议，想知道有氧运动强度是否合适，可在运动后测试心率，以达到最高心率的60%—90%为宜。如果想通过有氧运动来减肥，可以选择低度到中度的运动强度，同时延长运动时间，这种方法消耗的热量更多。运动频率每周3—5次，每次20—60分钟。想要锻炼肌肉，可以练举重、做体操以及其他重复伸、屈肌肉的运动。肌肉锻炼可以燃烧热量、增强骨密度、减少受伤，尤其是关节受伤的几率，还能预防骨质疏松。 在做举重运动前，先测一下，如果连续举8次你最多能举多重的东西，就从这个重量开始练习。当你可以连续12次举起这个重量时，试试增加5%的重量。注意每次练习时，要连续举8—12次，这样可以达到肌肉最大耐力的70%—80%，锻炼效果较好。每周2—3次，但要避免连续两天锻炼同一组肌肉群， 以便让肌肉有充分的恢复时间。");
        tvDoIt.setText("今日已完成该训练：1562位。");
        tv_train_group.setText("训练组数："+fitPlanBean.getTimes()+"组");
        tv_train_times.setText("训练次数："+fitPlanBean.getCounts());
        String gifUrl = fitPlanBean.getImage();
        if (!TextUtils.isEmpty(gifUrl)) {
            final String filePath = new String(SDCardUtil.getGifDir() + "/" + gifUrl);
            File file = new File(filePath);
            if (file.exists() && file.length() > 0) {
                Message message = Message.obtain();
                message.what = 1;
                message.obj = filePath;
                handler.sendMessage(message);
                return;
            }
            String suffixName = gifUrl.substring(gifUrl.lastIndexOf("."));
            if (suffixName.equalsIgnoreCase(".gif")) {
                gifUrl = gifUrl.trim();
                gifUrl = "fit/" + gifUrl;
                if (!gifUrl.startsWith("http://"))
                    gifUrl = String.format(UrlConstants.HTTP_DOWNLOAD_FILE_2, gifUrl);
                final String finalGifUrl = gifUrl;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            handler.sendEmptyMessage(11);
                            boolean save = IOTools.saveFile(finalGifUrl, filePath);
                            if (save) {
                                Message message = Message.obtain();
                                message.what = 1;
                                message.obj = filePath;
                                handler.sendMessage(message);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            handler.sendEmptyMessage(12);
                        }
                    }
                }).start();
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String filePath = (String) msg.obj;
                    gifView.setMovieFile(filePath);
                    break;
            }
        }
    };
}
