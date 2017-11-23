package com.fly.run.activity.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.adapter.SportModeAdapter;
import com.fly.run.utils.SharePreferceTool;
import com.fly.run.view.actionbar.CommonActionBar;

import java.util.Arrays;

public class SportModeActivity extends BaseUIActivity {

    /**
     * 跑步：0
     * 骑行：1
     * 快走：2
     */
    public static String[] SportMode = {"跑步", "骑行", "快走"};
    public static Integer[] LimitingSpeed = {8, 11, 4};
    private CommonActionBar actionBar;
    private ListView listView;
    private SportModeAdapter adapter;
    private Handler handler = new Handler();
    private int mIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_mode);
        initActionBar();
        listView = (ListView) findViewById(R.id.listview);
        adapter = new SportModeAdapter(this, setmIndex());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setmIndex(position);
                adapter.notifyDataSetChanged();
                SharePreferceTool.getInstance().setCache("SportModel", position);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent result = new Intent();
                        setResult(Activity.RESULT_OK, result);
                        finish();
                    }
                }, 200);
            }
        });
        loadData();
    }


    private void initActionBar() {
        actionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        actionBar.setActionTitle(getString(R.string.action_sport_mode));
        actionBar.setActionLeftIconListenr(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private int setmIndex() {
        mIndex = SharePreferceTool.getInstance().getInt("SportModel", 0);
        return mIndex;
    }

    private void loadData() {
        adapter.setData(Arrays.asList(SportMode));
        adapter.notifyDataSetChanged();
    }
}
