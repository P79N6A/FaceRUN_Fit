package com.fly.run.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fly.run.R;
import com.fly.run.bean.RunBean;
import com.fly.run.utils.LocationUtils;
import com.fly.run.utils.TimeFormatUtils;
import com.fly.run.view.SwipeListView.BaseSwipListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xinzhendi-031 on 2016/12/14.
 */
public class RecordAdapter extends BaseSwipListAdapter {

    private Context mContext;
    private List<RunBean> datas = new ArrayList<>();
    private Map<String, List<Object>> dataMap = new HashMap<>();
    private List<String> dateFormatList = new ArrayList<>();
    public boolean isEditState = false;

    public RecordAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<RunBean> list) {
        if (list != null && list.size() > 0) {
            datas.clear();
            datas.addAll(list);
        }
    }

    public List<RunBean> getDatas() {
        return datas;
    }

    public boolean isEditState() {
        return isEditState;
    }

    public RecordAdapter setEditState(boolean editState) {
        isEditState = editState;
        if (!editState) {
            for (RunBean bean : datas)
                bean.setCheck(false);
        }
        return this;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public RunBean getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean getSwipEnableByPosition(int position) {
        return getItemViewType(position) == 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewTitleHolder viewTitleHolder = null;
        ViewHolder viewHolder = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            if (type == 1) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_record_head, null);
                viewTitleHolder = new ViewTitleHolder(convertView, type);
                convertView.setTag(viewTitleHolder);
            } else {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_record, null);
                viewHolder = new ViewHolder(convertView, type);
                convertView.setTag(viewHolder);
            }
        } else {
            if (type == 1) {
                viewTitleHolder = (ViewTitleHolder) convertView.getTag();
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
        }
        final RunBean bean = getItem(position);
        if (type == 1) {
            viewTitleHolder.tvHeadDate.setText(bean.getRunHeadDate());
            viewTitleHolder.tvHeadDistance.setText(TimeFormatUtils.retainOne(bean.getRunHeadDistance() / 1000) + "公里");
            return convertView;
        }
        viewHolder.tvAddress.setText(bean.getmNearBy());
        viewHolder.tvDate.setText(TimeFormatUtils.getFormatDate5(Long.parseLong(bean.getmRunDate())));
        long useTime = 0;
        try {
            String strUseTime = bean.getmUseTime();
            useTime = Long.parseLong(strUseTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewHolder.tvUseTime.setText(TimeFormatUtils.formatDurationHHMMss(useTime));
        double distance = Double.parseDouble(bean.getmRunDistance());
        viewHolder.tvDistance.setText(TimeFormatUtils.retainTwo(distance / 1000f));
        if (distance > 0) {
            String strSpeed = LocationUtils.caculateSpeed(distance, useTime);
            if (strSpeed.endsWith("/公里")) {
                bean.setmRunSpeed(strSpeed.substring(0, strSpeed.length() - 4));
            }
            viewHolder.tvSpeed.setText(strSpeed);
        } else {
            viewHolder.tvSpeed.setText("--");
        }

        if (isEditState()) {
            viewHolder.ivIcon.setVisibility(View.INVISIBLE);
            viewHolder.ivSelect.setVisibility(View.VISIBLE);
            viewHolder.ivSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bean.setCheck(!bean.isCheck());
                    if (bean.isCheck())
                        ((ImageView) v).setImageResource(R.drawable.ic_people_choose_press);
                    else
                        ((ImageView) v).setImageResource(R.drawable.ic_people_choose_nol);
                }
            });
            if (bean.isCheck())
                viewHolder.ivSelect.setImageResource(R.drawable.ic_people_choose_press);
            else
                viewHolder.ivSelect.setImageResource(R.drawable.ic_people_choose_nol);
        } else {
            viewHolder.ivIcon.setVisibility(View.VISIBLE);
            viewHolder.ivSelect.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }


    private class ViewTitleHolder {
        public TextView tvHeadDate, tvHeadDistance;

        public ViewTitleHolder(View view, int type) {
            this.tvHeadDate = (TextView) view.findViewById(R.id.tv_head_date);
            this.tvHeadDistance = (TextView) view.findViewById(R.id.tv_head_distance);
        }
    }

    private class ViewHolder {
        private ImageView ivIcon, ivSelect;
        public TextView tvAddress, tvDate, tvDistance, tvSpeed, tvUseTime;

        public ViewHolder(View view, int type) {
            this.ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
            this.ivSelect = (ImageView) view.findViewById(R.id.iv_select);
            this.tvAddress = (TextView) view.findViewById(R.id.tv_address);
            this.tvDate = (TextView) view.findViewById(R.id.tv_date);
            this.tvDistance = (TextView) view.findViewById(R.id.tv_distance);
            this.tvSpeed = (TextView) view.findViewById(R.id.tv_speed);
            this.tvUseTime = (TextView) view.findViewById(R.id.tv_use_time);
        }
    }
}
