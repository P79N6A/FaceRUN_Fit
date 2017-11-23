package com.fly.run.utils;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps.model.LatLng;
import com.amap.api.trace.TraceLocation;
import com.fly.run.bean.RunBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kongwei on 2017/2/24.
 */

public class TraceParseUtil {

    public static List<TraceLocation> parseLocationsData(RunBean bean) {
        List<TraceLocation> locLists = new ArrayList<TraceLocation>();
        if (bean == null)
            return locLists;
        String locData = bean.getmRunCoordinateList();
        JSONArray array = null;
        if (!TextUtils.isEmpty(locData))
            array = JSON.parseArray(locData);
        for (int i = 0; array != null && i < array.size(); i++) {
            JSONObject traceItem = array.getJSONObject(i);
            TraceLocation location = new TraceLocation();
            location.setLatitude(traceItem.getDoubleValue("mLat"));
            location.setLongitude(traceItem.getDoubleValue("mLon"));
            locLists.add(location);
        }
        return locLists;
    }

    public static List<TraceLocation> parseLocationsData(String path) {
        List<TraceLocation> locLists = new ArrayList<TraceLocation>();
        try {
            String locData = IOTools.readFileSdcardFile(path);
            JSONArray array = null;
            if (!TextUtils.isEmpty(locData))
                array = JSON.parseArray(locData);
            for (int i = 0; array != null && i < array.size(); i++) {
                JSONObject traceItem = array.getJSONObject(i);
                TraceLocation location = new TraceLocation();
                location.setLatitude(traceItem.getDoubleValue("mLat"));
                location.setLongitude(traceItem.getDoubleValue("mLon"));
                locLists.add(location);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return locLists;
    }

    public static List<LatLng> parseLocationLatLng(String path) {
        List<LatLng> locLists = new ArrayList<LatLng>();
        try {
            String locData = IOTools.readFileSdcardFile(path);
            JSONArray array = null;
            if (!TextUtils.isEmpty(locData))
                array = JSON.parseArray(locData);
            for (int i = 0; array != null && i < array.size(); i++) {
                JSONObject traceItem = array.getJSONObject(i);
                LatLng mLatLng = new LatLng(traceItem.getDoubleValue("mLat"), traceItem.getDoubleValue("mLon"));
                locLists.add(mLatLng);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return locLists;
    }

}
