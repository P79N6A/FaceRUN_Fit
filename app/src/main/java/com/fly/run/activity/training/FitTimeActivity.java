package com.fly.run.activity.training;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.adapter.FitRecyclerAdapter;
import com.fly.run.view.actionbar.CommonActionBar;

import java.util.Arrays;

public class FitTimeActivity extends BaseUIActivity {

    private CommonActionBar actionBar;
    private RecyclerView mRecyclerView;
    private FitRecyclerAdapter mAdapter;
    //    private RecyclerView mRecyclerViewUp;
//    private FitRecyclerAdapter mAdapterUp;
//    private RecyclerView mRecyclerViewMid;
//    private FitRecyclerAdapter mAdapterMid;
    private int clickCount = 0;
    private String[] FitTrains = {"俯卧撑", "仰卧卷腹", "徒手深蹲"};
    private final String[] AllFitTrains = {"俯卧撑", "倒立撑", "引体向上", "仰卧起坐", "仰卧卷腹", "平板支撑", "徒手深蹲", "负重深蹲", "箭步蹲"};

    private String[] FitTrainsUp;
    private String[] FitTrainsMid;
    private String[] FitTrainsDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fit_time);
        FitTrains = getResources().getStringArray(R.array.array_fits);
//        FitTrainsMid = getResources().getStringArray(R.array.array_fit_mid);
//        FitTrainsDown = getResources().getStringArray(R.array.array_fit_down);
        initView();
        loadData();
    }

    private void initView() {
        initActionBar();
        initRecyclerView();
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

    private void initRecyclerView() {
        //得到控件
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        //设置布局管理器
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        mRecyclerView.setLayoutManager(linearLayoutManager);
        //设置layoutManager
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        //设置适配器
        mAdapter = new FitRecyclerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadData() {
        mAdapter.setData(Arrays.asList(FitTrains));
        mAdapter.notifyDataSetChanged();
    }
}
