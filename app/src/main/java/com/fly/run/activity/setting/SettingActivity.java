package com.fly.run.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.activity.location_visible.LocationVisibleActivity;
import com.fly.run.activity.music.MusicOnOffActivity;
import com.fly.run.activity.offlinemap.OfflineMapActivity;
import com.fly.run.adapter.SettingAdapter;
import com.fly.run.bean.ResultTaskBean;
import com.fly.run.config.Constant;
import com.fly.run.httptask.HttpTaskUtil;
import com.fly.run.manager.UserInfoManager;
import com.fly.run.utils.AndroidOSInfoManager;
import com.fly.run.utils.ToastUtil;
import com.fly.run.view.actionbar.CommonActionBar;
import com.fly.run.view.dialog.DialogInformation;
import com.squareup.okhttp.Request;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class SettingActivity extends BaseUIActivity implements View.OnClickListener {

    private CommonActionBar actionBar;
    private ListView listView;
    private SettingAdapter adapter;
    private TextView tv_logout;

    //    private String[] SportModels_2 = {"运动模式", "离线地图", "耗电保护", "版本说明", "意见反馈", "清理缓存"};
    private String[] SportModels_1 = {"位置可见", "音乐开关", "运动模式", "离线地图", "版本说明", "意见反馈", "清理缓存"};
    private String[] SportModels;
    private Class[] IntentClassList = {LocationVisibleActivity.class, MusicOnOffActivity.class, SportModeActivity.class, OfflineMapActivity.class, null, null, null};
    private String MobileType = "";
    private HttpTaskUtil httpTaskUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        MobileType = AndroidOSInfoManager.AOsManufacturer();
//        SportModels = "huawei".toUpperCase().equals(MobileType) ? SportModels_2 : SportModels_1;
        SportModels = SportModels_1;
        listView = (ListView) findViewById(R.id.listview);
        adapter = new SettingAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*if ("huawei".toUpperCase().equals(MobileType) && "耗电保护".equals(SportModels[position])) {
                    //华为 受保护的应用
                    Intent huaweiIntent = new Intent();
                    huaweiIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    huaweiIntent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
                    startActivity(huaweiIntent);
                    return;
                }*/
                if (SportModels[position].equals("版本说明")) {
                    ToastUtil.show(AndroidOSInfoManager.getAppVersion(SettingActivity.this));
                    return;
                }
                if (IntentClassList[position] == null)
                    return;
                intentToActivity(IntentClassList[position]);
            }
        });
        tv_logout = (TextView) findViewById(R.id.tv_logout);
        final String account = UserInfoManager.getInstance().getAccount();
        if (TextUtils.isEmpty(account)) {
            tv_logout.setVisibility(View.GONE);
        }
        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInformation information = new DialogInformation(SettingActivity.this);
                information.setData("退出当前帐号");
                information.setOnEventListener(onEventListener);
                information.show();
            }
        });
        initActionBar();
        loadData();
    }

    private void initActionBar() {
        actionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        actionBar.setActionTitle("设置");
        actionBar.setActionLeftIconListenr(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadData() {
        adapter.setData(Arrays.asList(SportModels));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    HttpTaskUtil.ResultListener resultListener = new HttpTaskUtil.ResultListener() {
        @Override
        public void onResponse(String response) {
            try {
                ResultTaskBean bean = JSON.parseObject(response, ResultTaskBean.class);
                if (bean != null && bean.flag) {
                    String path = Constant.UserConfigPath + "Account.info";
                    File userFile = new File(path);
                    if (userFile != null && userFile.exists()) {
                        boolean del = userFile.delete();
                        if (del) {
                            UserInfoManager.getInstance().logout();
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                } else {
                    ToastUtil.show(TextUtils.isEmpty(response) ? "退出失败" : response);
                }
            } catch (Exception e) {
                ToastUtil.show(TextUtils.isEmpty(response) ? "退出失败" : response);
            }
        }

        @Override
        public void onFailure(Request request, IOException e) {

        }
    };

    DialogInformation.OnEventListener onEventListener = new DialogInformation.OnEventListener() {
        @Override
        public void result(boolean sure) {
            if (sure) {
                if (httpTaskUtil == null) {
                    httpTaskUtil = new HttpTaskUtil();
                    httpTaskUtil.setResultListener(resultListener);
                }
                httpTaskUtil.LoginOutTask(UserInfoManager.getInstance().getAccount());
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 2008:
                    break;
            }
        }
    }
}
