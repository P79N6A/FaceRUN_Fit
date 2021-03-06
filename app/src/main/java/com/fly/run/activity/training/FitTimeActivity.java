package com.fly.run.activity.training;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.adapter.FitRecyclerAdapter;
import com.fly.run.bean.FitBean;
import com.fly.run.bean.ResultTaskBean;
import com.fly.run.httptask.HttpTaskUtil;
import com.fly.run.utils.DisplayUtil;
import com.fly.run.utils.ToastUtil;
import com.fly.run.view.actionbar.CommonActionBar;
import com.squareup.okhttp.Request;

import java.util.List;

public class FitTimeActivity extends BaseUIActivity {

    private CommonActionBar actionBar;
    private RecyclerView mRecyclerView;
    private FitRecyclerAdapter mAdapter;
    private int clickCount = 0;
    private String[] FitTrains = {"俯卧撑", "仰卧卷腹", "徒手深蹲"};
    private final String[] AllFitTrains = {"俯卧撑", "倒立撑", "引体向上", "仰卧起坐", "仰卧卷腹", "平板支撑", "徒手深蹲", "负重深蹲", "箭步蹲"};

    private HttpTaskUtil httpTaskUtil;
    private int columns = 3;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fit_time);
        FitTrains = getResources().getStringArray(R.array.array_fits);
        initView();
        loadTaskData();
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
        floatingActionButton = (FloatingActionButton)findViewById(R.id.float_action);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        if (DisplayUtil.screenWidth < 600)
            columns = 2;
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(columns, StaggeredGridLayoutManager.VERTICAL));
        //设置适配器
        mAdapter = new FitRecyclerAdapter(this, columns);
        mAdapter.setHandler(handler);
        mRecyclerView.setAdapter(mAdapter);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                List<FitBean> list = mAdapter.getJoinDatas();

            }
        }
    };

    private void loadTaskData() {
        if (httpTaskUtil == null) {
            httpTaskUtil = new HttpTaskUtil();
            httpTaskUtil.setResultListener(resultListener);
        }
        httpTaskUtil.QueryFitTask();
    }

    HttpTaskUtil.ResultListener resultListener = new HttpTaskUtil.ResultListener() {
        @Override
        public void onResponse(String response) {
            try {
                ResultTaskBean bean = JSON.parseObject(response, ResultTaskBean.class);
                if (bean != null && bean.code == 1) {
                    if (!TextUtils.isEmpty(bean.data)) {
                        List<FitBean> list = JSON.parseArray(bean.data, FitBean.class);
//                        Random random = new Random();
//                        int width = (DisplayUtil.screenWidth - (columns + 1) * DisplayUtil.dp2px(2)) / columns;
//                        for (FitBean fitBean : list){
//                            int result = DisplayUtil.dp2px(((random.nextInt(10) + 1)) * 10);
//                            fitBean.setItemWidth(width);
//                            fitBean.setItemHeight(width+result);
//                        }
                        mAdapter.setData(list);
                        mAdapter.notifyDataSetChanged();
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
