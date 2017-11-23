package com.fly.run.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.utils.LocationUtils;
import com.fly.run.utils.Logger;
import com.fly.run.utils.NetWorkUtil;
import com.fly.run.utils.OkHttpClientManager;
import com.fly.run.utils.ToastUtil;
import com.fly.run.utils.WeatherUtil;
import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class WelRunActivity extends BaseUIActivity {

    private long startTime = 0;
    private final long JumpTime = 2000;
    public static boolean isJump = false;
    private Handler mHandler = new Handler();
    private LocationUtils locationUtils;
    private String strProvince, strCity;

//    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wel_run);
        startTime = System.currentTimeMillis();
//        loadWebGif();
        getRunData();
    }

    private void loadWebGif() {
//        webView = (WebView) findViewById(R.id.webview);
//        webView.setBackgroundColor(0x00000000);
//        String gifPath = "file:///android_asset/image_8.gif";
//        webView.loadUrl(gifPath);
        getRunData();
    }

    LocationUtils.LocationChangedListener locationChangedListener = new LocationUtils.LocationChangedListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            if (null != loc && loc.getErrorCode() == 0) {
                if (TextUtils.isEmpty(strProvince) || TextUtils.isEmpty(strCity)) {
                    strProvince = loc.getProvince();
                    strCity = loc.getCity();
                    if (strProvince.endsWith("省"))
                        strProvince = strProvince.substring(0, strProvince.length() - 1);
                    if (strCity.endsWith("市"))
                        strCity = strCity.substring(0, strCity.length() - 1);
                    httpWeatherTask();
                }
                Logger.e(TAG, "Province = " + strProvince + "  City = " + strCity);
            } else {
                Logger.e(TAG, "loc error " + strProvince + "  City = " + strCity);
                handler.sendEmptyMessage(2);
            }
            locationUtils.stopLocation();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationUtils.destroyLocation();
    }

    private void getRunData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                locationUtils = new LocationUtils();
                locationUtils.setListener(locationChangedListener);
                locationUtils.startLocation(WelRunActivity.this, false);
                Logger.e(TAG, "run over");
            }
        }).start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
//                    intentToActivity(MainActivity.class);
                    Intent intent = new Intent(WelRunActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_alpha_in, R.anim.anim_alpha_out);
                    finish();
                    break;
                case 2:
                    if (isJump)
                        return;
                    long time = System.currentTimeMillis() - startTime;
                    if (time < 2000)
                        this.sendEmptyMessageDelayed(1, 2000 - time);
                    else
                        this.sendEmptyMessage(1);
                    isJump = true;
                    break;
            }
        }
    };

    private void httpWeatherTask() {
        if (!NetWorkUtil.haveNetWork(this)) {
            ToastUtil.show("请检查网络");
            handler.sendEmptyMessage(2);
            return;
        }
        if (TextUtils.isEmpty(strProvince) || TextUtils.isEmpty(strCity)) {
            handler.sendEmptyMessage(2);
            return;
        }

        OkHttpClientManager.getInstance()._getAsyn(WeatherUtil.getWeatherUrl(strProvince, strCity), new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(2);
            }

            @Override
            public void onResponse(String response) {
                if (TextUtils.isEmpty(response)) {
                    handler.sendEmptyMessage(2);
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String msg = jsonObject.optString("msg");
                    String retCode = jsonObject.optString("retCode");
                    if (!TextUtils.isEmpty(retCode) && retCode.equals("200")) {
                        JSONArray jsonArray = jsonObject.optJSONArray("result");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            JSONObject jsonData = jsonArray.optJSONObject(0);
                            WeatherUtil.weatherData = jsonData;
                            WeatherUtil.weatherBean = WeatherUtil.parsonWeatherData(jsonData);
                            Logger.e(TAG, "AirCondition = " + WeatherUtil.weatherBean.getAirCondition() + "  Weather = " + WeatherUtil.weatherBean.getWeather() + "  UpdateTime = " + WeatherUtil.weatherBean.getUpdateTime());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendEmptyMessage(2);
                }
            }
        });
    }
}
