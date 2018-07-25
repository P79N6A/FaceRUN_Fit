package com.fly.run.adapter.circle;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.fly.run.R;
import com.fly.run.activity.media.ShowImagesActivity;
import com.fly.run.bean.CircleBean;
import com.fly.run.utils.ImageLoaderOptions;
import com.fly.run.utils.Logger;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinzhendi-031 on 2016/12/14.
 */
public class FocusSearchAdapter extends BaseAdapter {

    private Context mContext;
    private List<CircleBean> datas = new ArrayList<>();
    private final int MAX_HEIGHT = 200;
    private String mImages = "";
    private String mImagesHD = "";

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

    public void setmImages(String mImages) {
        this.mImages = mImages;
    }

    public void setmImages(String mImages,String mImagesHD) {
        this.mImages = mImages;
        this.mImagesHD = mImagesHD;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
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
        CircleBean circleBean = getItem(position);
        String photos = TextUtils.isEmpty(circleBean.getThumbs()) ? circleBean.getPhotos() : circleBean.getThumbs();
        final String[] urls = photos.split(",");
        int length = urls.length;
        switch (length){
            case 0:
                break;
            case 1:
                ImageLoader.getInstance().displayImage(urls[0],viewHolder.iv1, ImageLoaderOptions.optionsGrayDefault);
                break;
            case 2:
                ImageLoader.getInstance().displayImage(urls[0],viewHolder.iv1, ImageLoaderOptions.optionsGrayDefault);
                ImageLoader.getInstance().displayImage(urls[1],viewHolder.iv2, ImageLoaderOptions.optionsGrayDefault);
                break;
            case 3:
                ImageLoader.getInstance().displayImage(urls[0],viewHolder.iv1, ImageLoaderOptions.optionsGrayDefault);
                ImageLoader.getInstance().displayImage(urls[1],viewHolder.iv2, ImageLoaderOptions.optionsGrayDefault);
                ImageLoader.getInstance().displayImage(urls[2],viewHolder.iv3, ImageLoaderOptions.optionsGrayDefault);
                break;
            case 4:
                ImageLoader.getInstance().displayImage(urls[0],viewHolder.iv1, ImageLoaderOptions.optionsGrayDefault);
                ImageLoader.getInstance().displayImage(urls[1],viewHolder.iv2, ImageLoaderOptions.optionsGrayDefault);
                ImageLoader.getInstance().displayImage(urls[2],viewHolder.iv3, ImageLoaderOptions.optionsGrayDefault);
                ImageLoader.getInstance().displayImage(urls[3],viewHolder.iv4, ImageLoaderOptions.optionsGrayDefault);
                break;
            case 5:
                ImageLoader.getInstance().displayImage(urls[0],viewHolder.iv1, ImageLoaderOptions.optionsGrayDefault);
                ImageLoader.getInstance().displayImage(urls[1],viewHolder.iv2, ImageLoaderOptions.optionsGrayDefault);
                ImageLoader.getInstance().displayImage(urls[2],viewHolder.iv3, ImageLoaderOptions.optionsGrayDefault);
                ImageLoader.getInstance().displayImage(urls[3],viewHolder.iv4, ImageLoaderOptions.optionsGrayDefault);
                ImageLoader.getInstance().displayImage(urls[4],viewHolder.iv5, ImageLoaderOptions.optionsGrayDefault);
                break;
            case 6:
                ImageLoader.getInstance().displayImage(urls[0],viewHolder.iv1, ImageLoaderOptions.optionsGrayDefault);
                ImageLoader.getInstance().displayImage(urls[1],viewHolder.iv2, ImageLoaderOptions.optionsGrayDefault);
                ImageLoader.getInstance().displayImage(urls[2],viewHolder.iv3, ImageLoaderOptions.optionsGrayDefault);
                ImageLoader.getInstance().displayImage(urls[3],viewHolder.iv4, ImageLoaderOptions.optionsGrayDefault);
                ImageLoader.getInstance().displayImage(urls[4],viewHolder.iv5, ImageLoaderOptions.optionsGrayDefault);
                ImageLoader.getInstance().displayImage(urls[5],viewHolder.iv6, ImageLoaderOptions.optionsGrayDefault);
                break;
            case 7:
                ImageLoader.getInstance().displayImage(urls[0],viewHolder.iv1, ImageLoaderOptions.optionsGrayDefault);
                ImageLoader.getInstance().displayImage(urls[1],viewHolder.iv2, ImageLoaderOptions.optionsGrayDefault);
                ImageLoader.getInstance().displayImage(urls[2],viewHolder.iv3, ImageLoaderOptions.optionsGrayDefault);
                ImageLoader.getInstance().displayImage(urls[3],viewHolder.iv4, ImageLoaderOptions.optionsGrayDefault);
                ImageLoader.getInstance().displayImage(urls[4],viewHolder.iv5, ImageLoaderOptions.optionsGrayDefault);
                ImageLoader.getInstance().displayImage(urls[5],viewHolder.iv6, ImageLoaderOptions.optionsGrayDefault);
                ImageLoader.getInstance().displayImage(urls[6],viewHolder.iv_bottom,ImageLoaderOptions.optionsGrayDefault);
                break;
        }
        viewHolder.iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int index = position*7+ 0;
                    ShowImagesActivity.startShowImageActivity(mContext, mImages,mImagesHD, index);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        viewHolder.iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int index = position*7+ 1;
                    ShowImagesActivity.startShowImageActivity(mContext, mImages,mImagesHD, index);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        viewHolder.iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int index = position*7+ 2;
                    ShowImagesActivity.startShowImageActivity(mContext, mImages,mImagesHD, index);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        viewHolder.iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int index = position*7+ 3;
                    ShowImagesActivity.startShowImageActivity(mContext, mImages,mImagesHD, index);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        viewHolder.iv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int index = position*7+ 4;
                    ShowImagesActivity.startShowImageActivity(mContext, mImages,mImagesHD, index);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        viewHolder.iv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int index = position*7+ 5;
                    ShowImagesActivity.startShowImageActivity(mContext, mImages,mImagesHD, index);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        viewHolder.iv_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int index = position*7+ 6;
                    ShowImagesActivity.startShowImageActivity(mContext, mImages,mImagesHD, index);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        public ImageView iv1,iv2,iv3,iv4,iv5,iv6;
        public ImageView iv_bottom;

        public ViewHolder(View view) {
            iv1 = (ImageView) view.findViewById(R.id.iv_1);
            iv2 = (ImageView) view.findViewById(R.id.iv_2);
            iv3 = (ImageView) view.findViewById(R.id.iv_3);
            iv4 = (ImageView) view.findViewById(R.id.iv_4);
            iv5 = (ImageView) view.findViewById(R.id.iv_5);
            iv6 = (ImageView) view.findViewById(R.id.iv_6);
            iv_bottom = (ImageView) view.findViewById(R.id.iv_bottom);
        }
    }
}
