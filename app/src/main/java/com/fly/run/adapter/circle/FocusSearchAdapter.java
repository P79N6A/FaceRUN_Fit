package com.fly.run.adapter.circle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.fly.run.R;
import com.fly.run.bean.CircleBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinzhendi-031 on 2016/12/14.
 */
public class FocusSearchAdapter extends BaseAdapter {

    private Context mContext;
    private List<CircleBean> datas = new ArrayList<>();
    private final int MAX_HEIGHT = 200;

    public FocusSearchAdapter(Context context) {
        this.mContext = context;
    }

    public List<CircleBean> getDatas() {
        return datas;
    }

    public void addData(List<CircleBean> list) {
        if (list != null) {
            datas.addAll(list);
        }
    }

    public void setData(List<CircleBean> list) {
        datas.clear();
        if (list != null) {
            datas.addAll(list);
        }
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public CircleBean getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemType = getItemViewType(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            if (itemType == 0) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_circle_focus_search_video_right, null);
            } else {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_circle_focus_search_video_left, null);
            }
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CircleBean item = getItem(position);
        if (item != null) {
            if (itemType == 0) {
                viewHolder.iv_bottom.setVisibility(View.VISIBLE);
            } else {
                viewHolder.iv_bottom.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    private class ViewHolder {
        public ImageView iv1;
        public ImageView iv_bottom;

        public ViewHolder(View view) {
            iv1 = (ImageView) view.findViewById(R.id.iv_1);
            iv_bottom = (ImageView) view.findViewById(R.id.iv_bottom);
        }
    }
}
