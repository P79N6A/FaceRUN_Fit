package com.fly.run.bean;

import java.io.Serializable;

/**
 * Created by hugo on 2015/9/30 0030.
 */
public class NearByBean implements Serializable {

    public String UserID;
    public String UserName;
    public String UserCover;//头像
    public String address;
    public String mNearBy;
    public long locateTime;
    public double latitude;
    public double longitude;
    public boolean onLine;
    public double distance;
    public boolean alertNearby;

    public String getUserID() {
        return UserID;
    }

    public NearByBean setUserID(String userID) {
        UserID = userID;
        return this;
    }

    public String getUserName() {
        return UserName;
    }

    public NearByBean setUserName(String userName) {
        UserName = userName;
        return this;
    }

    public String getUserCover() {
        return UserCover;
    }

    public NearByBean setUserCover(String userCover) {
        UserCover = userCover;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public NearByBean setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getmNearBy() {
        return mNearBy;
    }

    public NearByBean setmNearBy(String mNearBy) {
        this.mNearBy = mNearBy;
        return this;
    }

    public long getLocateTime() {
        return locateTime;
    }

    public NearByBean setLocateTime(long locateTime) {
        this.locateTime = locateTime;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public NearByBean setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public NearByBean setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public boolean isOnLine() {
        return onLine;
    }

    public NearByBean setOnLine(boolean onLine) {
        this.onLine = onLine;
        return this;
    }

    public double getDistance() {
        return distance;
    }

    public NearByBean setDistance(double distance) {
        this.distance = distance;
        return this;
    }

    public boolean isAlertNearby() {
        return alertNearby;
    }

    public NearByBean setAlertNearby(boolean alertNearby) {
        this.alertNearby = alertNearby;
        return this;
    }
}
