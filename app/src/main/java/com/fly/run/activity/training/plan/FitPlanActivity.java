package com.fly.run.activity.training.plan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.bean.FitPlanItem;
import com.fly.run.bean.ResultTaskBean;
import com.fly.run.fragment.FitPlanFragment;
import com.fly.run.httptask.HttpTaskUtil;
import com.fly.run.utils.ToastUtil;
import com.fly.run.view.PagerSlidingTabStrip;
import com.fly.run.view.actionbar.CommonActionBar;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

public class FitPlanActivity extends BaseUIActivity {

    private CommonActionBar actionBar;
    private PagerSlidingTabStrip tab;
    private ViewPager pager;
    private String[] plans = {"上肢计划", "胸背强化", "腿部训练", "腰腹特训"};
    private List<String> nameList = new ArrayList<>();
    private HttpTaskUtil httpTaskUtil;
    private List<FitPlanItem> fitPlanList;
    private FragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fit_plan);
        initActionBar();
        initView();
        loadTaskData();
    }

    private void initActionBar() {
        actionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        actionBar.setActionTitle("无器械健身");
        actionBar.setActionLeftIconListenr(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        pager = (ViewPager) findViewById(R.id.pager);
        tab = (PagerSlidingTabStrip) findViewById(R.id.indicator);
    }

    class TabPageIndicatorAdapter extends FragmentPagerAdapter {
        public TabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //新建一个Fragment来展示ViewPager item的内容，并传递参数
            return FitPlanFragment.newInstance(position, fitPlanList != null ? fitPlanList.get(position).getPlanList() : null);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return nameList.get(position);
        }

        @Override
        public int getCount() {
            return nameList.size();
        }
    }

    private void loadTaskData() {
        if (httpTaskUtil == null) {
            httpTaskUtil = new HttpTaskUtil();
            httpTaskUtil.setResultListener(resultListener);
        }
        httpTaskUtil.QuerySysFitPlanTask();
    }

    HttpTaskUtil.ResultListener resultListener = new HttpTaskUtil.ResultListener() {
        @Override
        public void onResponse(String response) {
            try {
                ResultTaskBean bean = JSON.parseObject(response, ResultTaskBean.class);
                if (bean != null && bean.code == 1) {
                    if (!TextUtils.isEmpty(bean.data)) {
                        fitPlanList = JSON.parseArray(bean.data, FitPlanItem.class);
                        if (fitPlanList == null)
                            return;
                        for (FitPlanItem item : fitPlanList) {
                            nameList.add(item.getPlanName());
                        }
                        if (adapter == null) {
                            //ViewPager的adapter
                            adapter = new TabPageIndicatorAdapter(getSupportFragmentManager());
                            pager.setAdapter(adapter);
                            tab.setViewPager(pager);
                            pager.setOffscreenPageLimit(fitPlanList.size());
                        }
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
