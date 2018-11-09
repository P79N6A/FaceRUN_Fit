package com.fly.run.view.circle;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.fly.run.R;
import com.fly.run.activity.circle.reply.CircleReplyActivity;
import com.fly.run.bean.CircleReply;
import com.fly.run.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinzhendi-031 on 2018/7/9.
 */

public class CircleBottomReviewView extends LinearLayout implements View.OnClickListener {

    private Context context;
    private LinearLayout layout_root;
    private final String ForegroundColor = "#000000";

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
    }

    private TextView initItemView(final CircleReply reply) {
        TextView item = (TextView) LayoutInflater.from(context).inflate(R.layout.layout_circle_bottom_review_item, null);
        String content = reply.getReplyUserName()+": "+reply.getReplyContent();
        SpannableString spannableString = setContentForegroundColor(content,reply.getReplyUserName()+": ");
        item.setText(spannableString);
        item.setTag(reply.getId());
        item.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                CircleReplyActivity.startActivity(getContext(),reply.getReplyCircleId());
            }
        });
        return item;
    }

    private SpannableString setContentForegroundColor(String content,String textColor){
        SpannableString spannableString = new SpannableString(content);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor(ForegroundColor)), 0,textColor.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public void loadReviewData(String reply){
        if (!TextUtils.isEmpty(reply)){
            layout_root.removeAllViews();
            List<CircleReply> list = JSONObject.parseArray(reply,CircleReply.class);
            for (int i=0;i<list.size();i++){
                CircleReply circleReply = list.get(i);
                TextView item = initItemView(circleReply);
                layout_root.addView(item);
            }
            postInvalidate();
        }
    }

    @Override
    public void onClick(View view) {

    }
}
