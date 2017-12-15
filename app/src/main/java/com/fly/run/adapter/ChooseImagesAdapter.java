package com.fly.run.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fly.run.R;
import com.fly.run.bean.FileItem;
import com.fly.run.utils.DisplayUtil;
import com.fly.run.utils.ImageLoaderOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinzhendi-031 on 2017/11/13.
 */
public class ChooseImagesAdapter extends BaseAdapter {

    private List<FileItem> urlImages;
    private Context mContext;
    private List<FileItem> ChooseItems = new ArrayList<>();

    public ChooseImagesAdapter(Context context) {
        this.mContext = context;
    }

    public List<FileItem> getChooseItems() {
        return ChooseItems;
    }

    public void setData(List<FileItem> urlImages){
        this.urlImages = urlImages;
    }

    @Override
    public int getCount() {
        return urlImages == null ? 0 : urlImages.size();
    }

    @Override
    public FileItem getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_choose_images, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final FileItem item = getItem(position);
        if (!TextUtils.isEmpty(item.getFilePath())) {
            if (item.isCheck())
                viewHolder.ivChoose.setImageResource(R.drawable.ic_new_chat_press);
            else
                viewHolder.ivChoose.setImageResource(R.drawable.ic_new_chat_nor);
            ImageLoader.getInstance().displayImage("file:///"+item.getFilePath(), viewHolder.ivHeader, ImageLoaderOptions.optionsLanuchHeader);
            viewHolder.ivHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.doShowImage(position, item.getFilePath());
                    }
                }
            });
            viewHolder.ivChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.doChooseImage(position, item.getFilePath());
                    }
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        private ImageView ivHeader;
        private ImageView ivChoose;

        ViewHolder(View view) {
            ivChoose = (ImageView) view.findViewById(R.id.iv_choose);
            ivHeader = (ImageView) view.findViewById(R.id.iv_icon);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivHeader.getLayoutParams();
            params.width = (DisplayUtil.screenWidth - DisplayUtil.dp2px(8)) / 3;
            params.height = params.width;
        }
    }

    private ImageListenrt listener;

    public void setImageListener(ImageListenrt listener) {
        this.listener = listener;
    }

    public interface ImageListenrt {
        public void doShowImage(int position, String url);

        public void doChooseImage(int position, String url);
    }
}
