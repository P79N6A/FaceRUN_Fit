package com.fly.run.activity.circle.reply;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.adapter.circle.CircleAdapter;
import com.fly.run.adapter.circle.CircleReplyAdapter;
import com.fly.run.bean.AccountBean;
import com.fly.run.bean.CircleBean;
import com.fly.run.bean.CircleReply;
import com.fly.run.bean.ResultTaskBean;
import com.fly.run.fragment.circle.CircleAttentionFragment;
import com.fly.run.fragment.circle.CircleRunFragment;
import com.fly.run.fragment.circle.CircleSearch2Fragment;
import com.fly.run.fragment.circle.CircleSearchFragment;
import com.fly.run.httptask.HttpTaskUtil;
import com.fly.run.manager.UserInfoManager;
import com.fly.run.utils.Logger;
import com.fly.run.utils.OkHttpClientManager;
import com.fly.run.utils.ToastUtil;
import com.fly.run.view.actionbar.CommonActionBar;
import com.fly.run.view.circle.FocusRecyclerView.FocusRecyclerView;
import com.fly.run.view.dialog.DialogChooseMedia;
import com.fly.run.view.viewpager.CustomViewPager;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CircleReplyActivity extends BaseUIActivity implements View.OnClickListener {

    private int diff;

    private CommonActionBar actionBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private CircleReplyAdapter adapter;
    private EditText et_reply_message;
    private ImageView iv_reply_send;
    private TextView tv_reply_item;
    private HttpTaskUtil httpTaskUtil;
    private CircleReply circleReply;
    private int mCircleId;
    private boolean textChanged = false;

    private int pageNum = 1;
    private final int pageSize = 20;

    public static void startActivity(Context context, int circleId){
        Intent intent = new Intent(context,CircleReplyActivity.class);
        intent.putExtra("DATA",circleId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_reply);
        View decorView = getWindow().getDecorView();
        View contentView = findViewById(Window.ID_ANDROID_CONTENT);
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(getGlobalLayoutListener(decorView, contentView));
        this.mCircleId = getIntent().getExtras().getInt("DATA");
        initActionBar();
        initView();
        loadTaskData();
    }

    private ViewTreeObserver.OnGlobalLayoutListener getGlobalLayoutListener(final View decorView, final View contentView) {
        return new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                decorView.getWindowVisibleDisplayFrame(r);
                int height = decorView.getContext().getResources().getDisplayMetrics().heightPixels;
                diff = height - r.bottom;

                if (diff != 0) {
                    if (contentView.getPaddingBottom() != diff) {
                        contentView.setPadding(0, 0, 0, diff);
                    }
                } else {
                    if (contentView.getPaddingBottom() != 0) {
                        contentView.setPadding(0, 0, 0, 0);
                    }
                }
            }
        };
    }

    private void initActionBar() {
        actionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
//        actionBar.setActionTitle("跑圈");
        actionBar.setActionLeftIconListenr(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        // 设置颜色属性的时候一定要注意是引用了资源文件还是直接设置16进制的颜色，因为都是int值容易搞混
        // 设置下拉进度的背景颜色，默认就是白色的
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_purple, android.R.color.holo_green_light, R.color.colorAccent, android.R.color.holo_blue_light);

        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                loadTaskData();
            }
        });
        listView = (ListView) findViewById(R.id.listview);
        adapter = new CircleReplyAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                circleReply = adapter.getItem(i);
                if (diff == 0){
                    toggleSoftInput();
                }
                et_reply_message.setText("@"+circleReply.getReplyUserName()+" ");
                et_reply_message.setSelection(et_reply_message.getText().toString().length());
                tv_reply_item.setVisibility(View.VISIBLE);
                tv_reply_item.setText("回复 @"+circleReply.getReplyUserName()+":"+circleReply.getReplyContent());
            }
        });
        et_reply_message = (EditText)findViewById(R.id.et_reply_message);
        et_reply_message.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_DEL){
                    String text = et_reply_message.getText().toString();
                    if (textChanged && circleReply != null){
                        if (("@"+circleReply.getReplyUserName()).equals(text)){
                            et_reply_message.setText("");
                        } else if (!text.startsWith("@"+circleReply.getReplyUserName()) && text.contains(" ")){
                            String[] ss = text.split(" ");
                            if (ss != null){
                                if (ss.length == 1){
                                    et_reply_message.setText("");
                                } else if (ss.length >= 2){
                                    String s0 = ss[0];
                                    et_reply_message.setText(text.substring(s0.length()));
                                }
                            }
                        }
                    }
                }
                return false;
            }
        });
        et_reply_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textChanged = false;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence != null && charSequence.length() == 0){
                    tv_reply_item.setVisibility(View.GONE);
                    tv_reply_item.setText("");
                    circleReply = null;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                textChanged = true;
            }
        });
        iv_reply_send = (ImageView)findViewById(R.id.iv_reply_send);
        iv_reply_send.setOnClickListener(this);
        tv_reply_item = (TextView) findViewById(R.id.tv_reply_item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_reply_send:
                String replyMsg = et_reply_message.getText().toString();
                Map<String,String> param = new HashMap<>();
                param.put("replyCircleId",""+mCircleId);
                param.put("replyUserId", ""+UserInfoManager.getInstance().getAccountId());
                param.put("replyContent", replyMsg);
                if (circleReply != null){
                    if (circleReply.getId() > 0){
                        param.put("replyId", ""+circleReply.getId());
                    }
                    if (circleReply.getReplyRootId() == 0){
                        param.put("replyRootId", ""+circleReply.getId());
                    } else {
                        param.put("replyRootId", ""+circleReply.getReplyRootId());
                    }
                } else {
                    param.put("replyRootId", "0");
                }
                insertReplyTask(param);
                break;
        }
    }

    private void insertReplyTask(Map param){
        showProgreessDialog();
        httpTaskUtil.InsertCircleReplyTask(param, new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                dismissProgressDialog();
            }

            @Override
            public void onResponse(String response) {
                try {
                    ResultTaskBean bean = JSON.parseObject(response, ResultTaskBean.class);
                    if (bean != null && bean.code == 1) {
                        loadTaskData();
                    } else {
                        dismissProgressDialog();
                    }
                } catch (Exception e) {
                    ToastUtil.show((e != null && !TextUtils.isEmpty(e.getMessage()) ? e.getMessage() : "网络请求失败"));
                    dismissProgressDialog();
                } finally {
                    et_reply_message.setText("");
                }
            }
        });
    }

    private void loadTaskData() {
        if (httpTaskUtil == null) {
            httpTaskUtil = new HttpTaskUtil();
            httpTaskUtil.setResultListener(resultListener);
        }
        showProgreessDialog();
        httpTaskUtil.QueryCircleReplyRunTask(pageNum, pageSize,mCircleId,0);
    }

    HttpTaskUtil.ResultListener resultListener = new HttpTaskUtil.ResultListener() {
        @Override
        public void onResponse(String response) {
            try {
                ResultTaskBean bean = JSON.parseObject(response, ResultTaskBean.class);
                if (bean != null && bean.code == 1) {
                    if (!TextUtils.isEmpty(bean.data)) {
                        List<CircleReply> list = JSON.parseArray(bean.data, CircleReply.class);
                        if (list == null || list.size() == 0)
                            return;
                        if (pageNum == 1) {
                            adapter.setData(list);
                        } else {
                            adapter.addData(list);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            } catch (Exception e) {
                pageNum--;
                ToastUtil.show((e != null && !TextUtils.isEmpty(e.getMessage()) ? e.getMessage() : "网络请求失败"));
            } finally {
                swipeRefreshLayout.setRefreshing(false);
                dismissProgressDialog();
            }
        }

        @Override
        public void onFailure(Request request, Exception e) {
            pageNum--;
            ToastUtil.show((e != null && !TextUtils.isEmpty(e.getMessage()) ? e.getMessage() : "网络请求失败"));
            swipeRefreshLayout.setRefreshing(false);
            dismissProgressDialog();
        }
    };
}
