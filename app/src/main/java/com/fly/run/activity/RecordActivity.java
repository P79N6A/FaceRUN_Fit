package com.fly.run.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;

import com.amap.api.maps.model.LatLng;
import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.activity.map.record.RecordMapActivity;
import com.fly.run.activity.map.smooth.SmoothMoveActivity;
import com.fly.run.activity.map.trace.TraceActivity;
import com.fly.run.adapter.RecordAdapter;
import com.fly.run.bean.RunBean;
import com.fly.run.db.helper.RunDBHelper;
import com.fly.run.utils.DisplayUtil;
import com.fly.run.utils.FileUtil;
import com.fly.run.utils.SDCardUtil;
import com.fly.run.utils.TimeFormatUtils;
import com.fly.run.utils.ToastUtil;
import com.fly.run.view.SwipeListView.SwipeMenu;
import com.fly.run.view.SwipeListView.SwipeMenuCreator;
import com.fly.run.view.SwipeListView.SwipeMenuItem;
import com.fly.run.view.SwipeListView.SwipeMenuListView;
import com.fly.run.view.actionbar.CommonActionBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends BaseUIActivity {

    private CommonActionBar actionBar;
    private SwipeMenuListView listView;
    private RecordAdapter adapter;
    private String LogPath = SDCardUtil.getLogDir();
    private List<LatLng> recordList = new ArrayList<>();

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        initView();
        loadDataDB();
    }

    private void initView() {
        initActionBar();
        initSwipeListView();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(RecordActivity.this, RecordMapActivity.class);
                intent.putParcelableArrayListExtra("RecordList", (ArrayList<? extends Parcelable>) recordList);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (adapter.isEditState()) {
            adapter.setEditState(false);
            adapter.notifyDataSetChanged();
            actionBar.setActionRightTextVisiable(View.VISIBLE);
            actionBar.setActionRightIconVisiable(View.GONE);
            return;
        }
        super.onBackPressed();
    }

    private void initActionBar() {
        actionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        actionBar.setActionTitle("我的行程");
        actionBar.setActionLeftIconListenr(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.isEditState()) {
                    adapter.setEditState(false);
                    adapter.notifyDataSetChanged();
                    actionBar.setActionRightTextVisiable(View.VISIBLE);
                    actionBar.setActionRightIconVisiable(View.GONE);
                    return;
                }
                finish();
            }
        });
        actionBar.setActionRightIconListenr(R.drawable.action_delete_normal, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDelBatchTrain();
            }
        });
        actionBar.setActionRightTextListenr("编辑", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionBar.setActionRightIconVisiable(View.VISIBLE);
                v.setVisibility(View.GONE);
                adapter.setEditState(true);
                adapter.notifyDataSetChanged();
            }
        });
        actionBar.setActionRightIconVisiable(View.GONE);
    }

    private SwipeMenuCreator createSwipeMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0x53, 0xbc,
                        0x68)));
                // set item width
                openItem.setWidth(DisplayUtil.dp2px(80));
                // set item title
                openItem.setTitle(getString(R.string.action_swipe_correct_line));
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                SwipeMenuItem delItem = new SwipeMenuItem(
                        getApplicationContext());
                // create "delete" item
                delItem.setBackground(new ColorDrawable(Color.rgb(0xD9, 0x26,
                        0x1B)));
                // set item width
                delItem.setWidth(DisplayUtil.dp2px(80));
                // set item title
                delItem.setTitle(getString(R.string.action_swipe_delete));
                // set item title fontsize
                delItem.setTitleSize(18);
                // set item title font color
                delItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(delItem);
            }
        };
        return creator;
    }

    private void initSwipeListView() {
        listView = (SwipeMenuListView) findViewById(R.id.listview);
        adapter = new RecordAdapter(this);
        listView.setAdapter(adapter);
        // step 1. create a MenuCreator
        // set creator
        listView.setMenuCreator(createSwipeMenu());

        // step 2. listener item click event
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                final RunBean bean = adapter.getItem(position);
                switch (index) {
                    case 0:
                        // edit
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(RecordActivity.this, TraceActivity.class);
                                intent.putExtra("Bean", bean);
                                startActivity(intent);
                            }
                        }, 240);
                        break;
                    case 1:
                        // delete
                        if (adapter.isEditState()) {
                            bean.setCheck(!bean.isCheck());
                            adapter.notifyDataSetChanged();
                            return true;
                        }
                        showAlertDelTrain(bean);
                        break;
                }
                return false;
            }
        });

        // set SwipeListener
        listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // set MenuStateChangeListener
        listView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
            }

            @Override
            public void onMenuClose(int position) {
            }
        });

        // other setting
