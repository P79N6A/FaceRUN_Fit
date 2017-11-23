package com.fly.run.activity.photo;

import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;

import static com.fly.run.R.id.iv_photo;

/**
 * Created by kongwei on 2017/3/2.
 */

public class PhotoActivity extends BaseUIActivity {

    private ImageView blurImage; // 显示模糊的图片

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        initViw();
    }

    private void initViw() {
        blurImage = (ImageView) findViewById(iv_photo);
        TransitionDrawable transition = (TransitionDrawable) getResources().getDrawable(R.drawable.expand_collapse);
        blurImage.setImageDrawable(transition);
        transition.startTransition(2000);
        blurImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
