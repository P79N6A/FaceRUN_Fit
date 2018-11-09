package com.fly.run.view.circle;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fly.run.R;
import com.fly.run.activity.circle.reply.CircleReplyActivity;
import com.fly.run.bean.CircleBean;
import com.fly.run.bean.CircleReply;
import com.fly.run.bean.ResultTaskBean;
import com.fly.run.httptask.HttpTaskUtil;
import com.fly.run.manager.UserInfoManager;
import com.fly.run.utils.AnimUtil;
import com.fly.run.utils.BroadcastUtil;
import com.fly.run.utils.NetWorkUtil;
import com.fly.run.utils.OkHttpClientManager;
import com.fly.run.utils.ToastUtil;
import com.fly.run.view.toast.CustomToast;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xinzhendi-031 on 2018/7/9.
 */

public class CircleBottomActionbarView extends LinearLayout implements View.OnClickListener {

    private LinearLayout layout_share, layout_review, layout_zan;
    private TextView tv_share, tv_review, tv_zan;
    private ImageView iv_zan;

    private boolean isZan = false;
    private int circleId;
    private CircleBean mCircleBean;
    private HttpTaskUtil httpTaskUtil;

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
        if (httpTaskUtil == null)
            httpTaskUtil = new HttpTaskUtil();
    }

    public void loadData(CircleBean circleBean){
        if (circleBean != null){
            mCircleBean = circleBean;
            this.circleId = circleBean.getId();
            tv_share.setText(""+circleBean.getShareCount());
            tv_zan.setText(""+circleBean.getLikeCount());
            tv_review.setText(""+circleBean.getReplyCount());
            if (circleBean.isHasThisUser()){
                iv_zan.setImageResource(R.drawable.icon_action_zan_press);
            } else {
                iv_zan.setImageResource(R.drawable.icon_action_zan_normal);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_share:
                if (!NetWorkUtil.haveNetWork(getContext())){
                    CustomToast.showRefreshToast(getContext(),false);
                    return;
                }
                Map<String,String> paramShare = new HashMap<>();
                paramShare.put("shareCircleId","" + circleId);
                paramShare.put("shareUserId", "" + UserInfoManager.getInstance().getAccountId());
                httpTaskUtil.InsertCircleShareTask(paramShare, new OkHttpClientManager.StringCallback() {
                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            ResultTaskBean bean = JSON.parseObject(response, ResultTaskBean.class);
                            if (bean != null && bean.code == 1) {
//                                BroadcastUtil.sendBroadcast(getContext(),BroadcastUtil.CIRCLE_SHARE_UPDATE);
                                int count = mCircleBean.getShareCount()+1;
                                tv_share.setText(""+count);
                            } else {
                                CustomToast.showRefreshToast(getContext(),false);
                            }
                        } catch (Exception e) {
                            ToastUtil.show((e != null && !TextUtils.isEmpty(e.getMessage()) ? e.getMessage() : "网络请求失败"));
                        } finally {
                        }
                    }
                });
                break;
            case R.id.layout_review:
                if (circleId <= 0)
                    return;
                CircleReplyActivity.startActivity(getContext(),circleId);
                break;
            case R.id.layout_zan:
//                if (isZan)
//                    iv_zan.setImageResource(R.drawable.icon_action_zan_normal);
//                else {
//                    iv_zan.setImageResource(R.drawable.icon_action_zan_press);
//                }
//                isZan = !isZan;
                if (!NetWorkUtil.haveNetWork(getContext())){
                    CustomToast.showRefreshToast(getContext(),false);
                    return;
                }
                Map<String,String> param = new HashMap<>();
                param.put("likeCircleId","" + circleId);
                param.put("likeUserId", "" + UserInfoManager.getInstance().getAccountId());
                httpTaskUtil.InsertCircleLikeTask(param, new OkHttpClientManager.StringCallback() {
                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            ResultTaskBean bean = JSON.parseObject(response, ResultTaskBean.class);
                            if (bean != null && bean.code == 1) {
                                iv_zan.setImageResource(R.drawable.icon_action_zan_press);
                                AnimUtil.scaleZanAnim(iv_zan, 1.4f, 1.0f, 0, 400);
                                int count = mCircleBean.getLikeCount()+1;
                                tv_zan.setText(""+count);
//                                BroadcastUtil.sendBroadcast(getContext(),BroadcastUtil.CIRCLE_LIKE_UPDATE);
                            } else {
                                CustomToast.showRefreshToast(getContext(),false);
                            }
                        } catch (Exception e) {
                            ToastUtil.show((e != null && !TextUtils.isEmpty(e.getMessage()) ? e.getMessage() : "网络请求失败"));
                        } finally {
                        }
                    }
                });
                break;
        }
    }
}
