package com.fly.run.view.actionbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fly.run.R;
import com.fly.run.utils.DisplayUtil;

/**
 * Created by kongwei on 2017/2/20.
 */

public class MainRunActionBar extends LinearLayout {

    private View viewStatus;
    private int statusHeight = 0;

    private ImageView ation_left_iv, action_right_iv;
    private TextView tvMap, tvData;

    public MainRunActionBar(Context context) {
        super(context);
        initView();
    }

    public MainRunActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MainRunActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.actionbar_main_run, this);
        viewStatus = (View) findViewById(R.id.view_top_status);
        ation_left_iv = (ImageView) findViewById(R.id.action_left_iv);
        action_right_iv = (ImageView) findViewById(R.id.action_right_iv);
        ation_left_iv.setColorFilter(getResources().getColor(R.color.black));
        action_right_iv.setColorFilter(getResources().getColor(R.color.black));
        tvMap = (TextView) findViewById(R.id.tv_map);
        tvData = (TextView) findViewById(R.id.tv_data);
        tvMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTopSectionView(0);
            }
        });
        tvData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTopSectionView(1);
            }
        });
        setStatusHeight();
    }


    public void setActionLeftListenr(OnClickListener listener) {
        if (listener != null) {
            ation_left_iv.setOnClickListener(listener);
        }

    }

    public void setActionRightListenr(OnClickListener listener) {
        if (listener != null) {
            action_right_iv.setOnClickListener(listener);
        }
    }

    public void changeTopSectionView(int position) {
        tvMap.setBackgroundResource(0);
        tvData.setBackgroundResource(0);
        if (position == 0) {
            tvMap.setBackgroundResource(R.drawable.bg_rectangle_white_left);
            tvMap.setTextColor(getResources().getColor(R.color.white));
            tvData.setTextColor(getResources().getColor(R.color.color_999999));
        } else {
            tvData.setBackgroundResource(R.drawable.bg_rectangle_white_right);
            tvData.setTextColor(getResources().getColor(R.color.white));
            tvMap.setTextColor(getResources().getColor(R.color.color_999999));
        }
        if (listener != null)
            listener.selectSection(position);
    }

    private void setStatusHeight() {
        if (statusHeight == 0)
            statusHeight = DisplayUtil.getStatusBarHeight(getContext());
        LayoutParams params = (LayoutParams) viewStatus.getLayoutParams();
        params.height = statusHeight;
        viewStatus.setLayoutParams(params);
    }

    public SectionListener listener;

    public MainRunActionBar setSectionListener(SectionListener listener) {
        this.listener = listener;
        return this;
    }

    public interface SectionListener{
        public void selectSection(int position);
    }
}
