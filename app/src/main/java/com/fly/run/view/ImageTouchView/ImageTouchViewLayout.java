package com.fly.run.view.ImageTouchView;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fly.run.R;
import com.fly.run.view.ImageZoom.ImageViewTouch;

/**
 * Created by kongwei on 2017/2/20.
 */

public class ImageTouchViewLayout extends RelativeLayout {

    private ImageViewTouch imageViewTouch;
    private ProgressBar progressBar;
    private TextView tvPersent;

    public ImageTouchViewLayout(Context context) {
        super(context);
        initView();
    }

    public ImageTouchViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ImageTouchViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_image_touch, this);
        imageViewTouch = (ImageViewTouch)findViewById(R.id.imageViewTouch);
        imageViewTouch.setSingleTapListener(new ImageViewTouch.OnImageViewTouchSingleTapListener() {
            @Override
            public void onSingleTapConfirmed() {
                ((Activity)getContext()).finish();
            }
        });
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        tvPersent = (TextView)findViewById(R.id.tv_persent);
    }

    public ImageViewTouch getImageViewTouch() {
        return imageViewTouch;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public TextView getTvPersent() {
        return tvPersent;
    }
}
