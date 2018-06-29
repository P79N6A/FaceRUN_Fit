package com.fly.run.view.toast;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fly.run.R;
import com.fly.run.utils.DisplayUtil;


/**
 * Created by xinzhendi-031 on 2017/11/29.
 */
public class CustomToast {

    private static TextView mTextView;
    private static ImageView mImageView;

    public static void showRefreshToast(Context context, boolean refresh) {
        //加载Toast布局
        View toastRoot = LayoutInflater.from(context).inflate(R.layout.view_toast_refresh, null);
        //初始化布局控件
        ImageView mImageView = (ImageView) toastRoot.findViewById(R.id.iv_clear_cache);
        if (refresh)
            mImageView.setImageResource(R.drawable.icon_toast_ok);
        else
            mImageView.setImageResource(R.drawable.icon_toast_fail);
        //Toast的初始化
        Toast toastStart = new Toast(context);
        //获取屏幕高度
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        int height = wm.getDefaultDisplay().getHeight();
        int height = DisplayUtil.screenHeight;
        int width = DisplayUtil.screenWidth;
//        toastRoot.setMinimumWidth(width * 2 / 3);
        //Toast的Y坐标是屏幕高度的1/3，不会出现不适配的问题
        toastStart.setGravity(Gravity.TOP, 0, height / 3);
        toastStart.setDuration(Toast.LENGTH_SHORT);
        toastStart.setView(toastRoot);
        toastStart.show();
    }

    public static void showRefreshToast(Context context, int src) {
        //加载Toast布局
        View toastRoot = LayoutInflater.from(context).inflate(R.layout.view_toast_refresh, null);
        //初始化布局控件
        ImageView mImageView = (ImageView) toastRoot.findViewById(R.id.iv_clear_cache);
        try {
            mImageView.setImageResource(src);
        }catch (Exception e){
            e.printStackTrace();
            mImageView.setImageResource(R.drawable.icon_toast_fail);
        }
        //Toast的初始化
        Toast toastStart = new Toast(context);
        //获取屏幕高度
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        int height = wm.getDefaultDisplay().getHeight();
        int height = DisplayUtil.screenHeight;
        int width = DisplayUtil.screenWidth;
//        toastRoot.setMinimumWidth(width * 2 / 3);
        //Toast的Y坐标是屏幕高度的1/3，不会出现不适配的问题
        toastStart.setGravity(Gravity.TOP, 0, height / 3);
        toastStart.setDuration(Toast.LENGTH_SHORT);
        toastStart.setView(toastRoot);
        toastStart.show();
    }
}
