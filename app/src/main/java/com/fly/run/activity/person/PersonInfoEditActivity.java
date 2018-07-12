package com.fly.run.activity.person;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fly.run.R;
import com.fly.run.activity.ChooseImages.ChooseImagesActivity;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.bean.AccountBean;
import com.fly.run.bean.FileUploadImageBean;
import com.fly.run.bean.ResultTaskBean;
import com.fly.run.config.Constant;
import com.fly.run.config.UrlConstants;
import com.fly.run.httptask.HttpTaskUtil;
import com.fly.run.manager.UserInfoManager;
import com.fly.run.utils.BroadcastUtil;
import com.fly.run.utils.IOTools;
import com.fly.run.utils.ImageLoaderOptions;
import com.fly.run.utils.OkHttpClientManager;
import com.fly.run.utils.ToastUtil;
import com.fly.run.view.ForwardItemView;
import com.fly.run.view.ImageView.RoundAngleImageView;
import com.fly.run.view.actionbar.CommonActionBar;
import com.fly.run.view.dialog.DialogChooseMedia;
import com.fly.run.view.dialog.DialogTextContent;
import com.fly.run.view.toast.CustomToast;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PersonInfoEditActivity extends BaseUIActivity implements View.OnClickListener {
    private CommonActionBar actionBar;
    private RelativeLayout rl_userPhoto;
    private RoundAngleImageView ivHeader;
    private ForwardItemView itemViewName;
    private ForwardItemView itemViewSex;
    private ForwardItemView itemViewLike;
    private ImageView iv_task;
    private HttpTaskUtil httpTaskUtil;
    private AccountBean accountBean;
    private String headerPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_personl_info);
        accountBean = UserInfoManager.getInstance().getAccountInfo();
        initActionBar();
        initView();
        httpTaskUtil = new HttpTaskUtil().setResultListener(resultListener);
    }

    private void initActionBar() {
        actionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        actionBar.setActionTitle("编辑");
        actionBar.setActionLeftIconListenr(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        rl_userPhoto = (RelativeLayout) findViewById(R.id.rl_userPhoto);
        ivHeader = (RoundAngleImageView) findViewById(R.id.img_view_header);
        itemViewName = (ForwardItemView) findViewById(R.id.item_view_name);
        itemViewSex = (ForwardItemView) findViewById(R.id.item_view_sex);
        itemViewLike = (ForwardItemView) findViewById(R.id.item_view_like);
        iv_task = (ImageView) findViewById(R.id.iv_task);
        rl_userPhoto.setOnClickListener(this);
        itemViewName.setOnClickListener(this);
        itemViewSex.setOnClickListener(this);
        itemViewLike.setOnClickListener(this);
        iv_task.setOnClickListener(this);
        if (accountBean != null) {
            ImageLoader.getInstance().displayImage(accountBean.getHeadPortrait(), ivHeader, ImageLoaderOptions.optionsUserHeader);
            itemViewName.setContent_text(accountBean.getName());
        }
    }


    @Override
    public void onClick(View view) {
        DialogTextContent dialogTextContent = null;
        switch (view.getId()) {
            case R.id.rl_userPhoto:
                DialogChooseMedia dialog = new DialogChooseMedia(PersonInfoEditActivity.this);
                dialog.setOnEventListener(onEventListener);
                dialog.show();
                break;
            case R.id.item_view_name:
                dialogTextContent = new DialogTextContent(PersonInfoEditActivity.this);
                dialogTextContent.setTypeLine(1, DialogTextContent.ACTION_TYPE_NAME);
                dialogTextContent.setOnEventListener(onTextContentEventListener);
                dialogTextContent.show();
                break;
            case R.id.item_view_sex:
                break;
            case R.id.item_view_like:
                dialogTextContent = new DialogTextContent(PersonInfoEditActivity.this);
                dialogTextContent.setTypeLine(5, DialogTextContent.ACTION_TYPE_DESC);
                dialogTextContent.setOnEventListener(onTextContentEventListener);
                dialogTextContent.show();
                break;
            case R.id.iv_task:
                String strName = itemViewName.getContent_text();
                String strLike = itemViewName.getContent_text();
                if ((!TextUtils.isEmpty(strName) && !TextUtils.isEmpty(strName.trim())) || !TextUtils.isEmpty(headerPath))
                    uploadFilesTask();
                break;
        }
    }

    DialogChooseMedia.OnEventListener onEventListener = new DialogChooseMedia.OnEventListener() {

        @Override
        public void result(int index) {
            if (index == 1) {
                takeCarema();
            } else if (index == 2) {
                ChooseImagesActivity.startActivityResult(PersonInfoEditActivity.this, 0, false);
//                Intent intent = new Intent(PersonInfoEditActivity.this, ChooseImagesActivity.class);
//                intent.putExtra("num", 0);
//                startActivityForResult(intent, REQUEST_ALBUM);
            }
        }
    };

    DialogTextContent.OnEventListener onTextContentEventListener = new DialogTextContent.OnEventListener() {

        @Override
        public void result(String content) {

        }

        @Override
        public void result(String content, int type) {
            switch (type) {
                case DialogTextContent.ACTION_TYPE_NAME:
                    accountBean.setName(content);
                    itemViewName.setContent_text(content);
                    break;
                case DialogTextContent.ACTION_TYPE_DESC:
                    accountBean.setDescription(content);
                    itemViewLike.setContent_text(content);
                    break;
            }
        }
    };

    private HttpTaskUtil.ResultListener resultListener = new HttpTaskUtil.ResultListener() {
        @Override
        public void onResponse(String response) {
            if (TextUtils.isEmpty(response)) {
                dismissProgressDialog();
                return;
            }
            boolean isSuccess = false;
            try {
                ResultTaskBean bean = JSON.parseObject(response, ResultTaskBean.class);
                if (bean != null && bean.code == 1 && !TextUtils.isEmpty(bean.data)) {
                    AccountBean accountBean = JSON.parseObject(bean.data, AccountBean.class);
                    if (accountBean != null) {
                        isSuccess = true;
                        UserInfoManager.getInstance().setAccountInfo(accountBean);
                        String path = Constant.UserConfigPath + "Account.info";
                        IOTools.writeObject(path, accountBean);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dismissProgressDialog();
                if (isSuccess) {
                    BroadcastUtil.sendBroadcast(PersonInfoEditActivity.this, BroadcastUtil.USER_INFO_UPDATE);
                    CustomToast.showRefreshToast(PersonInfoEditActivity.this, true);
                } else {
                    CustomToast.showRefreshToast(PersonInfoEditActivity.this, false);
                }
            }
        }

        @Override
        public void onFailure(Request request, Exception e) {
            if (e != null && !TextUtils.isEmpty(e.getMessage()))
                ToastUtil.show(e.getMessage());
            else
                ToastUtil.show("与服务器连接失败");
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            if (requestCode == REQUEST_CAMERA) {
                String takePhotoPicpath = takeImagePath;
                File file = new File(takePhotoPicpath);
                if (file.exists() && file.length() > 0) {
                    ArrayList<String> list = new ArrayList<>();
                    list.add(file.getAbsolutePath());

                }
            } else if (requestCode == REQUEST_ALBUM) {
                ArrayList<String> list = data.getStringArrayListExtra("images");
                if (list != null && list.size() > 0) {
                    headerPath = list.get(0);
                    ImageLoader.getInstance().displayImage("file:///" + headerPath, ivHeader, ImageLoaderOptions.optionsUserHeader);
                }
            }
        }
        takeImagePath = "";
    }

    private void uploadFilesTask() {
        if (TextUtils.isEmpty(headerPath))
            return;
        File[] files = new File[1];
        String[] fileKeys = new String[1];
        for (int i = 0; i < 1; i++) {
            files[i] = new File(headerPath);
            fileKeys[i] = headerPath;
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
                boolean isUpdate = false;
                try {
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
                            String url = photos.toString();
                            if (!url.startsWith("http://"))
                                url = String.format(UrlConstants.HTTP_DOWNLOAD_FILE_2, url);
                            accountBean.setHeadPortrait(url);
                            httpTaskUtil.UserEditTask(UserInfoManager.getInstance().getAccountInfo());
                            isUpdate = true;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    dismissProgressDialog();
                } finally {
                    if (!isUpdate)
                        dismissProgressDialog();
                }
            }
        });
    }
}
