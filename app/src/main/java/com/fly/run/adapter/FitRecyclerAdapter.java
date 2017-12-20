package com.fly.run.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fly.run.R;
import com.fly.run.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by kongwei on 2017/3/14.
 */

public class FitRecyclerAdapter extends RecyclerView.Adapter<FitRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<String> datas = new ArrayList<>();
    private int minItemHeight = 130;

    public FitRecyclerAdapter(Context context) {
        this.mContext = context;
        layoutInflater = LayoutInflater.from(mContext);
        minItemHeight = DisplayUtil.dp2px(130);
    }

    public void setData(List<String> datas) {
        if (datas != null) {
            this.datas.clear();
            this.datas.addAll(datas);
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.adapter_fit_recycler, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv.setText(datas.get(position));
        Random random = new Random();
        int result = DisplayUtil.dp2px((random.nextInt(10) + 1) * 30);
        if (result < minItemHeight) {
            result = minItemHeight;
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.tv.getLayoutParams();
        params.height = result;
        int r = random.nextInt(200) + 55;
        int g = random.nextInt(200) + 55;
        int b = random.nextInt(200) + 55;
        holder.tv.setBackgroundColor(Color.argb(255, r, g, b));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv_title);
        }
    }
}
