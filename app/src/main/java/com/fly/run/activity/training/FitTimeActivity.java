package com.fly.run.activity.training;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.adapter.FitRecyclerAdapter;
import com.fly.run.adapter.FitTimeAdapter;
import com.fly.run.utils.DisplayUtil;
import com.fly.run.utils.ToastUtil;
import com.fly.run.view.CircularProgressBar;
import com.fly.run.view.SwipeListView.SwipeMenu;
import com.fly.run.view.SwipeListView.SwipeMenuCreator;
import com.fly.run.view.SwipeListView.SwipeMenuItem;
import com.fly.run.view.SwipeListView.SwipeMenuListView;
import com.fly.run.view.actionbar.CommonActionBar;

import java.util.Arrays;

public class FitTimeActivity extends BaseUIActivity {

    private CommonActionBar actionBar;
    private SwipeMenuListView listView;
    private FitTimeAdapter adapter;
    private RecyclerView mRecyclerView;
    private FitRecyclerAdapter mAdapter;
    private CircularProgressBar circularProgressBar;
    private int clickCount = 0;
    private final String[] FitTrains = {"俯卧撑", "仰卧卷腹", "徒手深蹲"};
    private final String[] AllFitTrains = {"俯卧撑", "倒立撑", "引体向上", "仰卧起坐", "仰卧卷腹", "平板支撑", "徒手深蹲", "负重深蹲", "箭步蹲"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fit_time);
        initView();
        loadData();
    }

    private void initView() {
        initActionBar();
        initSwipeListView();
        initRecyclerView();
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        circularProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCount++;
                if (clickCount % 2 == 0) {
                    circularProgress(100, 2000);
                } else {
                    circularProgress(0, 2000);
                }
            }
        });
        circularProgress(100, 2000);
    }

    private void circularProgress(final float progress, final int duration) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                circularProgressBar.setProgressWithAnimation(progress, duration);
            }
        }, 200);
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

    private SwipeMenuCreator createSwipeMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0x53, 0xbc,
                        0x68)));
                // set item width
                openItem.setWidth(DisplayUtil.dp2px(80));
                // set item title
                openItem.setTitle(getString(R.string.action_swipe_edit));
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                SwipeMenuItem delItem = new SwipeMenuItem(
                        getApplicationContext());
                // create "delete" item
                delItem.setBackground(new ColorDrawable(Color.rgb(0xD9, 0x26,
                        0x1B)));
                // set item width
                delItem.setWidth(DisplayUtil.dp2px(80));
                // set item title
                delItem.setTitle(getString(R.string.action_swipe_delete));
                // set item title fontsize
                delItem.setTitleSize(18);
                // set item title font color
                delItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(delItem);
            }
        };
        return creator;
    }

    private void initSwipeListView() {
        listView = (SwipeMenuListView) findViewById(R.id.listview);
        adapter = new FitTimeAdapter(this);
        listView.setAdapter(adapter);
        // step 1. create a MenuCreator
        // set creator
        listView.setMenuCreator(createSwipeMenu());

        // step 2. listener item click event
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // edit
                        ToastUtil.show(getString(R.string.action_swipe_edit));
                        break;
                    case 1:
                        // delete
                        ToastUtil.show(getString(R.string.action_swipe_delete));
                        break;
                }
                return false;
            }
        });

        // set SwipeListener
        listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // set MenuStateChangeListener
        listView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
            }

            @Override
            public void onMenuClose(int position) {
            }
        });

        // other setting
//		listView.setCloseInterpolator(new BounceInterpolator());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }


    private void initRecyclerView() {
        //得到控件
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        //设置适配器
        mAdapter = new FitRecyclerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadData() {
        adapter.setData(Arrays.asList(FitTrains));
        adapter.notifyDataSetChanged();
        mAdapter.setData(Arrays.asList(AllFitTrains));
        mAdapter.notifyDataSetChanged();
    }
}
