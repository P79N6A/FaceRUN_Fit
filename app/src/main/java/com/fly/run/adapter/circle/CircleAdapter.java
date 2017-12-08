package com.fly.run.adapter.circle;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fly.run.R;
import com.fly.run.bean.CircleBean;
import com.fly.run.utils.ImageLoaderOptions;
import com.fly.run.view.LoadImagesView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinzhendi-031 on 2016/12/14.
 */
public class CircleAdapter extends BaseAdapter {

    private Context mContext;
    private List<CircleBean> datas = new ArrayList<>();

    public CircleAdapter(Context context) {
        this.mContext = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_circle_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CircleBean item = getItem(position);
        if (item != null) {
            if (!TextUtils.isEmpty(item.getName()))
                viewHolder.tvName.setText(item.getName());
            if (item.getCreateAt() != null)
                viewHolder.tvTime.setText(item.getCreateAt().toLocaleString());
            if (!TextUtils.isEmpty(item.getDesc()))
                viewHolder.tvDesc.setText(item.getDesc());
            if (!TextUtils.isEmpty(item.getAddress()))
                viewHolder.tvAddress.setText(item.getAddress());
            ImageLoader.getInstance().displayImage(item.getHeaderUrl(), viewHolder.ivHeader, ImageLoaderOptions.optionsLanuchHeader);
            String url1 = "http://pic.melinked.com/me2017/a9/wrskmvrxdoak.jpg,";
            String url2 = "http://pic.melinked.com/me2017/a29/gb8j3bcg5q8g.jpg,";
            String url3 = "http://pic.melinked.com/me2017/a0/owquaseo63yb.jpg,";
            String url = "http://pic.melinked.com/me2017/a9/wrskmvrxdoak.jpg," +
                    "http://pic.melinked.com/me2017/a29/gb8j3bcg5q8g.jpg," +
                    "http://pic.melinked.com/me2017/a0/owquaseo63yb.jpg,";
            switch (position) {
                case 0:
                    url = url1;
                    break;
                case 1:
                    url = url2;
                    break;
                case 2:
                    url = url3;
                    break;
                case 3:
                    url = url1 + url2;
                    break;
                case 4:
                    url = url1 + url3;
                    break;
                case 5:
                    url = url + url1;
                    break;
                case 6:
                    url = url + url1 + url3;
                    break;
                case 7:
                    url = url + url + url3;
                    break;
                case 8:
                    url = url + url + url;
                    break;
                default:
                    break;
            }
            viewHolder.loadImagesView.setImagesData(url);
        }
        return convertView;
    }

    private class ViewHolder {
        public ImageView ivHeader;
        public TextView tvName;
        public TextView tvTime;
        public TextView tvDesc;
        public TextView tvAddress;
        public LoadImagesView loadImagesView;

        public ViewHolder(View view) {
            this.ivHeader = (ImageView) view.findViewById(R.id.iv_header_icon);
            this.tvName = (TextView) view.findViewById(R.id.tv_name);
            this.tvTime = (TextView) view.findViewById(R.id.tv_time);
            this.tvDesc = (TextView) view.findViewById(R.id.tv_desc);
            this.tvAddress = (TextView) view.findViewById(R.id.tv_address);
            this.loadImagesView = (LoadImagesView) view.findViewById(R.id.view_load_images);
        }
    }
}
