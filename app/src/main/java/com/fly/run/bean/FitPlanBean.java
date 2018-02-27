package com.fly.run.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xinzhendi-031 on 2018/2/27.
 */
public class FitPlanBean implements Serializable{
    private Integer id;
    private Integer fitid;
    private String name;
    private Integer planid;
    private Integer times;
    private Integer counts;
    private Integer countstype;
    private Integer accountid;
    private Date createtime;
    private Integer sort;
    private String title;
    private String description;
    private String image;
    private static final long serialVersionUID = 1L;

    public FitPlanBean() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFitid() {
        return this.fitid;
    }

    public void setFitid(Integer fitid) {
        this.fitid = fitid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name == null?null:name.trim();
    }

    public Integer getPlanid() {
        return this.planid;
    }

    public void setPlanid(Integer planid) {
        this.planid = planid;
    }

    public Integer getTimes() {
        return this.times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Integer getCounts() {
        return this.counts;
    }

    public void setCounts(Integer counts) {
        this.counts = counts;
    }

    public Integer getCountstype() {
        return this.countstype;
    }

    public void setCountstype(Integer countstype) {
        this.countstype = countstype;
    }

    public Integer getAccountid() {
        return this.accountid;
    }

    public void setAccountid(Integer accountid) {
        this.accountid = accountid;
    }

    public Date getCreatetime() {
        return this.createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getSort() {
        return this.sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getTitle() {
        return title;
    }

    public FitPlanBean setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public FitPlanBean setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getImage() {
        return image;
    }

    public FitPlanBean setImage(String image) {
        this.image = image;
        return this;
    }
}
