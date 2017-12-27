package com.fly.run.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.fly.run.view.ImageTouchView.ImageTouchViewLayout;

import java.util.List;

/**
 * Created by xinzhendi-031 on 2017/11/9.
 */
public class ImageTouchViewPagerAdapter extends PagerAdapter {
    private List<ImageTouchViewLayout> viewList;// view数组

    public ImageTouchViewPagerAdapter setViewList(List<ImageTouchViewLayout> viewList) {
        this.viewList = viewList;
        return this;
    }

    @Override
    public int getCount() {
        return viewList != null ? viewList.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));
    }
}
