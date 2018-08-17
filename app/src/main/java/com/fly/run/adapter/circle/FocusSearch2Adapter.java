package com.fly.run.adapter.circle;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fly.run.R;
import com.fly.run.activity.media.ShowImagesActivity;
import com.fly.run.bean.CircleBean;
import com.fly.run.bean.ImageUrlWHBean;
import com.fly.run.config.UrlConstants;
import com.fly.run.utils.DisplayUtil;
import com.fly.run.utils.ImageLoaderOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xinzhendi-031 on 2016/12/14.
 */
public class FocusSearch2Adapter extends RecyclerView.Adapter<FocusSearch2Adapter.BeautyViewHolder>  {

    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 数据集合
     */
    private List<CircleBean> data = new ArrayList<>();

    private Map<String,ImageUrlWHBean> urlWHBeanMap = new HashMap<>();

    public FocusSearch2Adapter(Context context) {
        this.data = data;
        this.mContext = context;
    }

    public FocusSearch2Adapter(List<CircleBean> data, Context context) {
        this.data = data;
        this.mContext = context;
    }

    public void setData(List<CircleBean> data) {
        this.data.clear();
        if (data == null)
            return;
        this.data.addAll(data);
    }

    @Override
    public BeautyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载item 布局文件
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.beauty_item, parent, false);
        return new BeautyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BeautyViewHolder holder, int position) {
        //将数据设置到item上
        CircleBean circleBean = data.get(position);
//        holder.beautyImage.setImageResource(circleBean.getPhotos());
        holder.nameTv.setText(circleBean.getName());
        String thumbs = circleBean.getThumbs();
        String photos = circleBean.getPhotos();
        String itemImg = "";
        String itemImgHD = "";
        if (!TextUtils.isEmpty(thumbs)){
            itemImg = thumbs.split(",")[0];
        } else if (!TextUtils.isEmpty(photos)){
            itemImg = photos.split(",")[0];
        }
        if (!TextUtils.isEmpty(photos)){
            itemImgHD = photos.split(",")[0];

        }
        if (!TextUtils.isEmpty(itemImg)) {
            itemImg = itemImg.trim();
            if (!itemImg.startsWith("http://") && !itemImg.startsWith("https://"))
                itemImg = String.format(UrlConstants.HTTP_DOWNLOAD_FILE_2, itemImg.trim());
        }
        ImageLoader.getInstance().displayImage(itemImg, holder.beautyImage, ImageLoaderOptions.optionsGrayDefault, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                if (urlWHBeanMap.containsKey(s)){
                    ImageUrlWHBean bean = urlWHBeanMap.get(s);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.beautyImage.getLayoutParams();
                    params.height = bean.getImageHeight();
                    params.weight = bean.getImageWidth();
                    holder.beautyImage.setLayoutParams(params);
                }
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                if (bitmap == null || TextUtils.isEmpty(s))
                    return;
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                int viewWidth = width;
                int viewHeight = height;
                if (urlWHBeanMap.containsKey(s)){
                    ImageUrlWHBean bean = urlWHBeanMap.get(s);
                    viewWidth = bean.getImageWidth();
                    viewHeight = bean.getImageHeight();
                } else {
                    viewWidth = (DisplayUtil.screenWidth - DisplayUtil.dp2px(12)) / 2;
                    viewHeight = (int) (viewWidth * height * 1.0f / width);
                }
                if (!urlWHBeanMap.containsKey(s)){
                    ImageUrlWHBean bean = new ImageUrlWHBean();
                    bean.setImageWidth(viewWidth);
                    bean.setImageHeight(viewHeight);
                    urlWHBeanMap.put(s,bean);
                }
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.beautyImage.getLayoutParams();
                params.height = viewHeight;
                params.weight = viewWidth;
                holder.beautyImage.setLayoutParams(params);
                holder.beautyImage.setImageBitmap(bitmap);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
        final String finalItemImg = itemImg;
        final String finalItemImgHD = itemImgHD;
        holder.beautyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowImagesActivity.startShowImageActivity(mContext, finalItemImg, finalItemImgHD, 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class BeautyViewHolder extends RecyclerView.ViewHolder {
        ImageView beautyImage;
        TextView nameTv;

        public BeautyViewHolder(View itemView) {
            super(itemView);
            beautyImage = (ImageView) itemView.findViewById(R.id.image_item);
            nameTv = (TextView) itemView.findViewById(R.id.name_item);
        }
    }
}
