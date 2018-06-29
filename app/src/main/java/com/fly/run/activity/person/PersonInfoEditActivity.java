package com.fly.run.activity.person;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.bean.AccountBean;
import com.fly.run.bean.ResultTaskBean;
import com.fly.run.config.Constant;
import com.fly.run.httptask.HttpTaskUtil;
import com.fly.run.manager.UserInfoManager;
import com.fly.run.utils.BroadcastUtil;
import com.fly.run.utils.IOTools;
import com.fly.run.utils.ToastUtil;
import com.fly.run.view.ForwardItemView;
import com.fly.run.view.ImageView.RoundAngleImageView;
import com.fly.run.view.actionbar.CommonActionBar;
import com.fly.run.view.toast.CustomToast;
import com.squareup.okhttp.Request;

public class PersonInfoEditActivity extends BaseUIActivity implements View.OnClickListener {
    private CommonActionBar actionBar;
    private RelativeLayout rl_userPhoto;
    private RoundAngleImageView ivHeader;
    private ForwardItemView itemViewName;
    private ForwardItemView itemViewSex;
    private ForwardItemView itemViewLike;
    private ImageView iv_task;
    private HttpTaskUtil httpTaskUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_personl_info);
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

    private void initView(){
        rl_userPhoto = (RelativeLayout)findViewById(R.id.rl_userPhoto);
        ivHeader = (RoundAngleImageView)findViewById(R.id.img_view_header);
        itemViewName = (ForwardItemView)findViewById(R.id.item_view_name);
        itemViewSex = (ForwardItemView)findViewById(R.id.item_view_sex);
        itemViewLike = (ForwardItemView)findViewById(R.id.item_view_like);
        iv_task = (ImageView) findViewById(R.id.iv_task);
        rl_userPhoto.setOnClickListener(this);
        itemViewName.setOnClickListener(this);
        itemViewSex.setOnClickListener(this);
        itemViewLike.setOnClickListener(this);
        iv_task.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_userPhoto:
                break;
            case R.id.item_view_name:
                break;
            case R.id.item_view_sex:
                break;
            case R.id.item_view_like:
                break;
            case R.id.iv_task:
                String name = itemViewName.getContent_text();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(name.trim()))
                    httpTaskUtil.UserEditTask(UserInfoManager.getInstance().getAccount(), name);
                break;
        }
    }

    private HttpTaskUtil.ResultListener resultListener = new HttpTaskUtil.ResultListener() {
        @Override
        public void onResponse(String response) {
            if (TextUtils.isEmpty(response))
                return;
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
                if (isSuccess) {
                    BroadcastUtil.sendBroadcast(PersonInfoEditActivity.this,BroadcastUtil.USER_INFO_UPDATE);
                    CustomToast.showRefreshToast(PersonInfoEditActivity.this,true);
                } else {
                    CustomToast.showRefreshToast(PersonInfoEditActivity.this,false);
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
}
