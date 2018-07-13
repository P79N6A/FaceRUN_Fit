package com.fly.run.fragment.circle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.fly.run.R;
import com.fly.run.activity.ChooseImages.ChooseImagesActivity;
import com.fly.run.activity.circle.CirclePublishActivity;
import com.fly.run.adapter.circle.CircleAdapter;
import com.fly.run.bean.AccountBean;
import com.fly.run.bean.CircleBean;
import com.fly.run.bean.FocusRecyclerBean;
import com.fly.run.bean.ResultTaskBean;
import com.fly.run.config.UrlConstants;
import com.fly.run.fragment.base.BaseFragment;
import com.fly.run.httptask.HttpTaskUtil;
import com.fly.run.manager.UserInfoManager;
import com.fly.run.utils.ToastUtil;
import com.fly.run.view.actionbar.CommonActionBar;
import com.fly.run.view.circle.FocusRecyclerView.FocusRecyclerView;
import com.fly.run.view.dialog.DialogChooseMedia;
import com.squareup.okhttp.Request;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link CircleRunFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CircleRunFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private CommonActionBar actionBar;
    private FocusRecyclerView view_focus_header;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private CircleAdapter adapter;
    private HttpTaskUtil httpTaskUtil;

    private int pageNum = 1;
    private final int pageSize = 40;


    public CircleRunFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CircleRunFragment.
     */
    public static CircleRunFragment newInstance(String param1, String param2) {
        CircleRunFragment fragment = new CircleRunFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static CircleRunFragment newInstance() {
        CircleRunFragment fragment = new CircleRunFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_circle_run, container, false);
        initActionBar(view);
        initView(view);
        return view;
    }


    private void initActionBar(View view) {
        actionBar = (CommonActionBar) view.findViewById(R.id.common_action_bar);
        actionBar.setActionTitle("跑圈");
        actionBar.setActionLeftIconListenr(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        actionBar.setActionRightIconListenr(R.drawable.icon_popup_camera, getResources().getColor(R.color.color_ffffff), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogChooseMedia dialog = new DialogChooseMedia(getActivity());
                dialog.setOnEventListener(onEventListener);
                dialog.show();
            }
        });
    }

    private void initView(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
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
        listView = (ListView) view.findViewById(R.id.listview);
        adapter = new CircleAdapter(getActivity());
        listView.setAdapter(adapter);

        view_focus_header = (FocusRecyclerView)view.findViewById(R.id.view_focus_header);
    }

    DialogChooseMedia.OnEventListener onEventListener = new DialogChooseMedia.OnEventListener() {

        @Override
        public void result(int index) {
            if (index == 1) {
                takeCarema();
            } else if (index == 2) {
                Intent intent = new Intent(getActivity(), ChooseImagesActivity.class);
                intent.putExtra("num", 0);
                startActivityForResult(intent, REQUEST_ALBUM);
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            if (requestCode == REQUEST_CAMERA) {
                String takePhotoPicpath = takeImagePath;
                File file = new File(takePhotoPicpath);
                if (file.exists() && file.length() > 0) {
                    ArrayList<String> list = new ArrayList<>();
                    list.add(file.getAbsolutePath());
                    Intent intent = new Intent(getActivity(), CirclePublishActivity.class);
                    intent.putExtra("images", list);
                    startActivity(intent);
                }
            } else if (requestCode == REQUEST_ALBUM) {
                ArrayList<String> list = data.getStringArrayListExtra("images");
                if (list != null && list.size() > 0) {
                    CirclePublishActivity.startActivityForResultRefresh(getActivity(), list);
//                    Intent intent = new Intent(CircleActivity.this, CirclePublishActivity.class);
//                    intent.putExtra("images", list);
//                    startActivity(intent);
                }
            } else if (requestCode == CirclePublishActivity.ACTION_PUSH_CIRCLE) {
                pageNum = 1;
                loadTaskData();
            }
        }
        takeImagePath = "";
    }

    private void loadTaskData() {
        if (httpTaskUtil == null) {
            httpTaskUtil = new HttpTaskUtil();
            httpTaskUtil.setResultListener(resultListener);
        }
        AccountBean bean = UserInfoManager.getInstance().getAccountInfo();
//        if (bean == null || bean.getId() <= 0) {
//            ToastUtil.show("请登录");
//            swipeRefreshLayout.setRefreshing(false);
//            return;
//        }
        httpTaskUtil.QueryCircleRunTask(pageNum, pageSize);
    }

    HttpTaskUtil.ResultListener resultListener = new HttpTaskUtil.ResultListener() {
        @Override
        public void onResponse(String response) {
            try {
                ResultTaskBean bean = JSON.parseObject(response, ResultTaskBean.class);
                if (bean != null && bean.code == 1) {
                    if (!TextUtils.isEmpty(bean.data)) {
                        List<CircleBean> list = JSON.parseArray(bean.data, CircleBean.class);
                        if (list == null || list.size() == 0)
                            return;
                        if (pageNum == 1) {
                            adapter.setData(list);
                        } else {
                            adapter.addData(list);
                        }
                        adapter.notifyDataSetChanged();
                        view_focus_header.setData(getUrls());
                    }
                }
            } catch (Exception e) {
                pageNum--;
                ToastUtil.show((e != null && !TextUtils.isEmpty(e.getMessage()) ? e.getMessage() : "网络请求失败"));
            } finally {
                if (pageNum == 1)
                    swipeRefreshLayout.setRefreshing(false);
//                else
//                    listView.doneMore();
            }
        }

        @Override
        public void onFailure(Request request, Exception e) {
            pageNum--;
            ToastUtil.show((e != null && !TextUtils.isEmpty(e.getMessage()) ? e.getMessage() : "网络请求失败"));
        }
    };

    public List<FocusRecyclerBean> getUrls(){
        List<FocusRecyclerBean> fileItems = new ArrayList<>();
        if (adapter != null){
            List<CircleBean> list = adapter.getDatas();
            for (CircleBean bean : list){
                String[] urls = bean.getPhotos().split(",");
                for (String url : urls){
                    if (!url.startsWith("http://"))
                        url = String.format(UrlConstants.HTTP_DOWNLOAD_FILE_2, url);
                    String[] sf = url.split(File.separator);
                    String fileName = (sf != null && sf.length > 0) ? sf[sf.length-1] : url;
                    FocusRecyclerBean item = new FocusRecyclerBean();
                    item.setName(fileName).setHeaderUrl(url);
                    fileItems.add(item);
                }
            }
        }
        return fileItems;
    }
}
