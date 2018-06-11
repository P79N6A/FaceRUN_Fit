package com.fly.run.view.HeaderView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.fly.run.R;

/**
 * Created by kongwei on 2017/2/20.
 */

public class PersonNavigationView extends LinearLayout implements View.OnClickListener {

    private LinearLayout layoutShouCang, layoutGuanzhu, layoutChengjiu;

    public PersonNavigationView(Context context) {
        super(context);
        initView();
    }

    public PersonNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PersonNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_person_navigation, this);
        layoutShouCang = (LinearLayout) findViewById(R.id.layout_shoucang);
        layoutGuanzhu = (LinearLayout) findViewById(R.id.layout_guanzhu);
        layoutChengjiu = (LinearLayout) findViewById(R.id.layout_chengjiu);
        layoutShouCang.setOnClickListener(this);
        layoutGuanzhu.setOnClickListener(this);
        layoutChengjiu.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_shoucang:
                break;
            case R.id.layout_guanzhu:
                break;
            case R.id.layout_chengjiu:
                break;
        }
    }
}
