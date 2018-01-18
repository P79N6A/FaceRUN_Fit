package com.fly.run.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.fly.run.R;
import com.fly.run.bean.PhotoItem;
import com.fly.run.utils.ImageLoaderOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * @author tongqian.ni
 */
public class GalleryAdapter extends BaseAdapter {

    private Context mContext;
    private List<PhotoItem> values;
    public static GalleryHolder holder;

    /**
     * @param context
     * @param values
     */
    public GalleryAdapter(Context context, List<PhotoItem> values) {
        this.mContext = context;
        this.values = values;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public PhotoItem getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final GalleryHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.item_gallery, null);
            holder = new GalleryHolder();
            holder.sample = (ImageView) convertView.findViewById(R.id.gallery_sample_image);
            holder.check = (ImageView) convertView.findViewById(R.id.iv_check);
            convertView.setTag(holder);
        } else {
            holder = (GalleryHolder) convertView.getTag();
        }
        final PhotoItem gallery = (PhotoItem) getItem(position);
//        ImageLoaderUtils.displayLocalImage(gallery.getImageUri(), holder.sample,null);
        ImageLoader.getInstance().displayImage("file:///" + gallery.getImageUri(), holder.sample, ImageLoaderOptions.optionsLanuchHeader);
        if (gallery.isChecked()) {
            holder.check.setImageResource(R.drawable.ic_people_choose_press);
        } else {
            holder.check.setImageResource(R.drawable.ic_people_choose_nol);
        }
        return convertView;
    }

    class GalleryHolder {
        ImageView sample;
        ImageView check;
    }

}
