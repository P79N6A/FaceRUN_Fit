package com.fly.run.activity.training;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.fly.run.R;
import com.fly.run.activity.MainRunActivity;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.utils.SharePreferceTool;
import com.fly.run.utils.ToastUtil;
import com.fly.run.view.TrainPlanView;
import com.fly.run.view.actionbar.CommonActionBar;

public class TrainPlanActivity extends BaseUIActivity {

    public static String[] titles1 = {"菜鸟营", "1公里", "2公里", "3公里"};
    public static String[] titles2 = {"挑战赛", "5公里", "10公里", "限时赛"};
    public static String[] titles3 = {"大师级", "半程马拉松", "马拉松", "规定变速赛"};

    private CommonActionBar actionBar;
    private LinearLayout layout_content;
    private TrainPlanView viewPlanBird, viewPlanChallenge, viewPlanMaster;
    public static int level = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_plan);
        initActionBar();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        level = SharePreferceTool.getInstance().getInt("LEVEL", 1);
        initData();
    }

    private void initActionBar() {
        actionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        actionBar.setActionTitle("跑步训练");
        actionBar.setActionLeftIconListenr(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        layout_content = (LinearLayout) findViewById(R.id.layout_content);
        viewPlanBird = new TrainPlanView(this);
        viewPlanChallenge = new TrainPlanView(this);
        viewPlanMaster = new TrainPlanView(this);
        layout_content.addView(viewPlanBird);
        layout_content.addView(viewPlanChallenge);
        layout_content.addView(viewPlanMaster);
        viewPlanBird.setClickListener(clickListener_1);
        viewPlanChallenge.setClickListener(clickListener_2);
        viewPlanMaster.setClickListener(clickListener_3);
    }

    private void initData() {
        viewPlanBird.initData(titles1);
        viewPlanChallenge.initData(titles2);
        viewPlanMaster.initData(titles3);

        viewPlanBird.setLevel(level >= 3 ? 3 : level);
        if (level >= 6)
            viewPlanChallenge.setLevel(3);
        else if (level < 4) {
            viewPlanChallenge.setLevel(0);
        } else {
            viewPlanChallenge.setLevel(level - 3);
        }
        viewPlanMaster.setLevel(level > 6 ? level - 6 : 0);
    }

    TrainPlanView.ClickListener clickListener_1 = new TrainPlanView.ClickListener() {
        @Override
        public void runLow() {
            intentExtraToActivity(titles1[1], 1);
        }

        @Override
        public void runMid() {
            if (level < 2) {
                ToastUtil.show("权限不够，欲速则不达");
                return;
            }
            intentExtraToActivity(titles1[2], 2);
        }

        @Override
        public void runHigh() {
            if (level < 3) {
                ToastUtil.show("权限不够，欲速则不达");
                return;
            }
            intentExtraToActivity(titles1[3], 3);
        }
    };

    TrainPlanView.ClickListener clickListener_2 = new TrainPlanView.ClickListener() {
        @Override
        public void runLow() {
            if (level < 4) {
                ToastUtil.show("权限不够，欲速则不达");
                return;
            }
            intentExtraToActivity(titles2[1], 4);
        }

        @Override
        public void runMid() {
            if (level < 5) {
                ToastUtil.show("权限不够，欲速则不达");
                return;
            }
            intentExtraToActivity(titles2[2], 5);
        }

        @Override
        public void runHigh() {
            if (level < 6) {
                ToastUtil.show("权限不够，欲速则不达");
                return;
            }
            intentExtraToActivity(titles2[3], 6);
        }
    };
    TrainPlanView.ClickListener clickListener_3 = new TrainPlanView.ClickListener() {
        @Override
        public void runLow() {
            if (level < 7) {
                ToastUtil.show("权限不够，欲速则不达");
                return;
            }
            intentExtraToActivity(titles3[1], 7);
        }

        @Override
        public void runMid() {
            if (level < 8) {
                ToastUtil.show("权限不够，欲速则不达");
                return;
            }
            intentExtraToActivity(titles3[2], 8);
        }

        @Override
        public void runHigh() {
            if (level < 9) {
                ToastUtil.show("权限不够，欲速则不达");
                return;
            }
            intentExtraToActivity(titles3[3], 9);
        }
    };

    protected void intentExtraToActivity(String data, int level) {
        Intent intent = new Intent(TrainPlanActivity.this, MainRunActivity.class);
        intent.putExtra("DATA", data);
        intent.putExtra("LEVEL", level);
        startActivity(intent);
    }

}
