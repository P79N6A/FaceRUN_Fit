package com.fly.run.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.adapter.BaseTitleViewPagerAdapter;
import com.fly.run.config.UrlConstants;
import com.fly.run.utils.ImageLoaderOptions;
import com.fly.run.view.ImageZoom.ImageViewTouch;
import com.fly.run.view.viewpager.CustomViewPager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

public class ShowImagesActivity extends BaseUIActivity {

    private CustomViewPager mViewPager;
    private BaseTitleViewPagerAdapter adapter;
    private ImageViewTouch imageViewTouch;
    float scale = 1.0f;
    private Handler handler = new Handler();

    private String mImages = "";
    private int mPosition = 0;

    private List<View> viewList = new ArrayList<>();

    public static void startShowImageActivity(Context context, String images, int position) {
        Intent intent = new Intent(context, ShowImagesActivity.class);
        intent.putExtra("data", images);
        intent.putExtra("position", position);
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
        adapter = new BaseTitleViewPagerAdapter();
        mViewPager.setAdapter(adapter);
        loadData(mImages);
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
                ImageViewTouch imageViewTouch = new ImageViewTouch(this);
                imageViewTouch.setSingleTapListener(new ImageViewTouch.OnImageViewTouchSingleTapListener() {
                    @Override
                    public void onSingleTapConfirmed() {
                        finish();
                    }
                });
                loadImage(url, imageViewTouch);
                viewList.add(imageViewTouch);
            }
        }
        adapter.setViewList(viewList);
        adapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(mPosition, false);
    }

    private void loadImage(String url, final ImageViewTouch imageViewTouch) {
        ImageLoader.getInstance().displayImage(url, imageViewTouch, ImageLoaderOptions.optionsLanuchHeader, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

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
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageViewTouch.zoomTo(scale, 400);
                    }
                }, 200);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
    }
}
