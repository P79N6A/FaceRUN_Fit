package com.fly.run.activity.circle;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.bean.CircleBean;
import com.fly.run.bean.ResultTaskBean;
import com.fly.run.httptask.HttpTaskUtil;
import com.fly.run.manager.UserInfoManager;
import com.fly.run.utils.ToastUtil;
import com.fly.run.view.actionbar.CommonActionBar;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.List;

public class CircleActivity extends BaseUIActivity implements View.OnClickListener {

    private CommonActionBar actionBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private HttpTaskUtil httpTaskUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);
        initActionBar();
        initView();
        loadTaskData();
    }

    private void initActionBar() {
        actionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        actionBar.setActionTitle("跑圈圈");
        actionBar.setActionLeftIconListenr(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        // 设置颜色属性的时候一定要注意是引用了资源文件还是直接设置16进制的颜色，因为都是int值容易搞混
        // 设置下拉进度的背景颜色，默认就是白色的
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_purple, R.color.colorAccent, android.R.color.holo_blue_light, android.R.color.holo_green_light);

        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // 开始刷新，设置当前为刷新状态
                //swipeRefreshLayout.setRefreshing(true);
                // 这里是主线程
                // 一些比较耗时的操作，比如联网获取数据，需要放到子线程去执行
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 加载完数据设置为不刷新状态，将下拉进度收起来
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);

                // System.out.println(Thread.currentThread().getName());

                // 这个不能写在外边，不然会直接收起来
                //swipeRefreshLayout.setRefreshing(false);
            }
        });
        listView = (ListView) findViewById(R.id.listview);
    }

    private void loadTaskData() {
        if (httpTaskUtil == null) {
            httpTaskUtil = new HttpTaskUtil();
            httpTaskUtil.setResultListener(resultListener);
        }
        httpTaskUtil.QueryCircleRunTask(UserInfoManager.getInstance().getAccount());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    HttpTaskUtil.ResultListener resultListener = new HttpTaskUtil.ResultListener() {
        @Override
        public void onResponse(String response) {
            try {
                ResultTaskBean bean = JSON.parseObject(response, ResultTaskBean.class);
                if (bean != null && bean.code == 1) {
                    if (!TextUtils.isEmpty(bean.data)) {
                        List<CircleBean> list = JSON.parseArray(bean.data, CircleBean.class);
                    }
                }
            } catch (Exception e) {
                ToastUtil.show((e != null && !TextUtils.isEmpty(e.getMessage()) ? e.getMessage() : "网络请求失败"));
            }
        }

        @Override
        public void onFailure(Request request, IOException e) {
            ToastUtil.show((e != null && !TextUtils.isEmpty(e.getMessage()) ? e.getMessage() : "网络请求失败"));
        }
    };
}
