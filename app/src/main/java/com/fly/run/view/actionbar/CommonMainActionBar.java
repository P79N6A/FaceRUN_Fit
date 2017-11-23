package com.fly.run.view.actionbar;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

public class CommonMainActionBar extends LinearLayout {

    private View viewStatus;
    private int statusHeight = 0;

    private ImageView ation_left_iv, action_right_iv;
    private TextView action_title, action_right_text;

    public CommonMainActionBar(Context context) {
        super(context);
        initView();
    }

    public CommonMainActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CommonMainActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.actionbar_common_main, this);
        viewStatus = (View) findViewById(R.id.view_top_status);
        ation_left_iv = (ImageView) findViewById(R.id.action_left_iv);
        action_right_iv = (ImageView) findViewById(R.id.action_right_iv);
        ation_left_iv.setColorFilter(getResources().getColor(R.color.black));
        action_right_iv.setColorFilter(getResources().getColor(R.color.black));
        action_title = (TextView) findViewById(R.id.action_title);
        action_right_text = (TextView) findViewById(R.id.action_right_text);
        setStatusHeight();
    }

    public void setActionTitle(String title) {
        if (action_title != null)
            action_title.setText(title);
    }

    public void setActionTitle(int src) {
        if (action_title != null)
            action_title.setText(src);
    }

    public void setActionLeftListenr(View.OnClickListener listener) {
        if (listener != null) {
            ation_left_iv.setOnClickListener(listener);
        }
    }

    public void setActionRightListenr(OnClickListener listener) {
        if (listener != null) {
            action_right_iv.setOnClickListener(listener);
        }
    }

    public void setActionRightText(String src) {
        if (TextUtils.isEmpty(src)) {
            action_right_text.setVisibility(View.GONE);
            return;
        }
        action_right_text.setText(src);
        action_right_text.setVisibility(View.VISIBLE);
    }

    public void setActionRightTextDrawableLeft(int src) {
        Drawable drawable = getResources().getDrawable(src);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        action_right_text.setCompoundDrawables(drawable, null, null, null);
    }

    public void setActionRightTextListenr(String src, OnClickListener listener) {
        if (TextUtils.isEmpty(src)) {
            action_right_text.setVisibility(View.GONE);
            return;
        }
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
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewStatus.getLayoutParams();
        params.height = statusHeight;
        viewStatus.setLayoutParams(params);
    }
}
