package com.fly.run.activity.training;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.adapter.BaseTitleViewPagerAdapter;
import com.fly.run.utils.DisplayUtil;
import com.fly.run.view.actionbar.CommonActionBar;
import com.fly.run.view.gif.GifView;

import java.util.ArrayList;
import java.util.List;

public class FitTime2Activity extends BaseUIActivity implements ViewPager.OnPageChangeListener {

    private CommonActionBar actionBar;
    private final String[] FitTrains = {"俯卧撑", "仰卧卷腹", "徒手深蹲"};
    private final String[] AllFitTrains = {"俯卧撑", "倒立撑", "引体向上", "仰卧起坐", "仰卧卷腹", "平板支撑", "徒手深蹲", "负重深蹲", "箭步蹲"};

    //    private View view1, view2, view3;
    private List<View> viewList = new ArrayList<>();// view数组
    private PagerTabStrip pagerTabStrip;
    private ViewPager viewPager; // 对应的viewPager
    private BaseTitleViewPagerAdapter adapter;
    private GifView gifView;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fit_time_2);
        initView();
    }

    private void initView() {
        initActionBar();
        gifView = (GifView) findViewById(R.id.gifView);
        int dp10 = DisplayUtil.dp2px(10);
        pagerTabStrip = (PagerTabStrip) findViewById(R.id.pagertab);
        pagerTabStrip.setTabIndicatorColorResource(R.color.action_title_color);
        pagerTabStrip.setBackgroundColor(getResources().getColor(R.color.color_ffffff));
        pagerTabStrip.setPadding(dp10, dp10, dp10, dp10);
        pagerTabStrip.setTextSpacing(dp10);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(this);
        for (String title : AllFitTrains) {
            TextView textView = new TextView(this);
            textView.setText(title);
            textView.setPadding(dp10, dp10, dp10, dp10);
            viewList.add(textView);
        }
        adapter = new BaseTitleViewPagerAdapter(viewList, AllFitTrains);
        viewPager.setAdapter(adapter);
//        LayoutInflater inflater = getLayoutInflater();
//        view1 = inflater.inflate(R.layout.layout1, null);
//        view2 = inflater.inflate(R.layout.layout2, null);
//        view3 = inflater.inflate(R.layout.layout3, null);

    }

    private void initActionBar() {
        actionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        actionBar.setActionTitle("囚徒健身");
        actionBar.setActionLeftIconListenr(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gifView.setGifResource(R.mipmap.push_up);
                    }
                },200);
                break;
            case 1:
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gifView.setGifResource(R.mipmap.cheng_daoli);
                    }
                },200);
                break;
            case 2:
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gifView.setGifResource(R.mipmap.pull_ups);
                    }
                },200);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
