package com.fly.run.activity.circle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.fragment.circle.CircleAttentionFragment;
import com.fly.run.fragment.circle.CircleRunFragment;
import com.fly.run.fragment.circle.CircleSearch2Fragment;
import com.fly.run.fragment.circle.CircleSearchFragment;
import com.fly.run.utils.ToastUtil;
import com.fly.run.view.viewpager.CustomViewPager;

public class CircleActivity extends BaseUIActivity implements View.OnClickListener {

    private CustomViewPager viewPager;
    private CircleRunFragment circleRunFragment;
    private CircleSearchFragment circleSearchFragment;
    private CircleSearch2Fragment circleSearch2Fragment;
    private CircleAttentionFragment circleAttentionFragment;

    private TextView tv_run, tv_dongtai, tv_guanzhu;
    private View view_line_dongtai, view_line_run, view_line_guanzhu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);
        initView();
    }


    private void initView() {
        viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        viewPager.setScanScroll(false);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(pageChangeListener);
        tv_run = (TextView) findViewById(R.id.tv_run);
        tv_dongtai = (TextView) findViewById(R.id.tv_dongtai);
        tv_guanzhu = (TextView) findViewById(R.id.tv_guanzhu);
        view_line_dongtai = (View) findViewById(R.id.view_line_dongtai);
        view_line_run = (View) findViewById(R.id.view_line_run);
        view_line_guanzhu = (View) findViewById(R.id.view_line_guanzhu);
        tv_run.setOnClickListener(this);
        tv_dongtai.setOnClickListener(this);
        tv_guanzhu.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        setLineInvisiable();
        switch (view.getId()) {
            case R.id.tv_dongtai:
                viewPager.setCurrentItem(0, false);
                view_line_dongtai.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_run:
                viewPager.setCurrentItem(1, false);
                view_line_run.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_guanzhu:
                viewPager.setCurrentItem(2, false);
                view_line_guanzhu.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setLineInvisiable() {
        view_line_dongtai.setVisibility(View.INVISIBLE);
        view_line_run.setVisibility(View.INVISIBLE);
        view_line_guanzhu.setVisibility(View.INVISIBLE);
    }

    /**
     * 初始化FragmentPagerAdapter
     */
    private FragmentPagerAdapter adapter = new FragmentPagerAdapter(
            getSupportFragmentManager()) {

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (circleRunFragment == null)
                        circleRunFragment = new CircleRunFragment();
                    return circleRunFragment;
                case 1:
                    if (circleSearchFragment == null)
                        circleSearchFragment = new CircleSearchFragment();
                    return circleSearchFragment;
                case 2:
                    if (circleSearch2Fragment == null)
                        circleSearch2Fragment = new CircleSearch2Fragment();
                    return circleSearch2Fragment;
                default:
                    if (circleRunFragment == null)
                        circleRunFragment = new CircleRunFragment();
                    return circleRunFragment;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
