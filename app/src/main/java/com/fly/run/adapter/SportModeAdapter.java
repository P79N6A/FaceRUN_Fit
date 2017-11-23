package com.fly.run.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fly.run.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinzhendi-031 on 2016/12/14.
 */
public class SportModeAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> datas = new ArrayList<>();
    private int mIndex = -1;

    public SportModeAdapter(Context context) {
        this.mContext = context;
    }

    public SportModeAdapter(Context context, int index) {
        this.mContext = context;
        this.mIndex = index;
    }

    public void setData(List<String> list) {
        if (list != null) {
            datas.clear();
            datas.addAll(list);
        }
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public String getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_sport_mode, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvData.setText(getItem(position));
        if (mIndex == position)
            viewHolder.ivSelector.setVisibility(View.VISIBLE);
        else
            viewHolder.ivSelector.setVisibility(View.INVISIBLE);
        return convertView;
    }

    private class ViewHolder {
        public TextView tvData;
        public ImageView ivSelector;

        public ViewHolder(View view) {
            this.tvData = (TextView) view.findViewById(R.id.tv_title);
            this.ivSelector = (ImageView) view.findViewById(R.id.iv_selector);
        }
    }

    public int getmIndex() {
        return mIndex;
    }

    public SportModeAdapter setmIndex(int mIndex) {
        this.mIndex = mIndex;
        return this;
    }
}
