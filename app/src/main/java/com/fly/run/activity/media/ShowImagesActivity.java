package com.fly.run.activity.media;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.adapter.ImageTouchViewPagerAdapter;
import com.fly.run.bean.UrlDrawableBean;
import com.fly.run.config.UrlConstants;
import com.fly.run.utils.ImageLoaderOptions;
import com.fly.run.view.ImageTouchView.ImageTouchViewLayout;
import com.fly.run.view.viewpager.CustomViewPager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowImagesActivity extends BaseUIActivity {

    private CustomViewPager mViewPager;
    private ImageTouchViewPagerAdapter adapter;
    //    private ImageViewTouch imageViewTouch;
    float scale = 1.0f;
    private Handler handler = new Handler();

    private String mImages = "";
    private int mPosition = 0;
    private List<String> images = new ArrayList<>();
    private List<ImageTouchViewLayout> viewList = new ArrayList<>();
    private Map<Integer, Boolean> selectMap = new HashMap<>();
    private static Map<Integer, UrlDrawableBean> mUrlDrawableMap = new HashMap<>();

    public static void startShowImageActivity(Context context, String images, int position) {
        mUrlDrawableMap.clear();
        Intent intent = new Intent(context, ShowImagesActivity.class);
        intent.putExtra("data", images);
        intent.putExtra("position", position);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.anim_alpha_in, R.anim.anim_alpha_out);
    }

    public static void startShowImageActivity(Context context, String images, int position, Map<Integer, UrlDrawableBean> urlDrawableMap) {
        mUrlDrawableMap.clear();
        Intent intent = new Intent(context, ShowImagesActivity.class);
        intent.putExtra("data", images);
        intent.putExtra("position", position);
        if (urlDrawableMap == null)
            urlDrawableMap = new HashMap<>();
        mUrlDrawableMap.putAll(urlDrawableMap);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.anim_alpha_in, R.anim.anim_alpha_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);
        mImages = getIntent().getStringExtra("data");
        mPosition = getIntent().getIntExtra("position", 0);
        mViewPager = (CustomViewPager) findViewById(R.id.viewpager);
        mViewPager.setScanScroll(true);
        adapter = new ImageTouchViewPagerAdapter();
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(pageChangeListener);
        loadData(mImages);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUrlDrawableMap.clear();
    }

    private void loadData(String urls) {
        if (TextUtils.isEmpty(urls))
            return;
        String[] list = urls.split(",");
        mViewPager.setOffscreenPageLimit(list.length);
        for (String url : list) {
            if (!TextUtils.isEmpty(url)) {
                url = url.trim();
                if (!url.startsWith("http://"))
                    url = String.format(UrlConstants.HTTP_DOWNLOAD_FILE_2, url);
                images.add(url);
                ImageTouchViewLayout imageTouchViewLayout = new ImageTouchViewLayout(this);
                viewList.add(imageTouchViewLayout);
            }
        }
        adapter.setViewList(viewList);
        adapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(mPosition, false);
        try {
            loadImage(mPosition, images.get(mPosition), viewList.get(mPosition));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadImage(final int position, String url, final ImageTouchViewLayout imageTouchViewLayout) {
        Drawable drawable = null;
        try {
            drawable = mUrlDrawableMap.get(position).getDrawable();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DisplayImageOptions displayImageOptions = ImageLoaderOptions.getDisplayImageScaleOptions(drawable);
        ImageLoader.getInstance().displayImage(url, imageTouchViewLayout.getImageViewTouch(), ImageLoaderOptions.optionsLanuchHeader, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                setProgressView(imageTouchViewLayout, View.GONE);
            }


            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                if (bitmap == null)
                    return;
//                int w = bitmap.getWidth();
//                int h = bitmap.getHeight();
//                int ScreenWidth = DisplayUtil.screenWidth;
//                int ScreenHeight = DisplayUtil.screenHeight;
//                float scaleW = w * 1.0f / ScreenWidth;
//                float scaleH = h * 1.0f / ScreenHeight;
//                scale = Math.min(scaleW, scaleH);
//                final float finalScale = scale;
                selectMap.put(position, true);
                setProgressView(imageTouchViewLayout, View.GONE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageTouchViewLayout.getImageViewTouch().zoomTo(scale, 400);
                    }
                }, 200);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
                setProgressView(imageTouchViewLayout, View.GONE);
            }
        });
    }

    private void setProgressView(ImageTouchViewLayout imageTouchViewLayout, int visiable) {
        if (imageTouchViewLayout == null || imageTouchViewLayout.getProgressBar() == null)
            return;
        imageTouchViewLayout.getProgressBar().setVisibility(visiable);
        imageTouchViewLayout.getTvPersent().setVisibility(visiable);
    }

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (selectMap.containsKey(position) && selectMap.get(position))
                return;
            try {
                loadImage(position, images.get(position), viewList.get(position));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
