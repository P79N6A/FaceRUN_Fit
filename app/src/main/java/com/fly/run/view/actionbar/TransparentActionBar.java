package com.fly.run.view.actionbar;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fly.run.R;
import com.fly.run.utils.DisplayUtil;

import static com.fly.run.utils.ColorUtils.evaluateColor;

/**
 * Created by kongwei on 2017/2/20.
 */

public class TransparentActionBar extends RelativeLayout {

    private View viewStatus;
    private int statusHeight = 0;

    private ImageView action_iv_bg;
    private ImageView ation_left_iv, action_right_iv;
    private TextView action_title, action_right_text;
    public boolean isChangeTheme = false;
    private Handler mHandler = new Handler();

    public TransparentActionBar(Context context) {
        super(context);
        initView();
    }

    public TransparentActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TransparentActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.actionbar_transparent, this);
        action_iv_bg = (ImageView) findViewById(R.id.action_iv_bg);
        viewStatus = (View) findViewById(R.id.view_top_status);
        ation_left_iv = (ImageView) findViewById(R.id.action_left_iv);
        action_right_iv = (ImageView) findViewById(R.id.action_right_iv);
        action_title = (TextView) findViewById(R.id.action_title);
        action_right_text = (TextView) findViewById(R.id.action_right_text);
        setStatusHeight();
    }

    public class MyColorThread extends Thread {

        int startColor;
        int endColor;
        float progress1;
        ImageView layout;

        public MyColorThread(float progress1, ImageView layout) {
            this.progress1 = progress1;
            this.layout = layout;
        }

        public void setColors(int startColor, int endColor) {
            this.startColor = startColor;
            this.endColor = endColor;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                progress1 = i / 100f;
                try {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            layout.setBackgroundColor(evaluateColor(startColor, endColor, progress1));
                        }
                    });
                    Thread.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setBackgroundSrc(int fromColor, int toColor) {
        isChangeTheme = true;
        MyColorThread thread = new MyColorThread(0, action_iv_bg);
        thread.setColors(fromColor, toColor);
        thread.start();
    }

    public void setBackgroundTransparent(int fromColor, int toColor) {
        isChangeTheme = false;
        MyColorThread thread = new MyColorThread(0, action_iv_bg);
        thread.setColors(fromColor, toColor);
        thread.start();
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
        LayoutParams params2 = (LayoutParams) action_iv_bg.getLayoutParams();
        params2.height = DisplayUtil.dp2px(45) + statusHeight;
        action_iv_bg.setLayoutParams(params2);
    }
}
