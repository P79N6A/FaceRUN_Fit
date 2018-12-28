package com.fly.run.view.share;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fly.run.R;
import com.fly.run.utils.Logger;

import static com.fly.run.R.id.view_ring_bg;
import static com.fly.run.utils.ColorUtils.evaluateColor;

/**
 * Created by xinzhendi-031 on 2016/10/26.
 */
public class ShareAnimView extends RelativeLayout{

    private RelativeLayout layout_1,layout_2,layout_3,layout_4,layout_5;
    private ImageView iv_1,iv_2,iv_3,iv_4,iv_5;
    private ImageView iv_s1,iv_s2,iv_s3,iv_s4,iv_s5;

    public ShareAnimView(Context context) {
        super(context);
        init();
    }

    public ShareAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.view_share_anim, this);
        iv_s1 = (ImageView)view.findViewById(R.id.iv_s1);
        iv_s2 = (ImageView)view.findViewById(R.id.iv_s2);
        iv_s3 = (ImageView)view.findViewById(R.id.iv_s3);
        iv_s4 = (ImageView)view.findViewById(R.id.iv_s4);
        iv_s5 = (ImageView)view.findViewById(R.id.iv_s5);

        iv_1 = (ImageView)view.findViewById(R.id.iv_1);
        iv_2 = (ImageView)view.findViewById(R.id.iv_2);
        iv_3 = (ImageView)view.findViewById(R.id.iv_3);
        iv_4 = (ImageView)view.findViewById(R.id.iv_4);
        iv_5 = (ImageView)view.findViewById(R.id.iv_5);

        iv_1.setColorFilter(getResources().getColor(R.color.color_theme_weixin));
        iv_2.setColorFilter(getResources().getColor(R.color.color_theme_circle));
        iv_3.setColorFilter(getResources().getColor(R.color.color_theme_sina));
        iv_4.setColorFilter(getResources().getColor(R.color.color_theme_twitter));
        iv_5.setColorFilter(getResources().getColor(R.color.color_theme_qq));

        iv_s1.setColorFilter(getResources().getColor(R.color.color_ffffff));
        iv_s2.setColorFilter(getResources().getColor(R.color.color_ffffff));
        iv_s3.setColorFilter(getResources().getColor(R.color.color_ffffff));
        iv_s4.setColorFilter(getResources().getColor(R.color.color_ffffff));
        iv_s5.setColorFilter(getResources().getColor(R.color.color_ffffff));
    }

    private ClickListener clickListener;

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        public void doAction(int action);
    }
}
