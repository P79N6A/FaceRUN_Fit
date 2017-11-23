package com.fly.run.activity.person;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.adapter.PersonInfoAdapter;
import com.fly.run.manager.UserInfoManager;
import com.fly.run.utils.DisplayUtil;
import com.fly.run.view.HeaderView.PersonHeaderView;
import com.fly.run.view.actionbar.TransparentActionBar;

public class PersonInfoActivity extends BaseUIActivity {

    private TransparentActionBar actionBar;
    private ListView listView;
    private PersonInfoAdapter adapter;
    private PersonHeaderView personHeaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        initActionBar();
        listView = (ListView) findViewById(R.id.listview);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (personHeaderView != null && actionBar != null) {
                    int bottom = personHeaderView.getBottom();
                    if (bottom <= actionHeight + statusHeight + statusHeight && !actionBar.isChangeTheme) {
                        actionBar.setBackgroundSrc(0x00000000, 0xff2E7D32);
                    } else if (bottom > actionHeight + statusHeight + statusHeight && actionBar.isChangeTheme) {
                        actionBar.setBackgroundTransparent(0xff2E7D32, 0x00000000);
                    }
                }
            }
        });
        initHeaderView();
        initFooterView();
        adapter = new PersonInfoAdapter(this);
        listView.setAdapter(adapter);
        loadData();
    }

    private void initActionBar() {
        actionBar = (TransparentActionBar) findViewById(R.id.transparent_action_bar);
//        actionBar.setActionTitle("个人信息");
        actionBar.setActionLeftIconListenr(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initHeaderView() {
        personHeaderView = new PersonHeaderView(this);
        listView.addHeaderView(personHeaderView);
    }

    private int HeaderHeight;
    private int actionHeight;
    private int statusHeight;

    private void initFooterView() {
        int screenHeight = DisplayUtil.screenHeight;
        HeaderHeight = DisplayUtil.dp2px(260);
        actionHeight = DisplayUtil.dp2px(45);
        statusHeight = DisplayUtil.getStatusBarHeight(this);
        View footerView = new View(this);
        footerView.setMinimumHeight(screenHeight + actionHeight + statusHeight);
        listView.addFooterView(footerView);
    }

    private void loadData() {
        personHeaderView.setData(UserInfoManager.getInstance().getAccountInfo());
    }
}
