package com.fly.run.bean;

import java.io.Serializable;

/**
 * Created by hugo on 2015/9/30 0030.
 */
public class ALocationBean implements Serializable {

    public double mLat;//获取纬度
    public double mLon;//获取经度
    public String mLocationType;//获取当前定位结果来源，如网络定位结果，详见定位类型表
    public String mGpsStatus;//获取GPS的当前状态
    public String mAddress;//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
    public String mAccuracy;//获取精度信息
    public String mCountry;//国家信息
    public String mProvince;//省信息
    public String mCity;//城市信息
    public String mCityCode;//城市编码
    public String mDistrict;//城区信息
    public String mAdCode;//地区编码
    public String mStreet;//街道信息
    public String mAoiName;//获取当前定位点的AOI信息
    public String mPoiName;

    public double getmLat() {
        return mLat;
    }

    public ALocationBean setmLat(double mLat) {
        this.mLat = mLat;
        return this;
    }

    public double getmLon() {
        return mLon;
    }

    public ALocationBean setmLon(double mLon) {
        this.mLon = mLon;
        return this;
    }

    public String getmLocationType() {
        return mLocationType;
    }

    public ALocationBean setmLocationType(String mLocationType) {
        this.mLocationType = mLocationType;
        return this;
    }

    public String getmGpsStatus() {
        return mGpsStatus;
    }

    public ALocationBean setmGpsStatus(String mGpsStatus) {
        this.mGpsStatus = mGpsStatus;
        return this;
    }

    public String getmAddress() {
        return mAddress;
    }

    public ALocationBean setmAddress(String mAddress) {
        this.mAddress = mAddress;
        return this;
    }

    public String getmAccuracy() {
        return mAccuracy;
    }

    public ALocationBean setmAccuracy(String mAccuracy) {
        this.mAccuracy = mAccuracy;
        return this;
    }

    public String getmCountry() {
        return mCountry;
    }

    public ALocationBean setmCountry(String mCountry) {
        this.mCountry = mCountry;
        return this;
    }

    public String getmProvince() {
        return mProvince;
    }

    public ALocationBean setmProvince(String mProvince) {
        this.mProvince = mProvince;
        return this;
    }

    public String getmCity() {
        return mCity;
    }

    public ALocationBean setmCity(String mCity) {
        this.mCity = mCity;
        return this;
    }

    public String getmCityCode() {
        return mCityCode;
    }

    public ALocationBean setmCityCode(String mCityCode) {
        this.mCityCode = mCityCode;
        return this;
    }

    public String getmDistrict() {
        return mDistrict;
    }

    public ALocationBean setmDistrict(String mDistrict) {
        this.mDistrict = mDistrict;
        return this;
    }

    public String getmAdCode() {
        return mAdCode;
    }

    public ALocationBean setmAdCode(String mAdCode) {
        this.mAdCode = mAdCode;
        return this;
    }

    public String getmStreet() {
        return mStreet;
    }

    public ALocationBean setmStreet(String mStreet) {
        this.mStreet = mStreet;
        return this;
    }

    public String getmAoiName() {
        return mAoiName;
    }

    public ALocationBean setmAoiName(String mAoiName) {
        this.mAoiName = mAoiName;
        return this;
    }

    public String getmPoiName() {
        return mPoiName;
    }

    public ALocationBean setmPoiName(String mPoiName) {
        this.mPoiName = mPoiName;
        return this;
    }
}
