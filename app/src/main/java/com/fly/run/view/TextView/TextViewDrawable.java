package com.fly.run.view.TextView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

/**
 * Created by xinzhendi-031 on 2017/12/4.
 */
public class TextViewDrawable {

    public static void setTextViewDrawableLeft(TextView textView, Context context,int src) {
        Drawable drawable = context.getResources().getDrawable(src);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(drawable, null, null, null);
    }
}
