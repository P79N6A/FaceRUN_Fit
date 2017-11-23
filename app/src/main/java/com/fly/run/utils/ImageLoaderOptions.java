/*
 * Copyright 2014-2024 setNone. All rights reserved. 
 */
package com.fly.run.utils;

import android.graphics.Bitmap;
import com.fly.run.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * ImageLoaderOptions.java - ImageLoader config parameters
 *
 * @author Kevin.Zhang
 *         <p/>
 *         2014-2-27 下午5:17:25
 */
public class ImageLoaderOptions {

    public static final int MAX_IMAGE_WIDTH = 480; // The max pixels of image width
    public static final int MAX_IMAGE_HEIGHT = 800; // The max pixels of image height
    public static final int MAX_IMAGE_MEMORY_CACHE_SIZE = 2 * 1024 * 1024; // 2MB memory cache of images
    public static final int MAX_IMAGE_DISK_CACHE_SIZE = 200 * 1024 * 1024; // 200MB SD card cache of images
    public static final int MAX_IMAGE_DISK_FILE_COUNT = 400; // SD card cache for 400 max images

    /**
     * 图片圆角像素
     */
    private static final int _CORNER_RADIUS_100 = 100;
    private static final int _CORNER_RADIUS_10 = 10;

    /**
     * 用户头像和群组头像
     */
    public static DisplayImageOptions optionsUserCornerHeader = getDisplayImageOptions(R.drawable.ic_user_header_default,
            DisplayUtil.dp2px(_CORNER_RADIUS_100));
    public static DisplayImageOptions optionsUserHeader = getDisplayImageScaleOptions(R.drawable.ic_user_header_default);

    public static DisplayImageOptions getOptions(int drawable, int corner) {
        return getDisplayImageOptions(drawable, DisplayUtil.dp2px(corner));
    }

    public static DisplayImageOptions getOptions(int corner) {
        return getDisplayOptions(DisplayUtil.dp2px(corner));
    }

    /**
     * Obtain the ImageLoader display parameters
     *
     * @param imageResId         The resource ID of image
     * @param cornerRadiusPixels The rounded corners of image
     * @return DisplayImageOptions
     */
    public static DisplayImageOptions getDisplayImageOptions(int imageResId, int... cornerRadiusPixels) {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.showImageOnLoading(imageResId);
        builder.showImageForEmptyUri(imageResId);
        builder.cacheInMemory(true);
        builder.cacheOnDisk(true);
        builder.bitmapConfig(Bitmap.Config.RGB_565);
        builder.imageScaleType(ImageScaleType.EXACTLY);
        if (null != cornerRadiusPixels && cornerRadiusPixels.length > 0) {
            builder.displayer(new RoundedBitmapDisplayer(cornerRadiusPixels[0]));
        }
        builder.resetViewBeforeLoading(false);
        return builder.build();
    }

    public static DisplayImageOptions getDisplayOptions(int... cornerRadiusPixels) {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.cacheInMemory(true);
        builder.cacheOnDisk(true);
        builder.bitmapConfig(Bitmap.Config.RGB_565);
        builder.imageScaleType(ImageScaleType.EXACTLY);
        if (null != cornerRadiusPixels && cornerRadiusPixels.length > 0) {
            builder.displayer(new RoundedBitmapDisplayer(cornerRadiusPixels[0]));
        }
        builder.resetViewBeforeLoading(false);
        return builder.build();
    }

    public static DisplayImageOptions getDisplayImageScaleOptions(int imageResId) {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.showImageOnLoading(imageResId);
        builder.showImageForEmptyUri(imageResId);
        builder.cacheInMemory(true);
        builder.cacheOnDisk(true);
        builder.bitmapConfig(Bitmap.Config.RGB_565);
        builder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
        builder.resetViewBeforeLoading(false);
        return builder.build();
    }

}
