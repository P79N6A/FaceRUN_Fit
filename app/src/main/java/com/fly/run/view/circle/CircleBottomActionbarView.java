package com.fly.run.view.circle;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fly.run.R;
import com.fly.run.utils.AnimUtil;

/**
 * Created by xinzhendi-031 on 2018/7/9.
 */

public class CircleBottomActionbarView extends LinearLayout implements View.OnClickListener {

    private LinearLayout layout_share, layout_review, layout_zan;
    private TextView tv_share, tv_review, tv_zan;
    private ImageView iv_zan;

    private boolean isZan = false;

    public CircleBottomActionbarView(Context context) {
        super(context);
        initView(context, null);
    }

    public CircleBottomActionbarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public CircleBottomActionbarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, @Nullable AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_circle_bottom_actionbar, this);
        layout_share = (LinearLayout) findViewById(R.id.layout_share);
        layout_review = (LinearLayout) findViewById(R.id.layout_review);
        layout_zan = (LinearLayout) findViewById(R.id.layout_zan);
        tv_share = (TextView) findViewById(R.id.tv_share);
        tv_review = (TextView) findViewById(R.id.tv_review);
        tv_zan = (TextView) findViewById(R.id.tv_zan);
        iv_zan = (ImageView) findViewById(R.id.iv_zan);
        layout_share.setOnClickListener(this);
        layout_review.setOnClickListener(this);
        layout_zan.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_share:
                break;
            case R.id.layout_review:
                break;
            case R.id.layout_zan:
                if (isZan)
                    iv_zan.setImageResource(R.drawable.icon_action_zan_normal);
                else {
                    iv_zan.setImageResource(R.drawable.icon_action_zan_press);
                }
                isZan = !isZan;
                AnimUtil.scaleZanAnim(iv_zan, 1.4f, 1.0f, 0, 400);
                break;
        }
    }
}
