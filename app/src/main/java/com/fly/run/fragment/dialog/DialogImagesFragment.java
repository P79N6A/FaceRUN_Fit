package com.fly.run.fragment.dialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.fly.run.R;
import com.fly.run.activity.media.ShowImagesActivity;
import com.fly.run.adapter.ImageTouchViewPagerAdapter;
import com.fly.run.bean.UrlDrawableBean;
import com.fly.run.bean.WeatherBean;
import com.fly.run.config.UrlConstants;
import com.fly.run.utils.ImageLoaderOptions;
import com.fly.run.utils.TimeFormatUtils;
import com.fly.run.utils.WeatherUtil;
import com.fly.run.view.ImageTouchView.ImageTouchViewLayout;
import com.fly.run.view.viewpager.CustomViewPager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xinzhendi-031 on 2017/1/25.
 */
public class DialogImagesFragment extends DialogFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private CustomViewPager mViewPager;
    private ImageTouchViewPagerAdapter adapter;

    float scale = 1.0f;
    private Handler handler = new Handler();

    private String mImages = "";
    private int mPosition = 0;
    private List<String> images = new ArrayList<>();
    private List<ImageTouchViewLayout> viewList = new ArrayList<>();
    private Map<Integer, Boolean> selectMap = new HashMap<>();
    private static Map<Integer, UrlDrawableBean> mUrlDrawableMap = new HashMap<>();


    public static DialogImagesFragment newInstance(String images, int position, Map<Integer, UrlDrawableBean> urlDrawableMap) {
        DialogImagesFragment fragment = new DialogImagesFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, images);
        args.putSerializable(ARG_PARAM2, position);

        mUrlDrawableMap.clear();
//        Intent intent = new Intent(context, ShowImagesActivity.class);
//        intent.putExtra("data", images);
//        intent.putExtra("position", position);
        if (urlDrawableMap != null)
            mUrlDrawableMap.putAll(urlDrawableMap);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        //设置动画
        params.windowAnimations = R.style.NearbyDialogFragment;
        window.setAttributes(params);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            title = getArguments().getString(ARG_PARAM1);
//            content = getArguments().getString(ARG_PARAM2);
        }
        mImages = getActivity().getIntent().getStringExtra("data");
        mPosition = getActivity().getIntent().getIntExtra("position", 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_fragment_images, container, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mViewPager = (CustomViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setScanScroll(true);
        adapter = new ImageTouchViewPagerAdapter();
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(pageChangeListener);
        loadData(mImages);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUrlDrawableMap.clear();
    }

    private void loadData(String urls) {
        if (TextUtils.isEmpty(urls))
            return;
        String[] list = urls.split(",");
        mViewPager.setOffscreenPageLimit(list.length);
        for (String url : list) {
            if (!TextUtils.isEmpty(url)) {
                url = url.trim();
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = String.format(UrlConstants.HTTP_DOWNLOAD_FILE_2, url);
                images.add(url);
                ImageTouchViewLayout imageTouchViewLayout = new ImageTouchViewLayout(getActivity());
                viewList.add(imageTouchViewLayout);
            }
        }
        adapter.setViewList(viewList);
        adapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(mPosition, false);
        try {
            loadImage(mPosition, images.get(mPosition), viewList.get(mPosition));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadImage(final int position, String url, final ImageTouchViewLayout imageTouchViewLayout) {
        Drawable drawable = null;
        try {
            drawable = mUrlDrawableMap.get(position).getDrawable();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DisplayImageOptions displayImageOptions = ImageLoaderOptions.getDisplayImageScaleOptions(drawable);
        ImageLoader.getInstance().displayImage(url, imageTouchViewLayout.getImageViewTouch(), ImageLoaderOptions.optionsLanuchHeader, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                setProgressView(imageTouchViewLayout, View.GONE);
            }


            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                if (bitmap == null)
                    return;
//                int w = bitmap.getWidth();
//                int h = bitmap.getHeight();
//                int ScreenWidth = DisplayUtil.screenWidth;
//                int ScreenHeight = DisplayUtil.screenHeight;
//                float scaleW = w * 1.0f / ScreenWidth;
//                float scaleH = h * 1.0f / ScreenHeight;
//                scale = Math.min(scaleW, scaleH);
//                final float finalScale = scale;
                selectMap.put(position, true);
                setProgressView(imageTouchViewLayout, View.GONE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageTouchViewLayout.getImageViewTouch().zoomTo(scale, 400);
                    }
                }, 200);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
                setProgressView(imageTouchViewLayout, View.GONE);
            }
        });
    }

    private void setProgressView(ImageTouchViewLayout imageTouchViewLayout, int visiable) {
        if (imageTouchViewLayout == null || imageTouchViewLayout.getProgressBar() == null)
            return;
        imageTouchViewLayout.getProgressBar().setVisibility(visiable);
        imageTouchViewLayout.getTvPersent().setVisibility(visiable);
    }

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (selectMap.containsKey(position) && selectMap.get(position))
                return;
            try {
                loadImage(position, images.get(position), viewList.get(position));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
