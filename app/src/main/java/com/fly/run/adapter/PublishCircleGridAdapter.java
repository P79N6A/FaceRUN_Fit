package com.fly.run.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fly.run.R;
import com.fly.run.utils.DisplayUtil;
import com.fly.run.utils.ImageLoaderOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinzhendi-031 on 2017/11/13.
 */
public class PublishCircleGridAdapter extends BaseAdapter {

    private List<String> urlImages = new ArrayList<>();
    private Context mContext;

    public PublishCircleGridAdapter(Context context) {
        this.mContext = context;
    }

    public void setUrlImages(List<String> urlImages) {
        this.urlImages.clear();
        if (urlImages != null)
            this.urlImages.addAll(urlImages);
        if (urlImages == null || this.urlImages.size() < 9)
            this.urlImages.add("ADD");
    }

    public void addUrlImages(List<String> urlImages) {
        this.urlImages.addAll(urlImages);
    }

    @Override
    public int getCount() {
        return urlImages == null ? 0 : urlImages.size();
    }

    @Override
    public String getItem(int position) {
        return urlImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_publish_circle, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final String url = getItem(position);
        if (!TextUtils.isEmpty(url)) {
            if (url.equals("ADD")) {
                viewHolder.ivHeader.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                String add_url = "drawable://" + R.drawable.ic_nav_menu_add;
                ImageLoader.getInstance().displayImage(add_url, viewHolder.ivHeader, ImageLoaderOptions.optionsGrayDefault);
            } else {
                viewHolder.ivHeader.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ImageLoader.getInstance().displayImage("file:///" + url, viewHolder.ivHeader, ImageLoaderOptions.optionsGrayDefault);
            }
            viewHolder.ivHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        if (url.equals("ADD"))
                            listener.doAddImage();
                        else
                            listener.doShowImage(position, url);
                    }
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        private ImageView ivHeader;

        ViewHolder(View view) {
            ivHeader = (ImageView) view.findViewById(R.id.iv_icon);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ivHeader.getLayoutParams();
            params.width = (DisplayUtil.screenWidth - DisplayUtil.dp2px(32)) / 3;
            params.height = params.width;
        }
    }

    private ImageListenrt listener;

    public void setImageListener(ImageListenrt listener) {
        this.listener = listener;
    }

    public interface ImageListenrt {
        public void doShowImage(int position, String url);

        public void doAddImage();
    }
}
