package com.fly.run.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fly.run.R;
import com.fly.run.bean.FitBean;
import com.fly.run.config.UrlConstants;
import com.fly.run.utils.DisplayUtil;
import com.fly.run.utils.ImageLoaderOptions;
import com.fly.run.view.dialog.DialogFitGif;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kongwei on 2017/3/14.
 */

public class FitRecyclerAdapter extends RecyclerView.Adapter<FitRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<FitBean> datas = new ArrayList<>();
    private int minItemHeight = 130;
    private int columns = 3;
    private List<FitBean> joinDatas = new ArrayList<>();

    public FitRecyclerAdapter(Context context, int column) {
        this.mContext = context;
        layoutInflater = LayoutInflater.from(mContext);
        minItemHeight = DisplayUtil.dp2px(130);
        this.columns = column;
    }

    public void setData(List<FitBean> datas) {
        this.datas.clear();
        if (datas != null) {
            this.datas.addAll(datas);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.adapter_fit_recycler, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final FitBean bean = datas.get(position);
        holder.tv.setText(bean.getTitle());
//        Random random = new Random();
//        int result = DisplayUtil.dp2px((random.nextInt(10) + 1) * 30);
//        if (result < minItemHeight) {
//            result = minItemHeight;
//        }
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.tv.getLayoutParams();
//        params.height = result;
//        int r = random.nextInt(200) + 55;
//        int g = random.nextInt(200) + 55;
//        int b = random.nextInt(200) + 55;
//        holder.tv.setBackgroundColor(Color.argb(255, r, g, b));
//        holder.gifView.setGifResource(R.mipmap.gif3);
//        holder.gifView.setGifResource(R.drawable.menu_bg_1);
        String url = "";
        if (!TextUtils.isEmpty(bean.getImage())) {
            url = "fit/" + bean.getImage();
            url = url.trim();
            if (!url.startsWith("http://"))
                url = String.format(UrlConstants.HTTP_DOWNLOAD_FILE_2, url);
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.gifView.getLayoutParams();
        params.height = bean.getItemHeight();
        params.width = bean.getItemWidth();
        ImageLoader.getInstance().displayImage(url, holder.gifView, ImageLoaderOptions.optionsItemDefault);
//        ImageLoader.getInstance().displayImage(url, holder.gifView, ImageLoaderOptions.optionsLanuchHeader, new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String s, View view) {
//
//            }
//
//            @Override
//            public void onLoadingFailed(String s, View view, FailReason failReason) {
//
//            }
//
//            @Override
//            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                if (bitmap == null)
//                    return;
//                int w = bitmap.getWidth();
//                int h = bitmap.getHeight();
//                bean.setItemWidth(w);
//                bean.setItemHeight(h);
//                if (w == 0 || h == 0)
//                    return;
////                int itemWidth = DisplayUtil.screenWidth - 4 * DisplayUtil.dp2px(1);
//                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.gifView.getLayoutParams();
//                int viewWidth = (DisplayUtil.screenWidth - (columns + 1) * DisplayUtil.dp2px(2)) / columns;
//                int viewHeight = viewWidth * h / w;
//                params.height = viewHeight;
//                params.width = viewWidth;
//            }
//
//            @Override
//            public void onLoadingCancelled(String s, View view) {
//
//            }
//        });
        final String finalUrl = url;
        holder.gifView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogFitGif dialogFitGif = new DialogFitGif(mContext);
                dialogFitGif.setFitBean(bean);
                dialogFitGif.setOnEventListener(new DialogFitGif.OnEventListener() {
                    @Override
                    public void result(boolean sure) {
                        if (!joinDatas.contains(bean)) {
                            bean.setJoin(true);
                            joinDatas.add(bean);
                        }
                        dialogFitGif.dismiss();
                    }
                });
                dialogFitGif.setData(bean.getImage());
                dialogFitGif.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView gifView;
        TextView tv;

        public MyViewHolder(View view) {
            super(view);
            gifView = (ImageView) view.findViewById(R.id.gifView);
            tv = (TextView) view.findViewById(R.id.tv_title);
        }
    }
}
