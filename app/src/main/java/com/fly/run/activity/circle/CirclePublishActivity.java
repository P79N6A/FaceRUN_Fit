package com.fly.run.activity.circle;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fly.run.R;
import com.fly.run.activity.ChooseImages.ChooseImagesActivity;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.adapter.PublishCircleGridAdapter;
import com.fly.run.bean.AccountBean;
import com.fly.run.bean.CircleBean;
import com.fly.run.bean.FileUploadImageBean;
import com.fly.run.bean.ResultTaskBean;
import com.fly.run.httptask.HttpTaskUtil;
import com.fly.run.manager.UserInfoManager;
import com.fly.run.utils.Logger;
import com.fly.run.utils.OkHttpClientManager;
import com.fly.run.utils.ToastUtil;
import com.fly.run.view.actionbar.CommonActionBar;
import com.squareup.okhttp.Request;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CirclePublishActivity extends BaseUIActivity implements View.OnClickListener {

    private CommonActionBar actionBar;
    private EditText etInput;
    private TextView tvAddress;
    private GridView gridView;
    private PublishCircleGridAdapter adapter;
    private HttpTaskUtil httpTaskUtil;
    private final int CHOOSE_IMAGES_CODE = 1001;
    private List<String> urlImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_circle);
        initActionBar();
        initView();
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

    private void initView() {
        etInput = (EditText) findViewById(R.id.et_input);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        gridView = (GridView) findViewById(R.id.gridview);
        adapter = new PublishCircleGridAdapter(this);
        gridView.setAdapter(adapter);
        adapter.setImageListener(new PublishCircleGridAdapter.ImageListenrt() {
            @Override
            public void doShowImage(int position, String url) {

            }

            @Override
            public void doAddImage() {
                Intent intent = new Intent(CirclePublishActivity.this, ChooseImagesActivity.class);
                intent.putExtra("num", urlImages.size());
                startActivityForResult(intent, CHOOSE_IMAGES_CODE);
            }
        });
        showGridImgsView(urlImages);
    }

    private void showGridImgsView(List<String> images) {
        adapter.setUrlImages(images);
        adapter.notifyDataSetChanged();
    }

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
            insertCircleData("","");
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
                        List<FileUploadImageBean> list = JSON.parseArray(data,FileUploadImageBean.class);
                        StringBuffer photos = new StringBuffer();
                        StringBuffer thumbs = new StringBuffer();
                        for(FileUploadImageBean bean : list){
                            if (bean != null){
                                photos.append(bean.getFileNameMD5()).append(",");
                                thumbs.append(bean.getFileNameThumb()).append(",");
                            }
                        }
                        if (photos.length() > 0)
                            photos.setLength(photos.length() - 1);
                        if (thumbs.length() > 0)
                            thumbs.setLength(thumbs.length() - 1);
                        insertCircleData(photos.toString(),thumbs.toString());
                    }
                }
            }
        });
    }

    private void insertCircleData(String photos,String thumbs) {
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
        if (resultCode == RESULT_OK && requestCode == CHOOSE_IMAGES_CODE) {
            List<String> list = data.getStringArrayListExtra("images");
            if (list != null && list.size() > 0)
                urlImages.addAll(list);
            showGridImgsView(urlImages);
        }
    }

    @Override
    protected void onDestroy() {
        Logger.e(TAG, "onDestroy");
        super.onDestroy();
    }
}
