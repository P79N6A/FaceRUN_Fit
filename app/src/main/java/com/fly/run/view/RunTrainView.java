package com.fly.run.view;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fly.run.R;

/**
 * Created by xinzhendi-031 on 2016/10/26.
 */
public class RunTrainView extends RelativeLayout {

    private TextView tvDistance, tvSpeed, tvTime, tvHeat;
    private TextView tvSpeedMin;
    private Handler handler;

    public RunTrainView(Context context) {
        super(context);
        init();
    }

    public RunTrainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.view_train_run, this);
        tvDistance = (TextView) view.findViewById(R.id.tv_distance);
        tvSpeed = (TextView) view.findViewById(R.id.tv_speed);
        tvSpeedMin = (TextView) view.findViewById(R.id.tv_speed_min);
        tvTime = (TextView) view.findViewById(R.id.tv_time);
        tvHeat = (TextView) view.findViewById(R.id.tv_heat);
        initData();
    }

//    public RunTrainView setHandler(Handler handler) {
//        if (handler != null) {
//            this.handler = handler;
//            circleAnimView.setHandler(handler);
//        }
//        return this;
//    }


    private void initData() {
        setTvDistanceText("");
        setTvSpeedText("");
        setTvTimeText("");
        setTvHeatText("");
    }

    public void setTvDistanceText(String s) {
        if (TextUtils.isEmpty(s)) {
            tvDistance.setText("00.00");
        } else {
            tvDistance.setText(s);
        }
    }

    public void setTvSpeedText(String s) {
        if (TextUtils.isEmpty(s)) {
            tvSpeed.setText("--");
//            tvSpeedMin.setVisibility(View.GONE);
        } else {
            tvSpeed.setText(s);
//            tvSpeedMin.setVisibility(View.VISIBLE);
        }
    }

    public void setTvTimeText(String s) {
        if (TextUtils.isEmpty(s)) {
            tvTime.setText("00:00:00");
        } else {
            tvTime.setText(s);
        }
    }

    public void setTvHeatText(String s) {
        if (TextUtils.isEmpty(s)) {
            tvHeat.setText("--");
        } else {
            tvHeat.setText(s);
        }
    }

    private ClickListener clickListener;

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        public void run();
    }
}
