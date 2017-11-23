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
public class PersonInfoAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> datas = new ArrayList<>();

    public PersonInfoAdapter(Context context) {
        this.mContext = context;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_person_info, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_title.setText(getItem(position));
        return convertView;
    }

    private class ViewHolder {
        public TextView tv_title;
        public ImageView iv_arrow;

        public ViewHolder(View view) {
            this.tv_title = (TextView) view.findViewById(R.id.tv_title);
            this.iv_arrow = (ImageView) view.findViewById(R.id.iv_arrow);
        }
    }
}
