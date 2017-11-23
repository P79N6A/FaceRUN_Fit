package com.fly.run.bean;

/**
 * Created by kongwei on 2017/3/1.
 */

public class GroupRunBean {
    private int type;
    private String runDateFormat;
    private double runHeadDistance;

    public int getType() {
        return type;
    }

    public GroupRunBean setType(int type) {
        this.type = type;
        return this;
    }

    public String getRunDateFormat() {
        return runDateFormat;
    }

    public GroupRunBean setRunDateFormat(String runDateFormat) {
        this.runDateFormat = runDateFormat;
        return this;
    }

    public double getRunHeadDistance() {
        return runHeadDistance;
    }

    public GroupRunBean setRunHeadDistance(double runHeadDistance) {
        this.runHeadDistance = runHeadDistance;
        return this;
    }
}
