package com.fly.run.activity.location_visible;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.utils.SharePreferceTool;
import com.fly.run.view.actionbar.CommonActionBar;

public class LocationVisibleActivity extends BaseUIActivity {

    private CommonActionBar actionBar;
    private LinearLayout layoutSwitch;
    private TextView tvSwitch;
    private Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_visible);
        tvSwitch = (TextView) findViewById(R.id.tv_switch);
        aSwitch = (Switch) findViewById(R.id.switch_state);
        layoutSwitch = (LinearLayout) findViewById(R.id.layout_switch);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharePreferceTool.getInstance().setCache("SwitchLocationVisible", isChecked);
            }
        });
        initActionBar();
        initData();
    }

    private void initActionBar() {
        actionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        actionBar.setActionTitle("位置可见");
        actionBar.setActionLeftIconListenr(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initData() {
        boolean visiable = SharePreferceTool.getInstance().getBoolean("SwitchLocationVisible", true);
        aSwitch.setChecked(visiable);
    }
}
