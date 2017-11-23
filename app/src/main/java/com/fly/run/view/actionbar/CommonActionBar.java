package com.fly.run.view.actionbar;

import android.content.Context;
import android.text.TextUtils;
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

public class CommonActionBar extends LinearLayout {

    private View viewStatus;
    private int statusHeight = 0;

    private ImageView ation_left_iv, action_right_iv;
    private TextView action_title, action_right_text;

    public CommonActionBar(Context context) {
        super(context);
        initView();
    }

    public CommonActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CommonActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.actionbar_common, this);
        viewStatus = (View) findViewById(R.id.view_top_status);
        ation_left_iv = (ImageView) findViewById(R.id.action_left_iv);
        action_right_iv = (ImageView) findViewById(R.id.action_right_iv);
        action_title = (TextView) findViewById(R.id.action_title);
        action_right_text = (TextView) findViewById(R.id.action_right_text);
        setStatusHeight();
    }

    public void setBackgroundSrc(int src) {
        this.setBackgroundResource(src);
    }

    public void setActionTitle(String title) {
        if (action_title != null)
            action_title.setText(title);
    }

    public void setActionTitle(int src) {
        if (action_title != null)
            action_title.setText(src);
    }

    public void setActionLeftIconListenr(int src, OnClickListener listener) {
        if (src > 0)
            ation_left_iv.setImageResource(src);
        if (listener != null) {
            ation_left_iv.setOnClickListener(listener);
        }
    }

    public void setActionRightIconListenr(int src, OnClickListener listener) {
        if (src > 0)
            action_right_iv.setImageResource(src);
        if (listener != null) {
            action_right_iv.setOnClickListener(listener);
        }
        ation_left_iv.setVisibility(View.VISIBLE);
    }

    public void setActionRightTextListenr(int src, OnClickListener listener) {
        if (src > 0)
            action_right_text.setText(src);
        if (listener != null) {
            action_right_text.setOnClickListener(listener);
        }
        action_right_text.setVisibility(View.VISIBLE);
    }

    public void setActionRightTextListenr(String src, OnClickListener listener) {
        if (TextUtils.isEmpty(src))
            return;
        action_right_text.setText(src);
        if (listener != null) {
            action_right_text.setOnClickListener(listener);
        }
        action_right_text.setVisibility(View.VISIBLE);
    }

    public void setActionRightIconVisiable(int visiable) {
        action_right_iv.setVisibility(visiable);
    }

    public void setActionRightTextVisiable(int visiable) {
        action_right_text.setVisibility(visiable);
    }

    private void setStatusHeight() {
        if (statusHeight == 0)
            statusHeight = DisplayUtil.getStatusBarHeight(getContext());
        LayoutParams params = (LayoutParams) viewStatus.getLayoutParams();
        params.height = statusHeight;
        viewStatus.setLayoutParams(params);
    }
}
