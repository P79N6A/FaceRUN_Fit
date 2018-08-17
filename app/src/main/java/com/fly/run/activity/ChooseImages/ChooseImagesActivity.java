package com.fly.run.activity.ChooseImages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.adapter.ChooseImagesAdapter;
import com.fly.run.bean.FileItem;
import com.fly.run.utils.CompareOrderUtil;
import com.fly.run.utils.MediaQueryUtil;
import com.fly.run.utils.ToastUtil;
import com.fly.run.view.actionbar.CommonActionBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChooseImagesActivity extends BaseUIActivity {

    private CommonActionBar actionBar;
    private GridView gridView;
    private ChooseImagesAdapter adapter;
    private int ChooseCount = 9;

    private boolean isMultiple = true; //是否支持多选

    public static void startActivityResult(Activity activity, int num, boolean isMultiple) {
        Intent intent = new Intent(activity, ChooseImagesActivity.class);
        intent.putExtra("num", 0);
        intent.putExtra("isMultiple", isMultiple);
        activity.startActivityForResult(intent, REQUEST_ALBUM);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_images);
        int num = getIntent().getIntExtra("num", 0);
        isMultiple = getIntent().getBooleanExtra("isMultiple", isMultiple);
        ChooseCount = ChooseCount - num;
        initActionBar();
        initView();
        new Thread(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                Log.e(TAG, "start = " + start);
                handler.sendEmptyMessage(2);
                initLocalVideos();
                initLocalImages();
                handler.sendEmptyMessageDelayed(3,50);
                Log.e(TAG, "耗时 = " + (System.currentTimeMillis() - start) + "  数量 = " + list.size());
            }
        }).start();
    }

    private void initActionBar() {
        actionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        actionBar.setActionLeftIconListenr(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        actionBar.setActionRightTextListenr("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> imageList = new ArrayList<String>();
                for (FileItem item : adapter.getChooseItems())
                    imageList.add(item.filePath);
                Intent intent = new Intent();
                intent.putExtra("images", (Serializable) imageList);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    showGridImgsView(list);
                    break;
                case 2:
                    showProgreessDialog();
                    break;
                case 3:
                    dismissProgressDialog();
                    break;
            }
        }
    };

    private List<FileItem> list = null;

    private void initLocalImages() {
        List<FileItem> images = MediaQueryUtil.getAllPhoto(this);
        if (images != null){
            if (list == null)
                list = new ArrayList<>();
            list.addAll(images);
        }
        Collections.sort(list, new CompareOrderUtil());
        Log.e(TAG, "initLocalImages size = " + list.size());
        handler.sendEmptyMessage(1);
    }

    private void initLocalVideos() {
//        List<FileItem> list = MediaQueryUtil.getAllVideoImages(this);
//        Log.e(TAG, "initLocalVideos size = " + list.size());
//        List<FileItem> list2 = MediaQueryUtil.getAllVideo(this);
//        Log.e(TAG, "initLocalVideos2 size = " + list2.size());
        List<FileItem> videos = MediaQueryUtil.getAllVideos(this);
        if (videos != null){
            if (list == null)
                list = new ArrayList<>();
            list.addAll(videos);
        }
        Log.e(TAG, "initLocalVideos3 size = " + list.size());
    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.gridview);
        adapter = new ChooseImagesAdapter(this);
        gridView.setAdapter(adapter);
        adapter.setImageListener(new ChooseImagesAdapter.ImageListenrt() {
            @Override
            public void doShowImage(int position, String url) {

            }

            @Override
            public void doChooseImage(int position, String url) {
                FileItem item = adapter.getItem(position);
                if (!isMultiple){
                    ArrayList<String> imageList = new ArrayList<String>();
                    if (item != null && !TextUtils.isEmpty(item.getFilePath()))
                        imageList.add(item.getFilePath());
                    Intent intent = new Intent();
                    intent.putExtra("images", (Serializable) imageList);
                    setResult(RESULT_OK, intent);
                    finish();
                    return;
                }
                if (!item.isCheck()) {
                    if (adapter.getChooseItems().size() >= ChooseCount) {
                        ToastUtil.show("最多选择9张图片");
                        return;
                    }
                    adapter.getChooseItems().add(item);
                } else {
                    adapter.getChooseItems().remove(item);
                }
                item.setCheck(!item.isCheck());
                adapter.notifyDataSetChanged();
            }
        });
        showGridImgsView(null);
    }

    private void showGridImgsView(List<FileItem> images) {
        adapter.setData(images);
        adapter.notifyDataSetChanged();
    }
}
