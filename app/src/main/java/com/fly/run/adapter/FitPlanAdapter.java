package com.fly.run.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fly.run.R;
import com.fly.run.bean.FitPlanBean;
import com.fly.run.config.UrlConstants;
import com.fly.run.utils.DisplayUtil;
import com.fly.run.utils.SDCardUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinzhendi-031 on 2016/12/14.
 */
public class FitPlanAdapter extends BaseAdapter {

    private Context mContext;
    private List<FitPlanBean> datas = new ArrayList<>();

    public FitPlanAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<FitPlanBean> list) {
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
    public FitPlanBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_fit_plan, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        FitPlanBean bean = getItem(position);

        viewHolder.tvTitle.setText(bean.getTitle());
        String mTimes = "";
        if (bean.getCountstype() == 2) { //计数
            mTimes = bean.getTimes() + " X " + bean.getCounts() + "次";
        } else { //计时
            mTimes = bean.getTimes() + " X " + bean.getCounts() + "s";
        }
        viewHolder.tvTimes.setText(mTimes);
        String url = bean.getImage();
        String filePath = new String(SDCardUtil.getGifDir() + "/" + url);
        File file = new File(filePath);
        if (file.exists() && file.length() > 0) {
            url = "file://"+filePath;
        } else if (!url.startsWith("http://") || !url.startsWith("https://"))
            url = String.format(UrlConstants.HTTP_DOWNLOAD_FILE_2, "fit/" + url);
        final ViewHolder finalViewHolder = viewHolder;
        ImageLoader.getInstance().displayImage(url, viewHolder.ivIcon, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                if (bitmap == null)
                    return;
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) finalViewHolder.ivIcon.getLayoutParams();
                params.width = DisplayUtil.screenWidth - DisplayUtil.dp2px(2 * 8);
                params.height = h * params.width / w;
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
        return convertView;
    }

    private class ViewHolder {
        public ImageView ivIcon;
        public TextView tvTitle;
        public TextView tvTimes;

        public ViewHolder(View view) {
            this.ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
            this.tvTitle = (TextView) view.findViewById(R.id.tv_title);
            this.tvTimes = (TextView) view.findViewById(R.id.tv_times);
        }
    }
}
