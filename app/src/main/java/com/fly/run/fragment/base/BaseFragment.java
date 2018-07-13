package com.fly.run.fragment.base;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.fly.run.app.App;
import com.fly.run.utils.SDCardUtil;
import com.fly.run.utils.TimeFormatUtils;
import com.fly.run.view.dialog.ProgressDialog;

import java.io.File;

public class BaseFragment extends Fragment {

    protected String TAG = getClass().getSimpleName();
    protected static final int REQUEST_CAMERA = 10001;
    protected static final int REQUEST_ALBUM = 10002;
    protected String takeImagePath = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public boolean onBackPressed() {
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onDestroyView() {
        dismissProgressDialog();
        super.onDestroyView();
    }

    public Context getContextActivity() {
        if (getActivity() != null)
            return getActivity();
        else
            return App.getInstance();
    }

    /**
     * 关闭输入法
     *
     * @param view
     */
    protected void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && view != null) {
            view.clearFocus();
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 打开输入法
     */
    protected void toggleSoftInput() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    private ProgressDialog pDialog;

    /**
     * 显示等待对话框 当点击返回键取消对话框并停留在该界面
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void showProgreessDialog() {
        if (pDialog == null) {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setCanceledOnTouchOutside(false);
        }
        if (pDialog.isShowing())
            pDialog.dismiss();
        pDialog.show();
        pDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    try {
                        dismissProgressDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
    }

    /**
     * 销毁对话框
     */
    public void dismissProgressDialog() {
        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String takeCarema() {
        takeImagePath = "";
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
            return "";
        }
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        takeImagePath = getTakePhotoPicpath();
        File imageFile = new File(takeImagePath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(imageFile));
        startActivityForResult(intent, REQUEST_CAMERA);
        return takeImagePath;
    }

    protected String getTakePhotoPicpath() {
        StringBuffer sb = new StringBuffer();
        String imageName = "image_" + TimeFormatUtils.getRecordFormatDate(System.currentTimeMillis());
        sb.append(SDCardUtil.getImgDir()).append("/").append(imageName).append(".jpg");
        return sb.toString();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                takeCarema();
            } else {
                // Permission Denied
                //  displayFrameworkBugMessageAndExit();
                Toast.makeText(getActivity(), "请在应用管理中打开“相机”访问权限！", Toast.LENGTH_LONG).show();
            }
        }
    }
}
