package com.fly.run.activity.circle;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.adapter.PublishCircleGridAdapter;
import com.fly.run.bean.AccountBean;
import com.fly.run.bean.CircleBean;
import com.fly.run.bean.ResultTaskBean;
import com.fly.run.httptask.HttpTaskUtil;
import com.fly.run.manager.UserInfoManager;
import com.fly.run.utils.ToastUtil;
import com.fly.run.view.actionbar.CommonActionBar;
import com.fly.run.view.gridview.CustomGridView;
import com.squareup.okhttp.Request;

import java.util.List;

public class CirclePublishActivity extends BaseUIActivity implements View.OnClickListener {

    private CommonActionBar actionBar;
    private EditText etInput;
    private TextView tvAddress;
    private CustomGridView gridView;
    private PublishCircleGridAdapter adapter;
    private HttpTaskUtil httpTaskUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_circle);
        initActionBar();
        initView();
    }

    private void initActionBar() {
        actionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        actionBar.setActionLeftIconListenr(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        actionBar.setActionRightIconListenr(R.drawable.ic_jobs_commen_press, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishTaskData();
            }
        });
    }

    private void initView() {
        etInput = (EditText) findViewById(R.id.et_input);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        gridView = (CustomGridView) findViewById(R.id.gridview);
        adapter = new PublishCircleGridAdapter(this);
        gridView.setAdapter(adapter);
        adapter.setImageListener(new PublishCircleGridAdapter.ImageListenrt() {
            @Override
            public void doShowImage(int position, String url) {

            }

            @Override
            public void doAddImage() {

            }
        });
        showGridImgsView(null);
    }

    private void showGridImgsView(List<String> images) {
        adapter.setUrlImages(images);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    private void publishTaskData() {
        if (httpTaskUtil == null) {
            httpTaskUtil = new HttpTaskUtil();
            httpTaskUtil.setResultListener(resultListener);
        }
        AccountBean bean = UserInfoManager.getInstance().getAccountInfo();
        if (bean == null || bean.getId() <= 0) {
            ToastUtil.show("请登录");
            return;
        }
        String strInput = etInput.getText().toString();
        CircleBean circleBean = new CircleBean();
        circleBean.setAccount(bean.getAccount());
        circleBean.setAccountId(bean.getId());
        circleBean.setDescription(TextUtils.isEmpty(strInput) ? etInput.getHint().toString() : strInput);
        circleBean.setPhotos("http://pic.melinked.com/me2017/a19/6ep2168ni8ek.jpg");
        circleBean.setAddress(tvAddress.getText().toString());
        String strJson = JSONObject.toJSONString(circleBean);
        httpTaskUtil.InsertCircleRunTask(strJson, "" + bean.getId());
    }

    HttpTaskUtil.ResultListener resultListener = new HttpTaskUtil.ResultListener() {
        @Override
        public void onResponse(String response) {
            try {
                ResultTaskBean bean = JSON.parseObject(response, ResultTaskBean.class);
                if (bean != null && bean.code == 1) {
                    if (!TextUtils.isEmpty(bean.data)) {
                    }
                }
            } catch (Exception e) {
                ToastUtil.show((e != null && !TextUtils.isEmpty(e.getMessage()) ? e.getMessage() : "网络请求失败"));
            } finally {
                
            }
        }

        @Override
        public void onFailure(Request request, Exception e) {
            ToastUtil.show((e != null && !TextUtils.isEmpty(e.getMessage()) ? e.getMessage() : "网络请求失败"));
        }
    };
}
