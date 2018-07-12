package com.fly.run.view.circle;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fly.run.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinzhendi-031 on 2018/7/9.
 */

public class CircleBottomReviewView extends LinearLayout implements View.OnClickListener {

    private Context context;
    private LinearLayout layout_root;
    private String review1 = "平行线：你每天都要跑步吗？";
    private String review2 = "Mike：Yes,I like run,you want to join me.";
    private String review3 = "平行线：我可以和你一起跑吗，那真是太兴奋了！";
    private String review4 = "Jorden：I also want to join you, can I?";
    private String review5 = "Mike：Welcome!";
    private List<String> reviewList = new ArrayList<>();

    public CircleBottomReviewView(Context context) {
        super(context);
        initView(context, null);
    }

    public CircleBottomReviewView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public CircleBottomReviewView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, @Nullable AttributeSet attrs) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_circle_bottom_review, this);
        layout_root = (LinearLayout) findViewById(R.id.layout_root);
        initData();
    }

    private void initData() {
        reviewList.add(review1);
        reviewList.add(review2);
        reviewList.add(review3);
        reviewList.add(review4);
        reviewList.add(review5);
        for (int i = 0; i < reviewList.size(); i++) {
            TextView item = initItemView(i, reviewList.get(i));
            layout_root.addView(item);
        }
        postInvalidate();
    }

    private TextView initItemView(int i, String review) {
        TextView item = (TextView) LayoutInflater.from(context).inflate(R.layout.layout_circle_bottom_review_item, null);
        item.setText(review);
        item.setTag(i);
        return item;
    }

    private TextView initItemView(String review) {
        TextView item = new TextView(context);
        item.setMaxLines(2);
        item.setEllipsize(TextUtils.TruncateAt.END);
        item.setGravity(Gravity.CENTER_VERTICAL);
        item.setText(review);
        return item;
    }

    @Override
    public void onClick(View view) {

    }
}
