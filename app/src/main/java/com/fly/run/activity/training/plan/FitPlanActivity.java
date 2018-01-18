package com.fly.run.activity.training.plan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.fragment.RunDataFragment;
import com.fly.run.view.PagerSlidingTabStrip;
import com.fly.run.view.actionbar.CommonActionBar;

public class FitPlanActivity extends BaseUIActivity {

    private CommonActionBar actionBar;
    private PagerSlidingTabStrip tab;
    private ViewPager pager;
    private String[] plans = {"上肢计划", "胸背强化", "腿部训练", "腰腹特训"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fit_plan);
        initActionBar();
        initView();
    }

    private void initActionBar() {
        actionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        actionBar.setActionTitle("囚徒计划");
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
        //ViewPager的adapter
        FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tab.setViewPager(pager);
    }

    class TabPageIndicatorAdapter extends FragmentPagerAdapter {
        public TabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //新建一个Fragment来展示ViewPager item的内容，并传递参数
            return RunDataFragment.newInstance("1", "2");
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return plans[position];
        }

        @Override
        public int getCount() {
            return plans.length;
        }
    }
}
