package com.fly.run.activity.person;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.adapter.PersonInfoAdapter;
import com.fly.run.bean.AccountBean;
import com.fly.run.fragment.HeaderViewPagerFragment;
import com.fly.run.fragment.ListViewFragment;
import com.fly.run.manager.UserInfoManager;
import com.fly.run.utils.BroadcastUtil;
import com.fly.run.utils.DisplayUtil;
import com.fly.run.view.HeaderView.MyHeaderViewPager;
import com.fly.run.view.HeaderView.PersonHeaderView;
import com.fly.run.view.actionbar.TransparentActionBar;

import java.util.ArrayList;
import java.util.List;

public class PersonInfoActivity extends BaseUIActivity {

    private TransparentActionBar actionBar;
    private ListView listView;
    private PersonInfoAdapter adapter;
    private PersonHeaderView personHeaderView;
    private MyHeaderViewPager scrollableLayout;
    private View titleBar_Bg;
    private TextView titleBar_title;
    private View status_bar_fix;
    private View titleBar;
    private ImageView ivBack, ivEdit;
    public List<HeaderViewPagerFragment> fragments;
    private ViewPager viewPager;
    private AccountBean accountBean;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        registerReceiver();
        accountBean = UserInfoManager.getInstance().getAccountInfo();
        personHeaderView = (PersonHeaderView) findViewById(R.id.peopleHeaderView);
        personHeaderView.setData(accountBean);
        titleBar = findViewById(R.id.titleBar);
        titleBar_Bg = titleBar.findViewById(R.id.bg);
        //当状态栏透明后，内容布局会上移，这里使用一个和状态栏高度相同的view来修正内容区域
        status_bar_fix = titleBar.findViewById(R.id.status_bar_fix);
        status_bar_fix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.getStatusBarHeight(this)));
        titleBar_title = (TextView) titleBar.findViewById(R.id.title);
        ivBack = (ImageView) titleBar.findViewById(R.id.back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ivEdit = (ImageView) titleBar.findViewById(R.id.edit);
        ivEdit.setColorFilter(getResources().getColor(R.color.white));
        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               intentToActivity(PersonInfoEditActivity.class);
            }
        });
        titleBar_Bg.setAlpha(0);
        status_bar_fix.setAlpha(0);
        titleBar_title.setAlpha(0);
        titleBar_title.setText("");
        if (accountBean != null) {
            titleBar_title.setText(accountBean.getName());
        }
        //内容的fragment
        fragments = new ArrayList<>();
        fragments.add(ListViewFragment.newInstance());
        scrollableLayout = (MyHeaderViewPager) findViewById(R.id.scrollableLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new ContentAdapter(getSupportFragmentManager()));
        scrollableLayout.setCurrentScrollableContainer(fragments.get(0));
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                scrollableLayout.setCurrentScrollableContainer(fragments.get(position));
            }
        });
        scrollableLayout.setCurrentScrollableContainer(fragments.get(0));
        scrollableLayout.setOnScrollListener(new MyHeaderViewPager.OnScrollListener() {
            @Override
            public void onScroll(int currentY, int maxY) {
                //让头部具有差速动画,如果不需要,可以不用设置
                personHeaderView.setTranslationY(currentY / 2);
                //动态改变标题栏的透明度,注意转化为浮点型
                float alpha = 1.0f * currentY / maxY;
                titleBar_Bg.setAlpha(alpha);
                //注意头部局的颜色也需要改变
                status_bar_fix.setAlpha(alpha);
                titleBar_title.setAlpha(alpha);
            }

            @Override
            public void onMoving(boolean isVertical) {
//                if (isVertical)
//                    viewPager.setSlide(false);
            }

            @Override
            public void onCancle() {
//                viewPager.setSlide(true);
            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterReceiver();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //当前窗口获取焦点时，才能正真拿到titlebar的高度，此时将需要固定的偏移量设置给scrollableLayout即可
        scrollableLayout.setTopOffset(titleBar.getHeight());
    }

    /**
     * 内容页的适配器
     */
    private class ContentAdapter extends FragmentPagerAdapter {

        public ContentAdapter(FragmentManager fm) {
            super(fm);
        }

        public String[] titles = new String[]{"ListView"};

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    public void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastUtil.USER_INFO_UPDATE);
        if (null == mReceiver) {
            mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    switch (action) {
                        case BroadcastUtil.USER_INFO_UPDATE:
                            AccountBean bean = UserInfoManager.getInstance().getAccountInfo();
                            accountBean = bean;
                            personHeaderView.setData(bean);
                            break;
                    }
                }
            };
        }
        registerReceiver(mReceiver, intentFilter);
    }

    public void unRegisterReceiver() {
        if (null != mReceiver) {
            unregisterReceiver(mReceiver);
        }
    }
}
