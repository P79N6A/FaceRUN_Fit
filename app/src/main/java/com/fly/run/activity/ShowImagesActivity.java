package com.fly.run.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.utils.ImageLoaderOptions;
import com.fly.run.view.ImageZoom.ImageViewTouch;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ShowImagesActivity extends BaseUIActivity {

    private ImageViewTouch imageViewTouch;
    float scale = 1.0f;
    private Handler handler = new Handler();

    public static void startShowImageActivity(Context context, String image) {
        Intent intent = new Intent(context, ShowImagesActivity.class);
        intent.putExtra("data", image);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.anim_alpha_in, R.anim.anim_alpha_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);
        String url = getIntent().getStringExtra("data");
        imageViewTouch = (ImageViewTouch) findViewById(R.id.iv_photo);
        imageViewTouch.setSingleTapListener(new ImageViewTouch.OnImageViewTouchSingleTapListener() {
            @Override
            public void onSingleTapConfirmed() {
                finish();
            }
        });
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
