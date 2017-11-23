package com.fly.run.utils;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Created by xinzhendi-031 on 2017/1/6.
 */
public class WallpaperUtil {

    public static Drawable getWallpagerDrawable(Context context) {
        // 获取壁纸管理器
        WallpaperManager wallpaperManager = WallpaperManager
                .getInstance(context);
        Drawable wallpaperDrawable = null;
        // 获取当前壁纸
        wallpaperDrawable = wallpaperManager.getDrawable();
//        // 将Drawable,转成Bitmap
//        Bitmap bm = ((BitmapDrawable) wallpaperDrawable).getBitmap();
        return wallpaperDrawable;
    }

    /**
     * 设置桌面壁纸
     */
    public static void setWallPaper(Activity activity, String imageFilesPath) {
        WallpaperManager mWallManager = WallpaperManager.getInstance(activity);
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilesPath);
            mWallManager.setBitmap(bitmap);
            Toast.makeText(activity, "壁纸设置成功", Toast.LENGTH_SHORT)
                    .show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置锁屏壁纸
     */
    public static void setLockWallPaper(Activity activity, String imageFilesPath) {
        // TODO Auto-generated method stub
        try {
            WallpaperManager mWallManager = WallpaperManager.getInstance(activity);
            Class class1 = mWallManager.getClass();//获取类名
            Method setWallPaperMethod = class1.getMethod("setBitmapToLockWallpaper", Bitmap.class);//获取设置锁屏壁纸的函数
            setWallPaperMethod.invoke(mWallManager, BitmapFactory.decodeFile(imageFilesPath));//调用锁屏壁纸的函数，并指定壁纸的路径imageFilesPath
            Toast.makeText(activity, "锁屏壁纸设置成功", Toast.LENGTH_SHORT).show();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
