package com.fly.run.adapter.circle;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fly.run.R;
import com.fly.run.bean.CircleBean;
import com.fly.run.bean.CircleReply;
import com.fly.run.utils.DisplayUtil;
import com.fly.run.utils.ImageLoaderOptions;
import com.fly.run.view.ImageView.RoundAngleImageView;
import com.fly.run.view.LoadImagesView;
import com.fly.run.view.circle.CircleBottomReviewView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinzhendi-031 on 2016/12/14.
 */
public class CircleReplyAdapter extends BaseAdapter {

    private Context mContext;
    private List<CircleReply> datas = new ArrayList<>();
    private final int MAX_HEIGHT = 200;

    public CircleReplyAdapter(Context context) {
        this.mContext = context;
    }

    public List<CircleReply> getDatas() {
        return datas;
    }

    public void addData(List<CircleReply> list) {
        if (list != null) {
            datas.addAll(list);
        }
    }

    public void setData(List<CircleReply> list) {
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
    public CircleReply getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_circle_reply, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CircleReply item = getItem(position);
        if (item != null) {
//            ImageLoader.getInstance().displayImage("",viewHolder.iv_header);
            viewHolder.tvName.setText(item.getReplyUserName()+":"+item.getReplyContent());
            viewHolder.tvTime.setText(item.getReplyTime().toLocaleString());
            if (item.getReplyRootId() > 0){
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.iv_header.getLayoutParams();
                params.leftMargin = DisplayUtil.dp2px(38);
                viewHolder.iv_header.setLayoutParams(params);
            }
        }
        return convertView;
    }

    private class ViewHolder {
        private ImageView iv_header;
        public TextView tvName;
        public TextView tvTime;

        public ViewHolder(View view) {
            this.iv_header = (ImageView)view.findViewById(R.id.iv_header);
            this.tvName = (TextView) view.findViewById(R.id.tv_name);
            this.tvTime = (TextView) view.findViewById(R.id.tv_time);
        }
    }
}
