package com.fly.run.activity.circle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.fly.run.R;
import com.fly.run.activity.ChooseImages.ChooseImagesActivity;
import com.fly.run.activity.WelRunActivity;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.adapter.PublishCircleGridAdapter;
import com.fly.run.bean.AccountBean;
import com.fly.run.bean.CircleBean;
import com.fly.run.bean.FileUploadImageBean;
import com.fly.run.bean.ResultTaskBean;
import com.fly.run.config.AppConstants;
import com.fly.run.httptask.HttpTaskUtil;
import com.fly.run.manager.UserInfoManager;
import com.fly.run.utils.LocationUtils;
import com.fly.run.utils.Logger;
import com.fly.run.utils.OkHttpClientManager;
import com.fly.run.utils.ToastUtil;
import com.fly.run.view.actionbar.CommonActionBar;
import com.fly.run.view.dialog.DialogChooseMedia;
import com.squareup.okhttp.Request;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CirclePublishActivity extends BaseUIActivity implements View.OnClickListener {

    public static final int ACTION_PUSH_CIRCLE = 2001;
    private CommonActionBar actionBar;
    private EditText etInput;
    private TextView tvAddress;
    private GridView gridView;
    private PublishCircleGridAdapter adapter;
    private HttpTaskUtil httpTaskUtil;
    private List<String> urlImages = new ArrayList<>();

    private LocationUtils locationUtils;
    private String strProvince = "四川", strCity = "成都";
    private String strAddress,strPoiName;

    public static void startActivityForResultRefresh(Activity context, ArrayList<String> list) {
        Intent intent = new Intent(context, CirclePublishActivity.class);
        intent.putExtra("images", list);
        context.startActivityForResult(intent, ACTION_PUSH_CIRCLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_circle);
        initActionBar();
        initData();
        initView();
        getLocaltionData();
    }

    private void initActionBar() {
        actionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        actionBar.setActionLeftIconListenr(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        actionBar.setActionRightIconListenr(R.drawable.ic_jobs_commen_press, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishTaskData();
            }
        });
    }

    private void initData() {
        List<String> list = getIntent().getStringArrayListExtra("images");
        if (list != null)
            urlImages.addAll(list);
    }

    private void initView() {
        etInput = (EditText) findViewById(R.id.et_input);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        gridView = (GridView) findViewById(R.id.gridview);
        adapter = new PublishCircleGridAdapter(this);
        gridView.setAdapter(adapter);
        adapter.setImageListener(new PublishCircleGridAdapter.ImageListenrt() {
            @Override
            public void doShowImage(int position, String url) {
                Logger.i(TAG, "position = " + position + "  url = " + url);
            }

            @Override
            public void doAddImage() {
                DialogChooseMedia dialog = new DialogChooseMedia(CirclePublishActivity.this);
                dialog.setOnEventListener(onEventListener);
                dialog.show();
//                Intent intent = new Intent(CirclePublishActivity.this, ChooseImagesActivity.class);
//                intent.putExtra("num", urlImages.size());
//                startActivityForResult(intent, REQUEST_ALBUM);
            }
        });
        showGridImgsView(urlImages);
    }

    private void showGridImgsView(List<String> images) {
        adapter.setUrlImages(images);
        adapter.notifyDataSetChanged();
    }

    DialogChooseMedia.OnEventListener onEventListener = new DialogChooseMedia.OnEventListener() {

        @Override
        public void result(int index) {
            if (index == 1) {
                takeCarema();
            } else if (index == 2) {
                Intent intent = new Intent(CirclePublishActivity.this, ChooseImagesActivity.class);
                intent.putExtra("num", urlImages.size());
                startActivityForResult(intent, REQUEST_ALBUM);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    private void publishTaskData() {
        if (httpTaskUtil == null) {
            httpTaskUtil = new HttpTaskUtil();
            httpTaskUtil.setResultListener(resultListener);
        }
        AccountBean bean = UserInfoManager.getInstance().getAccountInfo();
        if (bean == null || bean.getId() <= 0) {
            ToastUtil.show("请登录");
            return;
        }
        if (urlImages == null || urlImages.size() == 0) {
            insertCircleData("", "");
            return;
        }
        uploadFilesTask();
    }

    HttpTaskUtil.ResultListener resultListener = new HttpTaskUtil.ResultListener() {
        @Override
        public void onResponse(String response) {
            try {
                ResultTaskBean bean = JSON.parseObject(response, ResultTaskBean.class);
                if (bean != null && bean.code == 1) {
                    setResult(RESULT_OK);
                    finish();
                }
            } catch (Exception e) {
                ToastUtil.show((e != null && !TextUtils.isEmpty(e.getMessage()) ? e.getMessage() : "网络请求失败"));
            } finally {
                dismissProgressDialog();
            }
        }

        @Override
        public void onFailure(Request request, Exception e) {
            ToastUtil.show((e != null && !TextUtils.isEmpty(e.getMessage()) ? e.getMessage() : "网络请求失败"));
            dismissProgressDialog();
        }
    };

    private void uploadFilesTask() {
        if (urlImages == null || urlImages.size() == 0)
            return;
        File[] files = new File[urlImages.size()];
        String[] fileKeys = new String[urlImages.size()];
        for (int i = 0; i < urlImages.size(); i++) {
            files[i] = new File(urlImages.get(i));
            fileKeys[i] = urlImages.get(i);
        }
        showProgreessDialog();
        httpTaskUtil.UploadFilesTask(files, fileKeys, new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                ToastUtil.show(e != null ? e.getMessage() : "onFailure 文件上传失败");
                dismissProgressDialog();
            }

            @Override
            public void onResponse(String response) {
                dismissProgressDialog();
                if (!TextUtils.isEmpty(response)) {
                    JSONObject jsonObject = JSON.parseObject(response);
                    if (jsonObject != null && jsonObject.containsKey("code") && jsonObject.getInteger("code") == 1) {
                        String data = jsonObject.getString("data");
                        List<FileUploadImageBean> list = JSON.parseArray(data, FileUploadImageBean.class);
                        StringBuffer photos = new StringBuffer();
                        StringBuffer thumbs = new StringBuffer();
                        for (FileUploadImageBean bean : list) {
                            if (bean != null) {
                                photos.append(bean.getFileNameMD5()).append(",");
                                thumbs.append(bean.getFileNameThumb()).append(",");
                            }
                        }
                        if (photos.length() > 0)
                            photos.setLength(photos.length() - 1);
                        if (thumbs.length() > 0)
                            thumbs.setLength(thumbs.length() - 1);
                        insertCircleData(photos.toString(), thumbs.toString());
                    }
                }
            }
        });
    }

    private void insertCircleData(String photos, String thumbs) {
        String strInput = etInput.getText().toString();
        AccountBean bean = UserInfoManager.getInstance().getAccountInfo();
        CircleBean circleBean = new CircleBean();
        circleBean.setAccount(bean.getAccount());
        circleBean.setAccountId(bean.getId());
        circleBean.setDescription(TextUtils.isEmpty(strInput) ? etInput.getHint().toString() : strInput);
        circleBean.setPhotos(photos);
        circleBean.setThumbs(thumbs);
        circleBean.setAddress(tvAddress.getText().toString());
        String strJson = JSONObject.toJSONString(circleBean);
        showProgreessDialog();
        httpTaskUtil.InsertCircleRunTask(strJson, "" + bean.getId());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                String takePhotoPicpath = takeImagePath;
                File file = new File(takePhotoPicpath);
                if (file.exists() && file.length() > 0) {
                    urlImages.add(file.getAbsolutePath());
                    showGridImgsView(urlImages);
                }
            } else if (requestCode == REQUEST_ALBUM) {
                List<String> list = data.getStringArrayListExtra("images");
                if (list != null && list.size() > 0)
                    urlImages.addAll(list);
                showGridImgsView(urlImages);
            }
        }
        takeImagePath = "";
    }

    @Override
    protected void onDestroy() {
        Logger.e(TAG, "onDestroy");
        if (locationUtils != null)
            locationUtils.destroyLocation();
        super.onDestroy();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if (!TextUtils.isEmpty(strPoiName))
                        tvAddress.setText(strPoiName);
                    break;
            }
        }
    };

    private void getLocaltionData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                locationUtils = new LocationUtils();
                locationUtils.setListener(locationChangedListener);
                locationUtils.startLocation(CirclePublishActivity.this, false);
                Logger.e(TAG, "run over");
            }
        }).start();
    }

    LocationUtils.LocationChangedListener locationChangedListener = new LocationUtils.LocationChangedListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            try {
                if (null != loc && loc.getErrorCode() == 0) {
                    AppConstants.aMapLocation = loc;
                    strAddress = loc.getAddress();
                    strPoiName = loc.getPoiName();
                    strProvince = loc.getProvince();
                    strCity = loc.getCity();
                    if (strProvince.endsWith("省"))
                        strProvince = strProvince.substring(0, strProvince.length() - 1);
                    if (strCity.endsWith("市"))
                        strCity = strCity.substring(0, strCity.length() - 1);
                    Logger.e(TAG, "Province = " + strProvince + "  City = " + strCity);
                    handler.sendEmptyMessage(1);
                } else {
                    Logger.e(TAG, "loc error " + strProvince + "  City = " + strCity);
                    handler.sendEmptyMessage(0);
                }
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                locationUtils.stopLocation();
            }
        }
    };
}
