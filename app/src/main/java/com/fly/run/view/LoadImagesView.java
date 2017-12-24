package com.fly.run.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fly.run.R;
import com.fly.run.activity.ShowImagesActivity;
import com.fly.run.adapter.LoadImagesAdapter;
import com.fly.run.config.UrlConstants;
import com.fly.run.utils.DisplayUtil;
import com.fly.run.utils.ImageLoaderOptions;
import com.fly.run.view.gridview.CustomGridView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.Arrays;

/**
 * Created by xinzhendi-031 on 2017/12/8.
 */
public class LoadImagesView extends RelativeLayout {

    private ImageView imageView;
    private CustomGridView gridView;
    private LoadImagesAdapter adapter;
    private final int MAX_HEIGHT = 200;

    public LoadImagesView(Context context) {
        super(context);
        initView(context);
    }

    public LoadImagesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoadImagesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_load_images, this);
        imageView = (ImageView) findViewById(R.id.iv_single);
        gridView = (CustomGridView) findViewById(R.id.gridview);
        adapter = new LoadImagesAdapter(context);
        gridView.setAdapter(adapter);
    }

    public void setImagesData(String urls) {
        if (TextUtils.isEmpty(urls)) {
            imageView.setVisibility(View.GONE);
            gridView.setVisibility(View.GONE);
            return;
        }
        String[] list = urls.split(",");
        if (list.length == 1) {
            imageView.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
            String url = list[0];
            if (!TextUtils.isEmpty(url)) {
                url = url.trim();
                if (!url.startsWith("http://"))
                    url = String.format(UrlConstants.HTTP_DOWNLOAD_FILE_2, url);
            }
            final String finalUrl = url;
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowImagesActivity.startShowImageActivity(getContext(), finalUrl,0);
                }
            });
            ImageLoader.getInstance().displayImage(url, imageView, ImageLoaderOptions.optionsItemDefault, new ImageLoadingListener() {
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
                    int w = bitmap.getWidth();
                    int h = bitmap.getHeight();
                    if (w == 0 || h == 0)
                        return;
                    int maxHeight = DisplayUtil.dp2px(MAX_HEIGHT);
                    LayoutParams params = (LayoutParams) imageView.getLayoutParams();
                    int viewWidth = DisplayUtil.screenWidth - DisplayUtil.dp2px(8);
                    int viewHeight = viewWidth * h / w;
                    if (viewHeight > maxHeight) {
                        params.height = maxHeight;
                        params.width = params.height * w / h;
                    } else {
                        params.width = viewWidth;
                        params.height = viewHeight;
                    }
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
        } else if (list.length > 1) {
            imageView.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
            adapter.setData(Arrays.asList(list));
            adapter.notifyDataSetChanged();
        }
    }
}
