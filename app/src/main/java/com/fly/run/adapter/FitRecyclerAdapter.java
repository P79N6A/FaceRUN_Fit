package com.fly.run.adapter;

import android.content.Context;
import android.os.Handler;
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
    private Handler handler;

    public FitRecyclerAdapter(Context context, int column) {
        this.mContext = context;
        layoutInflater = LayoutInflater.from(mContext);
        minItemHeight = DisplayUtil.dp2px(130);
        this.columns = column;
    }

    public FitRecyclerAdapter setHandler(Handler handler) {
        this.handler = handler;
        return this;
    }

    public void setData(List<FitBean> datas) {
        this.datas.clear();
        if (datas != null) {
            this.datas.addAll(datas);
        }
    }

    public List<FitBean> getJoinDatas() {
        return joinDatas;
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
        String url = "";
        if (!TextUtils.isEmpty(bean.getImage())) {
            url = "fit/" + bean.getImage();
            url = url.trim();
            if (!url.startsWith("http://"))
                url = String.format(UrlConstants.HTTP_DOWNLOAD_FILE_2, url);
        }
        ImageLoader.getInstance().displayImage(url, holder.gifView, ImageLoaderOptions.optionsLanuchHeader);
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
                        } else {
                            if (bean.isJoin()) {
                                bean.setJoin(false);
                                joinDatas.remove(bean);
                            }
                        }
                        dialogFitGif.dismiss();
                        handler.sendEmptyMessage(1);
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
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) gifView.getLayoutParams();
            params.height = (DisplayUtil.screenWidth - DisplayUtil.dp2px(8)) / 3;
            params.width = params.height;
        }
    }
}
