package com.fly.run.fragment.circle;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.fly.run.R;
import com.fly.run.adapter.circle.FocusSearchAdapter;
import com.fly.run.bean.CircleBean;
import com.fly.run.bean.FocusRecyclerBean;
import com.fly.run.bean.ResultTaskBean;
import com.fly.run.config.UrlConstants;
import com.fly.run.httptask.HttpTaskUtil;
import com.fly.run.utils.Logger;
import com.fly.run.utils.ToastUtil;
import com.fly.run.view.actionbar.CommonActionBar;
import com.fly.run.view.circle.FocusRecyclerView.FocusRecyclerView;
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
public class CircleSearchFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private CommonActionBar actionBar;
    private FocusRecyclerView view_focus_header;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private FocusSearchAdapter adapter;
    private HttpTaskUtil httpTaskUtil;

    private int pageNum = 1;
    private final int pageSize = 40;

    public CircleSearchFragment() {
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
    public static CircleSearchFragment newInstance(String param1, String param2) {
        CircleSearchFragment fragment = new CircleSearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static CircleSearchFragment newInstance() {
        CircleSearchFragment fragment = new CircleSearchFragment();
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
        View view = inflater.inflate(R.layout.fragment_circle_search, container, false);
        initActionBar(view);
        initView(view);
        swipeRefreshLayout.setRefreshing(true);
        loadTaskData();
        return view;
    }

    private void initActionBar(View view) {
        actionBar = (CommonActionBar) view.findViewById(R.id.common_action_bar);
        actionBar.setActionTitle("");
        actionBar.setActionLeftIconListenr(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
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
        adapter = new FocusSearchAdapter(getActivity());
        listView.setAdapter(adapter);
        view_focus_header = (FocusRecyclerView) view.findViewById(R.id.view_focus_header);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:

                    break;
            }
        }
    };

    private void loadTaskData() {
        if (httpTaskUtil == null) {
            httpTaskUtil = new HttpTaskUtil();
            httpTaskUtil.setResultListener(resultListener);
        }
        httpTaskUtil.QueryCircleSearchRunTask(pageNum, pageSize);
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
                        List<FocusRecyclerBean> fileItems = getUrls(list);
                        StringBuffer sb = new StringBuffer();
                        StringBuffer images = new StringBuffer();
                        StringBuffer imagesHD = new StringBuffer();
                        List<CircleBean> list1 = new ArrayList<>();
                        for (int i=0;i<fileItems.size();i++){
                            if (i != 0 && i % 7 == 0){
                                if (sb.length() > 0)
                                    sb.setLength(sb.length() - 1);
                                CircleBean circleBean = new CircleBean();
                                circleBean.setPhotos(sb.toString());
                                list1.add(circleBean);
                                sb.setLength(0);
                            }
                            FocusRecyclerBean bean1 = fileItems.get(i);
                            sb.append(bean1.getHeaderUrl()).append(",");
                            images.append(bean1.getHeaderUrl()).append(",");
                            imagesHD.append(bean1.getHeaderUrlHD()).append(",");
                        }
                        if (images.length() > 0)
                            images.setLength(images.length() - 1);
                        if (imagesHD.length() > 0)
                            imagesHD.setLength(imagesHD.length() - 1);
                        adapter.setmImages(images.toString(),imagesHD.toString());
                        if (pageNum == 1) {
                            adapter.setData(list1);
                        } else {
                            adapter.addData(list1);
                        }
                        adapter.notifyDataSetInvalidated();
                        view_focus_header.setData(fileItems);
                    }
                }
            } catch (Exception e) {
                pageNum--;
                ToastUtil.show((e != null && !TextUtils.isEmpty(e.getMessage()) ? e.getMessage() : "网络请求失败"));
            } finally {
                swipeRefreshLayout.setRefreshing(false);
            }
        }

        @Override
        public void onFailure(Request request, Exception e) {
            pageNum--;
            ToastUtil.show((e != null && !TextUtils.isEmpty(e.getMessage()) ? e.getMessage() : "网络请求失败"));
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    public List<FocusRecyclerBean> getUrls(List<CircleBean> list) {
        List<FocusRecyclerBean> fileItems = new ArrayList<>();
        if (adapter != null) {
            for (CircleBean bean : list) {
                String thumbs = bean.getThumbs();
                String photos = bean.getPhotos();
                String[] urls = !TextUtils.isEmpty(thumbs) ? thumbs.split(",") : photos.split(",");
                if (urls != null && urls.length > 0) {
                    int index = 0;
                    for (String url : urls){
                        url = url.trim();
                        if (!url.startsWith("http://") && !url.startsWith("https://"))
                            url = String.format(UrlConstants.HTTP_DOWNLOAD_FILE_2, url);
                        String urlHD = "";
                        try {
                            urlHD = photos.split(",")[index];
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        index++;
                        String[] sf = url.split(File.separator);
                        String fileName = (sf != null && sf.length > 0) ? sf[sf.length - 1] : url;
                        FocusRecyclerBean item = new FocusRecyclerBean();
                        item.setName(fileName).setHeaderUrl(url).setHeaderUrlHD(urlHD);
                        fileItems.add(item);
                    }
                }
            }
        }
        return fileItems;
    }
}
