package com.fly.run.activity.wakeup;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.adapter.SettingAdapter;
import com.fly.run.fragment.dialog.DialogAlertFragment;
import com.fly.run.view.actionbar.CommonActionBar;

import java.util.Arrays;

public class WakeUpAlertActivity extends BaseUIActivity implements View.OnClickListener {

    private CommonActionBar actionBar;
    private ListView listView;
    private SettingAdapter adapter;
    private String[] array_wake_up;
    private String[] array_wake_up_alert;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wake_up_alert);
        listView = (ListView) findViewById(R.id.listview);
        adapter = new SettingAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showAlert(position);
            }
        });
        initActionBar();
        loadData();
    }

    private void initActionBar() {
        actionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        actionBar.setActionTitle("唤醒提示");
        actionBar.setActionLeftIconListenr(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadData() {
        if (array_wake_up == null)
            array_wake_up = this.getResources().getStringArray(R.array.array_wake_up);
        if (array_wake_up_alert == null)
            array_wake_up_alert = this.getResources().getStringArray(R.array.array_wake_up_alert);
        adapter.setData(Arrays.asList(array_wake_up));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    DialogAlertFragment nearbyFragment;

    private void showAlert(final int position) {
        handler.postDelayed(new Runnable() {
            @SuppressLint("NewApi")
            @Override
            public void run() {
                nearbyFragment = DialogAlertFragment.newInstance(array_wake_up[position], array_wake_up_alert[position]);
                if (!nearbyFragment.isVisible() && !WakeUpAlertActivity.this.isDestroyed())
                    nearbyFragment.show(getSupportFragmentManager(), "DialogNearbyFragment");
            }
        }, 50);
    }
}