//		listView.setCloseInterpolator(new BounceInterpolator());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RunBean bean = adapter.getItem(position);
                if (bean.getType() == 1)
                    return;
                if (adapter.isEditState()) {
                    bean.setCheck(!bean.isCheck());
                    adapter.notifyDataSetChanged();
                    return;
                }
                Intent intent = new Intent(RecordActivity.this, SmoothMoveActivity.class);
                intent.putExtra("Bean", bean);
                startActivityForResult(intent, 1024);
            }
        });
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                RunBean bean = adapter.getItem(position);
//                if (adapter.isEditState()) {
//                    bean.setCheck(!bean.isCheck());
//                    adapter.notifyDataSetChanged();
//                    return true;
//                }
//                showAlertDelTrain(bean);
//                return true;
//            }
//        });
    }

    private void loadDataDB() {
        final List<RunBean> list = RunDBHelper.query();
        adapter.setData(list);
        adapter.notifyDataSetChanged();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (RunBean bean : list) {
                    if (bean.getType() == 0) {
                        double mlat = bean.getmLat();
                        double mlon = bean.getmLon();
                        if (mlat != 0 || mlon != 0)
                            recordList.add(new LatLng(mlat, mlon));
                    }
                }
            }
        }, 100);
    }

    private void delRecordFile(String fileName) {
        boolean del = FileUtil.delFile(LogPath + File.separator + fileName);
        if (del)
            loadDataDB();
    }

    private void delRecordDB(RunBean bean) {
        boolean del = RunDBHelper.delete(bean);
        if (del)
            loadDataDB();
        else
            ToastUtil.show("删除数据失败");
    }

    private void delBatchRecordDB() {
        for (RunBean runBean : adapter.getDatas()) {
            if (runBean != null && runBean.isCheck()) {
                boolean del = RunDBHelper.delete(runBean);
            }
        }
        actionBar.setActionRightTextVisiable(View.VISIBLE);
        actionBar.setActionRightIconVisiable(View.GONE);
        adapter.setEditState(false);
        loadDataDB();
    }

    /**
     * 删除单条记录提示
     */
    private void showAlertDelTrain(final RunBean bean) {
        handler.postDelayed(new Runnable() {
            @SuppressLint("NewApi")
            @Override
            public void run() {
                String message = "删除 " + TimeFormatUtils.getFormatDate5(Long.parseLong(bean.getmRunDate())) + " 记录";
                String mStrDel = "删除";
                AlertDialog.Builder builder = new AlertDialog.Builder(RecordActivity.this);
                builder.setTitle("我的行程");
                builder.setMessage(message);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setPositiveButton(mStrDel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                delRecordDB(bean);
                            }
                        }, 200);
                    }
                });
                builder.create();
                builder.show();
            }
        }, 240);
    }


    /**
     * 批量删除记录提示
     */
    private void showAlertDelBatchTrain() {
        handler.postDelayed(new Runnable() {
            @SuppressLint("NewApi")
            @Override
            public void run() {
                String message = "删除选中记录";
                String mStrDel = "删除";
                AlertDialog.Builder builder = new AlertDialog.Builder(RecordActivity.this);
                builder.setTitle("批量删除");
                builder.setMessage(message);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setPositiveButton(mStrDel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                delBatchRecordDB();
                            }
                        }, 200);
                    }
                });
                builder.create();
                builder.show();
            }
        }, 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1024) {
                loadDataDB();
            }
        }
    }
}
