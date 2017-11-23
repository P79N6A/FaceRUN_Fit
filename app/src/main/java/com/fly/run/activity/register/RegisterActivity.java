package com.fly.run.activity.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fly.run.R;
import com.fly.run.activity.MainActivity;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.bean.AccountBean;
import com.fly.run.bean.ResultTaskBean;
import com.fly.run.config.Constant;
import com.fly.run.httptask.HttpTaskUtil;
import com.fly.run.manager.UserInfoManager;
import com.fly.run.utils.IOTools;
import com.fly.run.utils.TextTools;
import com.fly.run.utils.ToastUtil;
import com.fly.run.view.actionbar.CommonActionBar;
import com.squareup.okhttp.Request;

import java.io.IOException;

public class RegisterActivity extends BaseUIActivity {

    private CommonActionBar actionBar;
    private EditText et_input_email, et_input_password;
    private TextView tv_sign_up_complete;
    private HttpTaskUtil httpTaskUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register_email);
        initActionBar();
        initView();
    }

    private void initActionBar() {
        actionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        actionBar.setActionTitle(getResources().getString(R.string.txt_register));
        actionBar.setActionLeftIconListenr(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        et_input_email = (EditText) findViewById(R.id.et_input_email);
        et_input_password = (EditText) findViewById(R.id.et_input_password);
        tv_sign_up_complete = (TextView) findViewById(R.id.tv_sign_up_complete);
        tv_sign_up_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail = et_input_email.getText().toString();
                String strPassword = et_input_password.getText().toString();
                if (TextUtils.isEmpty(strEmail) || !TextTools.isEmail(strEmail) || TextUtils.isEmpty(strPassword)) {
                    ToastUtil.show("请输入邮箱或密码");
                    return;
                }
                if (httpTaskUtil == null) {
                    httpTaskUtil = new HttpTaskUtil().setResultListener(resultListener);
                }
                httpTaskUtil.RegisterTask(strEmail, strPassword);
            }
        });
    }

    private HttpTaskUtil.ResultListener resultListener = new HttpTaskUtil.ResultListener() {
        @Override
        public void onResponse(String response) {
            if (TextUtils.isEmpty(response))
                return;
            boolean isRegistSuccess = false;
            try {
                ResultTaskBean bean = JSON.parseObject(response, ResultTaskBean.class);
                if (bean != null && bean.flag && !TextUtils.isEmpty(bean.reason)) {
//                    ToastUtil.show(bean.reason);
                    AccountBean accountBean = JSON.parseObject(bean.reason, AccountBean.class);
                    if (accountBean != null) {
                        isRegistSuccess = true;
                        UserInfoManager.getInstance().setAccountInfo(accountBean);
                        String path = Constant.UserConfigPath + "Account.info";
                        IOTools.writeObject(path, accountBean);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                sendBroadcast(isRegistSuccess ? MainActivity.Regist_State_Success : MainActivity.Regist_State_Fail);
                if (isRegistSuccess) {
//                    setResult(RESULT_OK);
                    finish();
                } else {
                    ToastUtil.show("注册失败");
                }
            }
        }

        @Override
        public void onFailure(Request request, IOException e) {
            if (e != null && !TextUtils.isEmpty(e.getMessage()))
                ToastUtil.show(e.getMessage());
            else
                ToastUtil.show("与服务器连接失败");
        }
    };

    /**
     * 发送广播
     */
    private void sendBroadcast(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        sendBroadcast(intent);
    }

}
