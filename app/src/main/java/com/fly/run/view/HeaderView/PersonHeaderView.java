package com.fly.run.view.HeaderView;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fly.run.R;
import com.fly.run.activity.photo.PhotoActivity;
import com.fly.run.bean.AccountBean;
import com.fly.run.utils.DisplayUtil;
import com.fly.run.utils.ImageLoaderOptions;
import com.fly.run.utils.Logger;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by kongwei on 2017/2/20.
 */

public class PersonHeaderView extends RelativeLayout {

    private View viewStatus;
    private int statusHeight = 0;

    private ImageView iv_header_icon;
    private TextView tv_user_name, tv_address;

    public PersonHeaderView(Context context) {
        super(context);
        initView();
    }

    public PersonHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PersonHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_persion_header, this);
        viewStatus = (View) findViewById(R.id.view_top_status);
        iv_header_icon = (ImageView) findViewById(R.id.iv_header_icon);
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        tv_address = (TextView) findViewById(R.id.tv_address);
        iv_header_icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PhotoActivity.class);
                getContext().startActivity(intent);
            }
        });
        setStatusHeight();
    }

    public void setData(AccountBean bean) {
        if (bean != null) {
            String headerUrl = bean.getHead_portrait();
            Logger.e("PersonHeaderView", "headerUrl = " + headerUrl);
            ImageLoader.getInstance().displayImage(headerUrl, iv_header_icon, ImageLoaderOptions.optionsUserCornerHeader);
            if (!TextUtils.isEmpty(bean.getUsername()))
                tv_user_name.setText(bean.getUsername());
        }
    }

    private void setStatusHeight() {
        if (statusHeight == 0)
            statusHeight = DisplayUtil.getStatusBarHeight(getContext());
        LayoutParams params = (LayoutParams) viewStatus.getLayoutParams();
        params.height = statusHeight;
        viewStatus.setLayoutParams(params);
    }
}
