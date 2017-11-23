package com.fly.run.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
public class CircleAnimView extends RelativeLayout implements View.OnTouchListener {

    public static final int TYPE_RUN_PAUSE = 1;
    public static final int TYPE_RUN_KEEP = 2;
    public static final int TYPE_RUN_OVER = 3;
    private int TYPE_RUN_NOMAL = -1;

    private RelativeLayout layoutRingBg;
    private ImageView viewRing;
    private ImageView viewRingBg;
    private TextView tvR;
    private Handler handler;
    private Animation animBig2, animSmall2;
    private Animation animBig1, animSmall1;
    private Handler mHandler = new Handler();
    private boolean isEnable = true;
    private boolean isTouchCilck = false;

    public CircleAnimView(Context context) {
        super(context);
        init();
    }

    public CircleAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.view_circle_anim, this);
        layoutRingBg = (RelativeLayout) view.findViewById(R.id.layout_ring_bg);
        viewRingBg = (ImageView) view.findViewById(view_ring_bg);
        viewRing = (ImageView) view.findViewById(R.id.view_ring);
        tvR = (TextView) view.findViewById(R.id.tv_R);
        animBig2 = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale_big_2);
        animSmall2 = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale_small_2);
        animBig1 = AnimationUtils.loadAnimation(getContext(), R.anim.anim_video_scale_big);
        animSmall1 = AnimationUtils.loadAnimation(getContext(), R.anim.anim_video_scale_small);
        animSmall1.setAnimationListener(animSmall1Listener);
        viewRingBg.setOnTouchListener(this);
    }

    public CircleAnimView setHandler(Handler handler) {
        this.handler = handler;
        return this;
    }

    public class MyColorThread extends Thread {

        int startColor;
        int endColor;
        float progress1;
        ImageView layout;

        public MyColorThread(float progress1, ImageView layout) {
            this.progress1 = progress1;
            this.layout = layout;
        }

        public void setColors(int startColor, int endColor) {
            this.startColor = startColor;
            this.endColor = endColor;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                progress1 = i / 100f;
                try {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            layout.setColorFilter(evaluateColor(startColor, endColor, progress1));
                        }
                    });
                    Thread.sleep(6);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setTvRText(String s) {
        MyColorThread myColorThread = new MyColorThread(0, viewRingBg);
        if (TextUtils.isEmpty(s)) {
            tvR.setText("RUN");
            myColorThread.setColors(0xeeff0000, 0x884CAF50);
//            viewRingBg.setColorFilter(getContext().getResources().getColor(R.color.color_88353535));
        } else {
            tvR.setText(s);
            myColorThread.setColors(0x884CAF50, 0xeeff0000);
//            viewRingBg.setColorFilter(getContext().getResources().getColor(R.color.red));
        }
        myColorThread.start();
    }

    public void setTvRImg(int drawable) {
        tvR.setText("");
        Drawable nav_up = getResources().getDrawable(drawable);
        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
        tvR.setCompoundDrawables(null, nav_up, null, null);
    }

    public void setViewRingBgEnable(boolean enable) {
        viewRingBg.setEnabled(enable);
    }

    private void startAnimDown() {
        Logger.e("CircleAnimView", "down");
        viewRingBg.clearAnimation();
        viewRingBg.startAnimation(animBig2);
    }

    private void startAnimUp(boolean flag) {
        Logger.e("CircleAnimView", "up");
        isTouchCilck = flag;
        viewRing.clearAnimation();
        viewRing.startAnimation(animSmall1);
    }

    public int getTYPE_RUN_NOMAL() {
        return TYPE_RUN_NOMAL;
    }

    public CircleAnimView setTYPE_RUN_NOMAL(int TYPE_RUN_NOMAL) {
        this.TYPE_RUN_NOMAL = TYPE_RUN_NOMAL;
        return this;
    }

    public void setViewEnTouch(boolean enable) {
        this.isEnable = enable;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!isEnable)
            return false;
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startAnimDown();
                return true;
            case MotionEvent.ACTION_UP:
                int vLeft = v.getLeft();
                int vTop = v.getTop();
                int vRight = v.getRight();
                int vBottom = v.getBottom();
                float x = event.getX();
                float y = event.getY();
                if (x >= vLeft && x <= vRight && y >= vTop && y <= vBottom)
                    startAnimUp(true);
                else
                    startAnimUp(false);
                return true;
        }
        return false;
    }

    private Animation.AnimationListener animSmall1Listener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        viewRingBg.clearAnimation();
                        viewRingBg.startAnimation(animSmall2);
                        viewRing.clearAnimation();
                        viewRing.startAnimation(animBig1);
                        if (isTouchCilck) {
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (handler != null) {
                                        handler.sendEmptyMessage(1);
                                    } else if (clickListener != null) {
//                                        TYPE_RUN_NOMAL = TYPE_RUN_PAUSE;
                                        if (TYPE_RUN_NOMAL == -1)
                                            TYPE_RUN_NOMAL = TYPE_RUN_KEEP;
                                        else if (TYPE_RUN_NOMAL == TYPE_RUN_KEEP)
                                            TYPE_RUN_NOMAL = TYPE_RUN_OVER;
                                        else if (TYPE_RUN_NOMAL == TYPE_RUN_PAUSE)
                                            TYPE_RUN_NOMAL = TYPE_RUN_KEEP;
                                        clickListener.doAction(TYPE_RUN_NOMAL);
                                    }
                                }
                            }, 200);

                        }
                    } finally {
                        isTouchCilck = false;
                    }
                }
            }, 50);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private ClickListener clickListener;

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        public void doAction(int action);
    }
}
