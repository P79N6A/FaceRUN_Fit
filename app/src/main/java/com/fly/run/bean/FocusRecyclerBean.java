package com.fly.run.bean;

import java.io.Serializable;

/**
 * Created by xinzhendi-031 on 2018/4/13.
 */

public class FocusRecyclerBean implements Serializable {

    private int id;
    private String name;
    private String headerUrl;
    private String headerUrlHD;

    public int getId() {
        return id;
    }

    public FocusRecyclerBean setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public FocusRecyclerBean setName(String name) {
        this.name = name;
        return this;
    }

    public String getHeaderUrl() {
        return headerUrl;
    }

    public FocusRecyclerBean setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
        return this;
    }

    public String getHeaderUrlHD() {
        return headerUrlHD;
    }

    public void setHeaderUrlHD(String headerUrlHD) {
        this.headerUrlHD = headerUrlHD;
    }
}
