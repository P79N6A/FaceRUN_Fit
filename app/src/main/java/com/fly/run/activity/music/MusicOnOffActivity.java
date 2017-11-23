package com.fly.run.activity.music;

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

public class MusicOnOffActivity extends BaseUIActivity {

    private CommonActionBar actionBar;
    private LinearLayout layoutSwitch;
    private TextView tvSwitch;
    private Switch aSwitch;
    private String MusicClose = "关闭音乐";
    private String MusicOpen = "打开音乐";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_on_off);
        tvSwitch = (TextView) findViewById(R.id.tv_switch);
        aSwitch = (Switch) findViewById(R.id.switch_state);
        layoutSwitch = (LinearLayout) findViewById(R.id.layout_switch);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharePreferceTool.getInstance().setCache("SwitchMusic", isChecked);
                if (isChecked) {
                    tvSwitch.setText(MusicClose);
                    tvSwitch.setTextColor(getResources().getColor(R.color.color_333333));
                    layoutSwitch.setBackgroundColor(getResources().getColor(R.color.color_ffffff));
                } else {
                    tvSwitch.setText(MusicOpen);
                    tvSwitch.setTextColor(getResources().getColor(R.color.color_666666));
                    layoutSwitch.setBackgroundColor(getResources().getColor(R.color.bg_color));
                }
            }
        });
        initActionBar();
        initData();
    }

    private void initData() {
        boolean open = SharePreferceTool.getInstance().getBoolean("SwitchMusic", true);
        aSwitch.setChecked(open);
        if (open) {
            tvSwitch.setText(MusicClose);
            tvSwitch.setTextColor(getResources().getColor(R.color.color_333333));
            layoutSwitch.setBackgroundColor(getResources().getColor(R.color.color_ffffff));
        } else {
            tvSwitch.setText(MusicOpen);
            tvSwitch.setTextColor(getResources().getColor(R.color.color_666666));
            layoutSwitch.setBackgroundColor(getResources().getColor(R.color.bg_color));
        }
    }

    private void initActionBar() {
        actionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        actionBar.setActionTitle("音乐开关");
        actionBar.setActionLeftIconListenr(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
