package com.fly.run.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by xinzhendi-031 on 2017/9/26.
 */
public class AnimUtil {

    public static void startAnimOpen(View view, float distance, long delay, long duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", 0f, distance); // 初始化动画，设置各个参数
        animator.setDuration(duration); // 设置动画持续时间
        animator.setStartDelay(delay);
        animator.start(); // 开始运行动画
    }

    public static void startAnimClose(View view, float distance, long delay, long duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", -distance, 0f); // 初始化动画，设置各个参数
        animator.setDuration(duration); // 设置动画持续时间
        animator.setStartDelay(delay);
        animator.start(); // 开始运行动画
    }

    public static void alphaAnimVisible(View view, long delay, long duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1.0f); // 初始化动画，设置各个参数
        animator.setDuration(duration); // 设置动画持续时间
        animator.setStartDelay(delay);
        animator.start(); // 开始运行动画
    }

    public static void alphaAnimInVisible(View view, long delay, long duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.0f); // 初始化动画，设置各个参数
        animator.setDuration(duration); // 设置动画持续时间
        animator.setStartDelay(delay);
        animator.start(); // 开始运行动画
    }

    public static void scaleAnim(View view, long delay, long duration) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1.1f, 1.0f); // 初始化动画，设置各个参数
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1.1f, 1.0f); // 初始化动画，设置各个参数
        AnimatorSet set = new AnimatorSet();
        //同时沿X,Y轴放大
        set.play(scaleX).with(scaleY);
        //都设置3s，也可以为每个单独设置
        set.setDuration(duration);// 设置动画持续时间
        set.setStartDelay(delay);// 设置动画延时播放
        set.start();// 开始运行动画
    }

    public static void circularProgressAnimZero(View view, float startProgress, float endProgress, long delay, long duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "progress", startProgress, endProgress); // 初始化动画，设置各个参数
        animator.setDuration(duration); // 设置动画持续时间
        animator.setStartDelay(delay);
        animator.start(); // 开始运行动画
    }

    public static void circularProgressAnim(View view, float startProgress, float endProgress, long delay, long duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "progress", startProgress, 0, endProgress); // 初始化动画，设置各个参数
        animator.setDuration(duration); // 设置动画持续时间
        animator.setStartDelay(delay);
        animator.start(); // 开始运行动画
    }


    public static void scaleZanAnim(View view, float begin, float end, long delay, long duration) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", begin, end); // 初始化动画，设置各个参数
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", begin, end); // 初始化动画，设置各个参数
        AnimatorSet set = new AnimatorSet();
        //同时沿X,Y轴放大
        set.play(scaleX).with(scaleY);
        //都设置3s，也可以为每个单独设置
        set.setDuration(duration);// 设置动画持续时间
        set.setStartDelay(delay);// 设置动画延时播放
        set.start();// 开始运行动画
    }
}
