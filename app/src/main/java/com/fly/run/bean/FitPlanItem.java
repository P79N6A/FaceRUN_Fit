package com.fly.run.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xinzhendi-031 on 2018/2/27.
 */
public class FitPlanItem implements Serializable {

    String planName;
    List<FitPlanBean> planList;

    public String getPlanName() {
        return planName;
    }

    public FitPlanItem setPlanName(String planName) {
        this.planName = planName;
        return this;
    }

    public List<FitPlanBean> getPlanList() {
        return planList;
    }

    public FitPlanItem setPlanList(List<FitPlanBean> planList) {
        this.planList = planList;
        return this;
    }
}
