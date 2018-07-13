package com.fly.run.view.circle.FocusRecyclerView;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.fly.run.R;
import com.fly.run.adapter.circle.FocusRecyclerAdapter;
import com.fly.run.bean.FocusRecyclerBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinzhendi-031 on 2018/4/13.
 */

public class FocusRecyclerView extends RelativeLayout {

    private RecyclerView recyclerView;
    private FocusRecyclerAdapter adapter;
    private List<FocusRecyclerBean> datas = new ArrayList<>();

    public FocusRecyclerView(Context context) {
        super(context);
        initView(context, null);
    }

    public FocusRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public FocusRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_focus_recycler, this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        //创建LinearLayoutManager 对象 这里使用 <span style="font-family:'Source Code Pro';">LinearLayoutManager 是线性布局的意思</span>
        LinearLayoutManager layoutmanager = new LinearLayoutManager(getContext());
        //设置RecyclerView 布局
        recyclerView.setLayoutManager(layoutmanager);
        //设置为水平布局，这也是默认的
        layoutmanager.setOrientation(OrientationHelper.HORIZONTAL);
        //设置Adapter
        adapter = new FocusRecyclerAdapter(getContext());
        recyclerView.setAdapter(adapter);
        initData();
    }

    private void initData() {
        datas.clear();
        for (int i = 0; i < 10; i++) {
            FocusRecyclerBean bean = new FocusRecyclerBean();
            bean.setId(i + 1);
            bean.setName("hello " + i);
            bean.setHeaderUrl("");
            datas.add(bean);
        }
        adapter.setDatas(datas);
        adapter.notifyDataSetChanged();
    }

    public void setData(List<FocusRecyclerBean> list) {
        adapter.setDatas(list);
        adapter.notifyDataSetChanged();
    }
}
