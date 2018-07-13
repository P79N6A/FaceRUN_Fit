package com.fly.run.adapter.circle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fly.run.R;
import com.fly.run.bean.FocusRecyclerBean;
import com.fly.run.utils.ImageLoaderOptions;
import com.fly.run.view.ImageView.RoundAngleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinzhendi-031 on 2018/4/13.
 */

public class FocusRecyclerAdapter extends RecyclerView.Adapter<FocusRecyclerAdapter.ViewHolder> {

    private Context context;

    private List<FocusRecyclerBean> datas = new ArrayList<>();

    public FocusRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setDatas(List<FocusRecyclerBean> list) {
        this.datas.clear();
        if (list == null)
            return;
        this.datas = list;
    }

    @Override
    public FocusRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_focus_recycler, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FocusRecyclerAdapter.ViewHolder holder, int position) {
        FocusRecyclerBean bean = datas.get(position);
        holder.itemName.setText(bean.getName());
        ImageLoader.getInstance().displayImage(bean.getHeaderUrl(), holder.itemImage, ImageLoaderOptions.optionsLanuchHeader);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RoundAngleImageView itemImage;
        TextView itemName;

        public ViewHolder(View view) {
            super(view);
            itemImage = (RoundAngleImageView) view.findViewById(R.id.imageview);
            itemName = (TextView) view.findViewById(R.id.textview);
        }
    }
}
