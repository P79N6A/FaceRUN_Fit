package com.fly.run.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fly.run.R;

/**
 * Created by xinzhendi-031 on 2016/10/26.
 */
public class TrainPlanView extends RelativeLayout {

    private TextView tv_train_plan_level;
    private LinearLayout layout_train_bg;
    private RelativeLayout layout_train_1, layout_train_2, layout_train_3;
    private TextView tv_train_1, tv_train_2, tv_train_3;
    private TextView tv_train_1_times, tv_train_2_times, tv_train_3_times;
    private ImageView iv_train_lock_1, iv_train_lock_2, iv_train_lock_3;
    private String[] titles;
    private int level = 0;

    public TrainPlanView(Context context) {
        super(context);
        init();
    }

    public TrainPlanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.view_train_plan, this);
        tv_train_plan_level = (TextView) view.findViewById(R.id.tv_train_plan_level);
        layout_train_bg = (LinearLayout) view.findViewById(R.id.layout_train_bg);
        layout_train_1 = (RelativeLayout) view.findViewById(R.id.layout_train_1);
        layout_train_2 = (RelativeLayout) view.findViewById(R.id.layout_train_2);
        layout_train_3 = (RelativeLayout) view.findViewById(R.id.layout_train_3);
        tv_train_1 = (TextView) view.findViewById(R.id.tv_train_1);
        tv_train_1_times = (TextView) view.findViewById(R.id.tv_train_1_times);
        tv_train_2 = (TextView) view.findViewById(R.id.tv_train_2);
        tv_train_2_times = (TextView) view.findViewById(R.id.tv_train_2_times);
        tv_train_3 = (TextView) view.findViewById(R.id.tv_train_3);
        tv_train_3_times = (TextView) view.findViewById(R.id.tv_train_3_times);
        iv_train_lock_1 = (ImageView) view.findViewById(R.id.iv_train_lock_1);
        iv_train_lock_2 = (ImageView) view.findViewById(R.id.iv_train_lock_2);
        iv_train_lock_3 = (ImageView) view.findViewById(R.id.iv_train_lock_3);
        iv_train_lock_1.setColorFilter(getContext().getResources().getColor(R.color.black));
        iv_train_lock_2.setColorFilter(getContext().getResources().getColor(R.color.black));
        iv_train_lock_3.setColorFilter(getContext().getResources().getColor(R.color.black));
        layout_train_1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null)
                    clickListener.runLow();
            }
        });
        layout_train_2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null)
                    clickListener.runMid();
            }
        });
        layout_train_3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null)
                    clickListener.runHigh();
            }
        });
    }

    public void initData(String[] data) {
        if (data == null || data.length == 0) {
            this.setVisibility(View.GONE);
            return;
        }
        titles = data;
        setTrainPlanLevel(titles[0]);
        setTrain1(titles[1]);
        setTrain2(titles[2]);
        setTrain3(titles[3]);
        setTrain1Times(0);
        setTrain2Times(0);
        setTrain3Times(0);
    }

    public void setLevel(int level) {
        this.level = level;
        switch (level) {
            case 1:
                iv_train_lock_1.setVisibility(View.INVISIBLE);
                break;
            case 2:
                iv_train_lock_1.setVisibility(View.INVISIBLE);
                iv_train_lock_2.setVisibility(View.INVISIBLE);
                break;
            case 3:
                iv_train_lock_1.setVisibility(View.INVISIBLE);
                iv_train_lock_2.setVisibility(View.INVISIBLE);
                iv_train_lock_3.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    public void setTrainPlanLevel(String level) {
        tv_train_plan_level.setText(level);
    }

    public void setTrain1Times(int count) {
        if (count <= 0)
            tv_train_1_times.setText("");
        else
            tv_train_1_times.setText(String.valueOf(count));
    }

    public void setTrain2Times(int count) {
        if (count <= 0)
            tv_train_2_times.setText("");
        else
            tv_train_2_times.setText(String.valueOf(count));
    }

    public void setTrain3Times(int count) {
        if (count <= 0)
            tv_train_3_times.setText("");
        else
            tv_train_3_times.setText(String.valueOf(count));
    }

    public void setTrain1(String train) {
        tv_train_1.setText(train);
    }

    public void setTrain2(String train) {
        tv_train_2.setText(train);
    }

    public void setTrain3(String train) {
        tv_train_3.setText(train);
    }

    private ClickListener clickListener;

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        public void runLow();

        public void runMid();

        public void runHigh();
    }
}
