package com.fly.run.activity.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.activity.register.RegisterActivity;
import com.fly.run.bean.AccountBean;
import com.fly.run.bean.ResultTaskBean;
import com.fly.run.config.Constant;
import com.fly.run.httptask.HttpTaskUtil;
import com.fly.run.manager.UserInfoManager;
import com.fly.run.utils.IOTools;
import com.fly.run.utils.Logger;
import com.fly.run.utils.TextTools;
import com.fly.run.utils.ToastUtil;
import com.fly.run.view.actionbar.CommonActionBar;
import com.squareup.okhttp.Request;

import java.io.IOException;

import static com.fly.run.activity.MainActivity.Login_State_Fail;
import static com.fly.run.activity.MainActivity.Login_State_Success;

public class LoginActivity extends BaseUIActivity {

    private CommonActionBar actionBar;
    private EditText et_input_email, et_input_password;
    private TextView tv_sign_up_complete;
    private HttpTaskUtil httpTaskUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver();
        setContentView(R.layout.activity_login);
        initActionBar();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterReceiver();
    }

    private void initActionBar() {
        actionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        actionBar.setActionTitle(getResources().getString(R.string.txt_login));
        actionBar.setActionLeftIconListenr(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        actionBar.setActionRightTextListenr(getString(R.string.txt_register), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToActivity(RegisterActivity.class);
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
                    ToastUtil.show("邮箱或密码错误");
                    return;
                }
                if (httpTaskUtil == null) {
                    httpTaskUtil = new HttpTaskUtil().setResultListener(resultListener);
                }
                httpTaskUtil.LoginTask(strEmail, strPassword);
            }
        });
    }

    private HttpTaskUtil.ResultListener resultListener = new HttpTaskUtil.ResultListener() {
        @Override
        public void onResponse(String response) {
            if (TextUtils.isEmpty(response))
                return;
            boolean isLoginSuccess = false;
            try {
                ResultTaskBean bean = JSON.parseObject(response, ResultTaskBean.class);
                if (bean != null && bean.flag && !TextUtils.isEmpty(bean.reason)) {
                    AccountBean accountBean = JSON.parseObject(bean.reason, AccountBean.class);
                    if (accountBean != null) {
                        isLoginSuccess = true;
                        UserInfoManager.getInstance().setAccountInfo(accountBean);
                        String path = Constant.UserConfigPath + "Account.info";
                        boolean save = IOTools.writeObject(path, accountBean);
                        Logger.e(TAG, "path = " + path + "  save = " + save);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                sendBroadcast(isLoginSuccess ? Login_State_Success : Login_State_Fail);
                if (isLoginSuccess) {
//                    setResult(RESULT_OK);
                    finish();
                } else {
                    ToastUtil.show("登录失败");
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

    /**
     * 监听登录成功的Receiver
     */
    private BroadcastReceiver mLoginStateReceiver;
    //    public static final String Login_State_Success = "Login_State_Success";
//    public static final String Login_State_Fail = "Login_State_Fail";
    public static final String Regist_State_Success = "Regist_State_Success";
    public static final String Regist_State_Fail = "Regist_State_Fail";

    public void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Login_State_Success);
//        intentFilter.addAction(Login_State_Fail);
        intentFilter.addAction(Regist_State_Success);
        intentFilter.addAction(Regist_State_Fail);
        if (null == mLoginStateReceiver) {
            mLoginStateReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    switch (action) {
                        case Login_State_Success:
                            break;
                        case Login_State_Fail:
                            break;
                        case Regist_State_Success:
                            finish();
                            break;
                        case Regist_State_Fail:
                            break;
                    }
                }
            };
        }
        registerReceiver(mLoginStateReceiver, intentFilter);
    }

    public void unRegisterReceiver() {
        if (null != mLoginStateReceiver) {
            unregisterReceiver(mLoginStateReceiver);
        }
    }

}
